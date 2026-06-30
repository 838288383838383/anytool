package com.anydebloat.tools

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Environment
import android.os.StatFs
import android.util.Log
import java.io.File

object StorageAnalyzer {

    private const val TAG = "StorageAnalyzer"

    data class StorageStats(
        val totalStorage: Long,
        val usedStorage: Long,
        val freeStorage: Long,
        val appSize: Long,
        val cacheSize: Long,
        val dataSize: Long,
        val mediaSize: Long
    )

    data class AppSize(
        val packageName: String,
        val appName: String,
        val appSize: Long,
        val cacheSize: Long,
        val dataSize: Long
    )

    fun getStorageStats(): StorageStats {
        val stat = StatFs(Environment.getDataDirectory().path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong

        val totalStorage = totalBlocks * blockSize
        val freeStorage = availableBlocks * blockSize
        val usedStorage = totalStorage - freeStorage

        return StorageStats(
            totalStorage = totalStorage,
            usedStorage = usedStorage,
            freeStorage = freeStorage,
            appSize = 0,
            cacheSize = getCacheDirSize(),
            dataSize = getDataDirSize(),
            mediaSize = getMediaDirSize()
        )
    }

    fun getAppSizes(context: Context): List<AppSize> {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        return packages.mapNotNull { appInfo ->
            try {
                val appSize = getAppSize(appInfo.sourceDir)
                val cacheSize = getCacheSize(context, appInfo.packageName)
                val dataSize = getDataSize(context, appInfo.packageName)

                if (appSize > 0 || cacheSize > 0 || dataSize > 0) {
                    AppSize(
                        packageName = appInfo.packageName,
                        appName = pm.getApplicationLabel(appInfo).toString(),
                        appSize = appSize,
                        cacheSize = cacheSize,
                        dataSize = dataSize
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error calculating size for ${appInfo.packageName}", e)
                null
            }
        }.sortedByDescending { it.appSize + it.cacheSize + it.dataSize }
    }

    fun getTopApps(context: Context, limit: Int = 10): List<AppSize> {
        return getAppSizes(context).take(limit)
    }

    fun getStorageUsagePercent(): Float {
        val stats = getStorageStats()
        return ((stats.usedStorage.toFloat() / stats.totalStorage) * 100)
    }

    fun getCacheCleanupSize(context: Context): Long {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(0)
        return packages.sumOf { getCacheSize(context, it.packageName) }
    }

    private fun getAppSize(sourceDir: String): Long {
        return try {
            File(sourceDir).length()
        } catch (e: Exception) {
            0L
        }
    }

    private fun getCacheSize(context: Context, packageName: String): Long {
        return try {
            val cacheDir = context.cacheDir
            calculateDirSize(cacheDir)
        } catch (e: Exception) {
            0L
        }
    }

    private fun getDataSize(context: Context, packageName: String): Long {
        return try {
            val dataDir = context.dataDir
            calculateDirSize(dataDir)
        } catch (e: Exception) {
            0L
        }
    }

    private fun getCacheDirSize(): Long {
        return try {
            calculateDirSize(Environment.getDownloadCacheDirectory())
        } catch (e: Exception) {
            0L
        }
    }

    private fun getDataDirSize(): Long {
        return try {
            calculateDirSize(Environment.getDataDirectory())
        } catch (e: Exception) {
            0L
        }
    }

    private fun getMediaDirSize(): Long {
        return try {
            val mediaDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            calculateDirSize(mediaDir)
        } catch (e: Exception) {
            0L
        }
    }

    private fun calculateDirSize(dir: File): Long {
        return if (!dir.exists()) {
            0L
        } else if (dir.isFile) {
            dir.length()
        } else {
            dir.listFiles()?.sumOf { calculateDirSize(it) } ?: 0L
        }
    }
}
