package com.anydebloat.debloater

import android.util.Log
import com.anydebloat.models.DebloatMode
import com.anydebloat.models.DebloatResult
import com.anydebloat.models.PackageInfo
import com.anydebloat.shizuku.ShizukuService

object Debloater {

    private const val TAG = "Debloater"

    fun debloat(packageInfo: PackageInfo, mode: DebloatMode): DebloatResult {
        Log.d(TAG, "Debloating ${packageInfo.packageName} with mode $mode")

        return when (mode) {
            DebloatMode.NORMAL -> debloatNormal(packageInfo)
            DebloatMode.ZERO_DAY -> debloatZeroDay(packageInfo)
            DebloatMode.BRUTE -> debloatBrute(packageInfo)
        }
    }

    private fun debloatNormal(packageInfo: PackageInfo): DebloatResult {
        // Step 1: Force stop the app
        ShizukuService.forceStopPackage(packageInfo.packageName)

        // Step 2: Disable the app for current user
        val result = ShizukuService.disablePackage(packageInfo.packageName)

        return if (result.first) {
            Log.d(TAG, "Normal debloat success: ${packageInfo.packageName}")
            DebloatResult(
                packageName = packageInfo.packageName,
                success = true,
                method = DebloatMode.NORMAL,
                message = "Package disabled successfully"
            )
        } else {
            DebloatResult(
                packageName = packageInfo.packageName,
                success = false,
                method = DebloatMode.NORMAL,
                message = "Failed to disable: ${result.second}"
            )
        }
    }

    private fun debloatZeroDay(packageInfo: PackageInfo): DebloatResult {
        // Step 1: Force stop
        ShizukuService.forceStopPackage(packageInfo.packageName)

        // Step 2: Clear all data
        ShizukuService.clearPackageData(packageInfo.packageName)

        // Step 3: Try to uninstall for user (more aggressive than disable)
        val uninstallResult = ShizukuService.uninstallPackage(packageInfo.packageName)

        if (uninstallResult.first) {
            Log.d(TAG, "ZeroDay uninstall success: ${packageInfo.packageName}")
            return DebloatResult(
                packageName = packageInfo.packageName,
                success = true,
                method = DebloatMode.ZERO_DAY,
                message = "Package uninstalled for user"
            )
        }

        // Step 4: Fallback to disable if uninstall fails
        val disableResult = ShizukuService.disablePackage(packageInfo.packageName)

        if (disableResult.first) {
            // Step 5: Also revoke dangerous permissions as extra measure
            revokeDangerousPermissions(packageInfo.packageName)

            Log.d(TAG, "ZeroDay disable fallback: ${packageInfo.packageName}")
            return DebloatResult(
                packageName = packageInfo.packageName,
                success = true,
                method = DebloatMode.ZERO_DAY,
                message = "Package disabled (uninstall failed), permissions revoked"
            )
        }

        return DebloatResult(
            packageName = packageInfo.packageName,
            success = false,
            method = DebloatMode.ZERO_DAY,
            message = "Failed: ${uninstallResult.second}\nFallback: ${disableResult.second}"
        )
    }

    private fun debloatBrute(packageInfo: PackageInfo): DebloatResult {
        // Check if root is available
        if (!ShizukuService.isRootAvailable()) {
            return DebloatResult(
                packageName = packageInfo.packageName,
                success = false,
                method = DebloatMode.BRUTE,
                message = "Root access not available. Use Normal or ZeroDay mode."
            )
        }

        // Step 1: Force stop
        ShizukuService.forceStopPackage(packageInfo.packageName)

        // Step 2: Clear data
        ShizukuService.clearPackageData(packageInfo.packageName)

        // Step 3: Attempt full removal with root
        val rootResult = ShizukuService.executeCommand(
            "su -c 'pm uninstall -k --user 0 ${packageInfo.packageName}'"
        )

        if (rootResult.first) {
            // Step 4: Also try to remove from system partition if root
            ShizukuService.executeCommand(
                "su -c 'rm -rf /system/app/${packageInfo.packageName} 2>/dev/null'"
            )
            ShizukuService.executeCommand(
                "su -c 'rm -rf /system/priv-app/${packageInfo.packageName} 2>/dev/null'"
            )
            ShizukuService.executeCommand(
                "su -c 'rm -rf /data/app/${packageInfo.packageName} 2>/dev/null'"
            )

            Log.d(TAG, "Brute success: ${packageInfo.packageName}")
            return DebloatResult(
                packageName = packageInfo.packageName,
                success = true,
                method = DebloatMode.BRUTE,
                message = "Package fully removed with root"
            )
        }

        // Step 5: Fallback to disable
        val disableResult = ShizukuService.disablePackage(packageInfo.packageName)
        return if (disableResult.first) {
            DebloatResult(
                packageName = packageInfo.packageName,
                success = true,
                method = DebloatMode.BRUTE,
                message = "Root removal failed, package disabled instead"
            )
        } else {
            DebloatResult(
                packageName = packageInfo.packageName,
                success = false,
                method = DebloatMode.BRUTE,
                message = "All methods failed: ${rootResult.second}\n${disableResult.second}"
            )
        }
    }

    private fun revokeDangerousPermissions(packageName: String) {
        val dangerousPermissions = listOf(
            "android.permission.READ_CONTACTS",
            "android.permission.WRITE_CONTACTS",
            "android.permission.READ_SMS",
            "android.permission.SEND_SMS",
            "android.permission.READ_PHONE_STATE",
            "android.permission.CALL_PHONE",
            "android.permission.READ_CALL_LOG",
            "android.permission.WRITE_CALL_LOG",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_MEDIA_IMAGES",
            "android.permission.READ_MEDIA_VIDEO",
            "android.permission.READ_MEDIA_AUDIO",
            "android.permission.POST_NOTIFICATIONS",
        )

        for (permission in dangerousPermissions) {
            ShizukuService.revokePermission(packageName, permission)
        }
    }

    fun restorePackage(packageName: String): DebloatResult {
        val result = ShizukuService.enablePackage(packageName)

        return if (result.first) {
            DebloatResult(
                packageName = packageName,
                success = true,
                method = DebloatMode.NORMAL,
                message = "Package restored successfully"
            )
        } else {
            DebloatResult(
                packageName = packageName,
                success = false,
                method = DebloatMode.NORMAL,
                message = "Failed to restore: ${result.second}"
            )
        }
    }

    fun debloatMultiple(
        packages: List<PackageInfo>,
        mode: DebloatMode,
        onProgress: (Int, Int, DebloatResult) -> Unit
    ): List<DebloatResult> {
        val results = mutableListOf<DebloatResult>()

        packages.forEachIndexed { index, packageInfo ->
            val result = debloat(packageInfo, mode)
            results.add(result)
            onProgress(index + 1, packages.size, result)
        }

        return results
    }
}
