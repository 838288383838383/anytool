package com.anydebloat.ui

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anydebloat.databinding.ActivityWifiHotspotBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import java.lang.reflect.Method

class WifiHotspotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWifiHotspotBinding
    private var isHotspotOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiHotspotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "WiFi Hotspot"

        checkHotspotStatus()
        setupButtons()
    }

    private fun setupButtons() {
        binding.btnToggleHotspot.setOnClickListener {
            if (isHotspotOn) stopHotspot() else startHotspot()
        }
    }

    private fun checkHotspotStatus() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        try {
            val method: Method = wifiManager.javaClass.getDeclaredMethod("isWifiApEnabled")
            method.isAccessible = true
            isHotspotOn = method.invoke(wifiManager) as Boolean
        } catch (e: Exception) {
            isHotspotOn = false
        }

        binding.tvHotspotStatus.text = if (isHotspotOn) "Hotspot: ON" else "Hotspot: OFF"
        binding.btnToggleHotspot.text = if (isHotspotOn) "Stop Hotspot" else "Start Hotspot"
    }

    private fun startHotspot() {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                // Try via settings command first (works without root on many devices)
                ShizukuService.executeCommand("svc wifi enablehotspot")
            }

            if (!result.first) {
                // Fallback: try via reflection
                try {
                    val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val method: Method = wifiManager.javaClass.getDeclaredMethod(
                        "setWifiApEnabled",
                        android.net.wifi.WifiConfiguration::class.java,
                        Boolean::class.javaPrimitiveType
                    )
                    method.isAccessible = true
                    val config = android.net.wifi.WifiConfiguration().apply {
                        SSID = "AnyTool_Hotspot"
                        preSharedKey = "anytool123"
                    }
                    method.invoke(wifiManager, config, true)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@WifiHotspotActivity, "Hotspot started", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@WifiHotspotActivity, "Failed to start hotspot: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WifiHotspotActivity, "Hotspot started", Toast.LENGTH_SHORT).show()
                }
            }

            withContext(Dispatchers.Main) {
                checkHotspotStatus()
            }
        }
    }

    private fun stopHotspot() {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                ShizukuService.executeCommand("svc wifi disablehotspot")
            }

            if (!result.first) {
                try {
                    val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val method: Method = wifiManager.javaClass.getDeclaredMethod(
                        "setWifiApEnabled",
                        android.net.wifi.WifiConfiguration::class.java,
                        Boolean::class.javaPrimitiveType
                    )
                    method.isAccessible = true
                    method.invoke(wifiManager, null, false)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@WifiHotspotActivity, "Failed to stop hotspot", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            withContext(Dispatchers.Main) {
                checkHotspotStatus()
                Toast.makeText(this@WifiHotspotActivity, "Hotspot stopped", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
