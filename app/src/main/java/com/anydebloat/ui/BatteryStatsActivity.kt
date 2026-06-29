package com.anydebloat.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.anydebloat.databinding.ActivityBatteryStatsBinding

class BatteryStatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBatteryStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Battery Stats"

        loadBatteryInfo()
    }

    private fun loadBatteryInfo() {
        val batteryIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        batteryIntent?.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val percentage = if (level != -1 && scale != -1) (level * 100) / scale.toFloat() else 0f

            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val statusStr = when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
                BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
                BatteryManager.BATTERY_STATUS_FULL -> "Full"
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging"
                else -> "Unknown"
            }

            val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f
            val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000f
            val technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown"

            val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            val plugStr = when (plugged) {
                BatteryManager.BATTERY_PLUGGED_AC -> "AC"
                BatteryManager.BATTERY_PLUGGED_USB -> "USB"
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
                else -> "Not Plugged"
            }

            val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
            val healthStr = when (health) {
                BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
                BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
                BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
                else -> "Unknown"
            }

            binding.tvPercentage.text = "${percentage.toInt()}%"
            binding.tvStatus.text = "Status: $statusStr"
            binding.tvTechnology.text = "Technology: $technology"
            binding.tvTemperature.text = "Temperature: ${temperature}C"
            binding.tvVoltage.text = "Voltage: ${voltage}V"
            binding.tvPlugged.text = "Plugged: $plugStr"
            binding.tvHealth.text = "Health: $healthStr"

            binding.progressBattery.progress = percentage.toInt()
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
