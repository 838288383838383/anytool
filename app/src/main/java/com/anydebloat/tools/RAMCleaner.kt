package com.anydebloat.tools

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import android.util.Log
import com.anydebloat.shizuku.ShizukuService

object RAMCleaner {

    private const val TAG = "RAMCleaner"

    data class MemoryStats(
        val totalMemory: Long,
        val availableMemory: Long,
        val usedMemory: Long,
        val cachedMemory: Long,
        val cleanedSize: Long = 0
    )

    fun getMemoryStats(context: Context): MemoryStats {
        val runtime = Runtime.getRuntime()
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory

        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memInfo)

        return MemoryStats(
            totalMemory = memInfo.totalMem,
            availableMemory = memInfo.availMem,
            usedMemory = memInfo.totalMem - memInfo.availMem,
            cachedMemory = getCachedMemory(context)
        )
    }

    fun cleanRAM(context: Context): MemoryStats {
        Log.d(TAG, "Starting RAM cleanup...")

        val initialStats = getMemoryStats(context)

        System.gc()
        System.runFinalization()

        ShizukuService.executeCommand("pm trim-caches 2147483647")
        killNonEssentialApps(context)

        val finalStats = getMemoryStats(context)
        val cleanedSize = initialStats.usedMemory - finalStats.usedMemory

        Log.d(TAG, "RAM cleanup completed. Freed: ${cleanedSize / 1024 / 1024}MB")

        return finalStats.copy(cleanedSize = cleanedSize)
    }

    fun getMemoryUsagePercent(context: Context): Float {
        val stats = getMemoryStats(context)
        return ((stats.usedMemory.toFloat() / stats.totalMemory) * 100)
    }

    private fun getCachedMemory(context: Context): Long {
        return try {
            Debug.getNativeHeap().sumOf { it.allocatedSize }
        } catch (e: Exception) {
            0L
        }
    }

    private fun killNonEssentialApps(context: Context) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val essentialApps = setOf("com.android.systemui", "android.uid.system", context.packageName)

        try {
            val runningApps = activityManager.runningAppProcesses
            for (process in runningApps) {
                if (!essentialApps.contains(process.processName) && 
                    process.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    ShizukuService.executeCommand("am kill ${process.processName}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error killing apps", e)
        }
    }
}
