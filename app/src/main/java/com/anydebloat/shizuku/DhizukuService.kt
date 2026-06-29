package com.anydebloat.shizuku

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.rosan.dhizuku.api.Dhizuku
import com.rosan.dhizuku.api.DhizukuRequestPermissionListener

object DhizukuService {

    private const val TAG = "DhizukuService"
    var isAvailable = false
        private set
    var isPermissionGranted = false
        private set

    fun init(context: Context): Boolean {
        return try {
            isAvailable = Dhizuku.init(context)
            isPermissionGranted = Dhizuku.isPermissionGranted()
            Log.d(TAG, "Dhizuku init: available=$isAvailable, permission=$isPermissionGranted")
            isAvailable
        } catch (e: Exception) {
            Log.e(TAG, "Dhizuku init failed", e)
            false
        }
    }

    fun requestPermission(listener: (Boolean) -> Unit) {
        Dhizuku.requestPermission(object : DhizukuRequestPermissionListener() {
            override fun onRequestPermission(grantResult: Int) {
                isPermissionGranted = grantResult == android.content.pm.PackageManager.PERMISSION_GRANTED
                Log.d(TAG, "Dhizuku permission result: $isPermissionGranted")
                listener(isPermissionGranted)
            }
        })
    }

    fun getAdminComponent(): ComponentName? {
        return try {
            Dhizuku.getOwnerPackageName()
            null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get admin component", e)
            null
        }
    }

    fun executeAsDeviceOwner(command: String): Pair<Boolean, String> {
        if (!isPermissionGranted) {
            return Pair(false, "Dhizuku permission not granted")
        }
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            val stdout = process.inputStream.bufferedReader().readText()
            val stderr = process.errorStream.bufferedReader().readText()
            val exitCode = process.waitFor()
            if (exitCode == 0) Pair(true, stdout.trim()) else Pair(false, stderr.trim())
        } catch (e: Exception) {
            Pair(false, e.message ?: "Unknown error")
        }
    }

    fun disablePackage(packageName: String): Pair<Boolean, String> {
        return executeAsDeviceOwner("pm disable-user --user 0 $packageName")
    }

    fun enablePackage(packageName: String): Pair<Boolean, String> {
        return executeAsDeviceOwner("pm enable $packageName")
    }

    fun uninstallPackage(packageName: String): Pair<Boolean, String> {
        return executeAsDeviceOwner("pm uninstall --user 0 $packageName")
    }
}
