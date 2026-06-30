package com.anydebloat.shizuku

import android.os.IBinder
import android.util.Log
import com.anydebloat.AnyToolApp
import rikka.shizuku.Shizuku

object ShizukuService {

    private const val TAG = "ShizukuService"
    private var isBound = false

    fun bind(binder: IBinder?) {
        isBound = true
        Log.d(TAG, "Shizuku service bound")
    }

    fun isAvailable(): Boolean {
        return try {
            Shizuku.pingBinder() && isBound
        } catch (e: Exception) {
            false
        }
    }

    fun executeCommand(command: String): Pair<Boolean, String> {
        if (!isAvailable()) {
            return Pair(false, "Shizuku not available")
        }

        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            val stdout = process.inputStream.bufferedReader().use { it.readText() }
            val stderr = process.errorStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()

            if (exitCode == 0) {
                Log.d(TAG, "Command success: $command")
                Pair(true, stdout.trim())
            } else {
                Log.w(TAG, "Command failed (exit $exitCode): $command\n$stderr")
                Pair(false, stderr.trim())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Command error: $command", e)
            Pair(false, e.message ?: "Unknown error")
        }
    }

    fun disablePackage(packageName: String): Pair<Boolean, String> {
        return executeCommand("pm disable-user --user 0 $packageName")
    }

    fun enablePackage(packageName: String): Pair<Boolean, String> {
        return executeCommand("pm enable $packageName")
    }

    fun uninstallPackage(packageName: String): Pair<Boolean, String> {
        return executeCommand("pm uninstall --user 0 $packageName")
    }

    fun uninstallPackageRoot(packageName: String): Pair<Boolean, String> {
        return executeCommand("pm uninstall -k --user 0 $packageName")
    }

    fun isPackageInstalled(packageName: String): Boolean {
        val result = executeCommand("pm list packages $packageName")
        return result.first && result.second.contains("package:$packageName")
    }

    fun isRootAvailable(): Boolean {
        val result = executeCommand("su -c id")
        return result.first && result.second.contains("uid=0")
    }

    fun getInstalledPackages(): List<String> {
        val result = executeCommand("pm list packages --user 0")
        if (!result.first) return emptyList()

        return result.second.lines()
            .filter { it.startsWith("package:") }
            .map { it.removePrefix("package:") }
            .sorted()
    }

    fun isPackageEnabled(packageName: String): Boolean {
        val result = executeCommand("pm list packages -e $packageName")
        return result.first && result.second.contains("package:$packageName")
    }

    fun forceStopPackage(packageName: String): Pair<Boolean, String> {
        return executeCommand("am force-stop $packageName")
    }

    fun clearPackageData(packageName: String): Pair<Boolean, String> {
        return executeCommand("pm clear $packageName")
    }

    fun grantPermission(packageName: String, permission: String): Pair<Boolean, String> {
        return executeCommand("pm grant $packageName $permission")
    }

    fun revokePermission(packageName: String, permission: String): Pair<Boolean, String> {
        return executeCommand("pm revoke $packageName $permission")
    }

    fun getPackageInfo(packageName: String): String {
        val result = executeCommand("dumpsys package $packageName | head -50")
        return if (result.first) result.second else "Error: ${result.second}"
    }
}
