package com.anydebloat.tools

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.anydebloat.shizuku.ShizukuService

object BatteryOptimizer {

    private const val TAG = "BatteryOptimizer"

    data class BatteryInfo(
        val level: Int,
        val status: String,
        val temperature: Int,
        val voltage: Int,
        val health: String,
        val technology: String,
        val isCharging: Boolean
    )

    data class BatteryStats(
        val screenOnTime: Long,
        val screenOffTime: Long,
        val totalRunTime: Long,
        val topApps: List<String>
    )

    fun getBatteryInfo(context: Context): BatteryInfo {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            ?: return BatteryInfo(0, "Unknown", 0, 0, "Unknown", "Unknown", false)

        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val status = when (intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            else -> "Unknown"
        }
        val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        val health = when (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            else -> "Unknown"
        }
        val technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown"
        val isCharging = status == "Charging"

        return BatteryInfo(level, status, temperature, voltage, health, technology, isCharging)
    }

    fun optimizeBattery(context: Context): String {
        Log.d(TAG, "Optimizing battery settings...")

        val optimizations = mutableListOf<String>()

        ShizukuService.executeCommand("settings put global wifi_scan_interval_when_connected_ms 180000").let {
            if (it.first) optimizations.add("✓ WiFi scan optimization")
        }

        ShizukuService.executeCommand("settings put global location_mode 0").let {
            if (it.first) optimizations.add("✓ Location services optimized")
        }

        ShizukuService.executeCommand("settings put global adaptive_battery_management_enabled 1").let {
            if (it.first) optimizations.add("✓ Adaptive battery enabled")
        }

        ShizukuService.executeCommand("settings put global background_throttle_mode 1").let {
            if (it.first) optimizations.add("✓ Background throttling enabled")
        }

        ShizukuService.executeCommand("settings put global low_power_mode_extra_save_enabled 1").let {
            if (it.first) optimizations.add("✓ Extra power saving enabled")
        }

        Log.d(TAG, "Battery optimization completed")
        return optimizations.joinToString("\n")
    }

    fun enableBatteryProtection(context: Context): String {
        Log.d(TAG, "Enabling battery protection...")

        val results = mutableListOf<String>()

        ShizukuService.executeCommand("settings put global battery_saver_mode_enabled 1").let {
            if (it.first) results.add("✓ Battery saver enabled")
        }

        ShizukuService.executeCommand("pm disable-user com.google.android.gms.location.history").let {
            if (it.first) results.add("✓ Location history disabled")
        }

        ShizukuService.executeCommand("settings put system accelerometer_rotation 0").let {
            if (it.first) results.add("✓ Auto-rotation disabled")
        }

        return results.joinToString("\n")
    }

    fun getOptimizationRecommendations(batteryInfo: BatteryInfo): List<String> {
        val recommendations = mutableListOf<String>()

        if (batteryInfo.temperature > 40) {
            recommendations.add("Device is overheating (${batteryInfo.temperature}°C) - Reduce usage")
        }

        if (batteryInfo.level < 20) {
            recommendations.add("Battery low - Enable battery saver mode")
        }

        if (batteryInfo.health != "Good") {
            recommendations.add("Battery health degraded - Reduce usage")
        }

        if (!batteryInfo.isCharging && batteryInfo.level < 50) {
            recommendations.add("Battery below 50% - Consider charging soon")
        }

        return recommendations
    }
}
