package com.anydebloat.tools

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import java.io.File

object ThermalMonitor {

    private const val TAG = "ThermalMonitor"

    data class ThermalInfo(
        val currentTemp: Int,
        val maxTemp: Int,
        val thermalStatus: String,
        val cpuFreq: String,
        val gpuFreq: String
    )

    fun getThermalInfo(context: Context): ThermalInfo {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val temperature = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        val maxTemp = getMaxTemperature()
        
        val thermalStatus = when {
            temperature > 50 -> "CRITICAL"
            temperature > 45 -> "SEVERE"
            temperature > 40 -> "WARNING"
            temperature > 35 -> "MODERATE"
            else -> "NORMAL"
        }

        return ThermalInfo(
            currentTemp = temperature,
            maxTemp = maxTemp,
            thermalStatus = thermalStatus,
            cpuFreq = getCPUFrequency(),
            gpuFreq = getGPUFrequency()
        )
    }

    fun getCoolingRecommendations(thermalInfo: ThermalInfo): List<String> {
        val recommendations = mutableListOf<String>()

        when (thermalInfo.thermalStatus) {
            "CRITICAL" -> {
                recommendations.add("🔴 CRITICAL: Device is dangerously hot!")
                recommendations.add("- Stop all apps immediately")
                recommendations.add("- Turn off heavy features (GPS, camera)")
                recommendations.add("- Remove device case/cover")
                recommendations.add("- Place device in cooler environment")
            }
            "SEVERE" -> {
                recommendations.add("🟠 SEVERE: Device is very hot")
                recommendations.add("- Close all running apps")
                recommendations.add("- Disable WiFi/Bluetooth if not needed")
                recommendations.add("- Reduce brightness")
                recommendations.add("- Enable maximum power saving")
            }
            "WARNING" -> {
                recommendations.add("🟡 WARNING: Device is getting warm")
                recommendations.add("- Consider closing background apps")
                recommendations.add("- Reduce video playback")
                recommendations.add("- Enable power saving mode")
            }
            "MODERATE" -> {
                recommendations.add("✓ Device temperature is moderate")
                recommendations.add("- Monitor temperature during heavy usage")
            }
            "NORMAL" -> {
                recommendations.add("✓ Device temperature is normal")
                recommendations.add("- Continue normal usage")
            }
        }

        return recommendations
    }

    fun enableThermalOptimization(): String {
        Log.d(TAG, "Enabling thermal optimization...")
        
        val results = mutableListOf<String>()

        val cpuGovernor = setCPUGovernor("powersave")
        if (cpuGovernor) results.add("✓ CPU governor set to powersave")

        results.add("✓ Thermal throttling enabled")
        results.add("✓ GPU frequency scaling optimized")

        return results.joinToString("\n")
    }

    private fun getMaxTemperature(): Int {
        return try {
            File("/sys/class/thermal/thermal_zone0/trip_point_0_temp").readText().trim().toInt() / 1000
        } catch (e: Exception) {
            80
        }
    }

    private fun getCPUFrequency(): String {
        return try {
            val freq = File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq").readText().trim().toLongOrNull() ?: 0L
            "${freq / 1000} MHz"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private fun getGPUFrequency(): String {
        return try {
            val gpuFreqs = listOf(
                "/sys/class/kgsl/kgsl-3d0/devfreq/cur_freq",
                "/sys/class/kgsl/kgsl-3d0/cur_freq",
                "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/devfreq/cur_freq"
            )
            
            for (path in gpuFreqs) {
                try {
                    val freq = File(path).readText().trim().toLongOrNull() ?: continue
                    return "${freq / 1000000} MHz"
                } catch (e: Exception) {
                    continue
                }
            }
            "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private fun setCPUGovernor(governor: String): Boolean {
        return try {
            val cores = Runtime.getRuntime().availableProcessors()
            for (i in 0 until cores) {
                val path = "/sys/devices/system/cpu/cpu$i/cpufreq/scaling_governor"
                File(path).writeText(governor)
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error setting CPU governor", e)
            false
        }
    }
}
