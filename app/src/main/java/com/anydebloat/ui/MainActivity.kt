package com.anydebloat.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.anydebloat.R
import com.anydebloat.databinding.ActivityMainBinding
import com.anydebloat.models.OEM
import com.anydebloat.shizuku.DhizukuService
import com.anydebloat.shizuku.ShizukuService
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val shizukuPermissionResultListener =
        Shizuku.OnRequestPermissionResultListener { _, _ -> checkServiceStatus() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "AnyTool"

        Shizuku.addRequestPermissionResultListener(shizukuPermissionResultListener)
        setupUI()
        checkServiceStatus()
    }

    override fun onResume() {
        super.onResume()
        checkServiceStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        Shizuku.removeRequestPermissionResultListener(shizukuPermissionResultListener)
    }

    private fun setupUI() {
        // Debloat
        binding.btnDebloater.setOnClickListener { showDebloatOemPicker() }

        // System
        binding.btnSystemTweaks.setOnClickListener {
            launchIfShizuku(SystemTweaksActivity::class.java)
        }
        binding.btnBuildProp.setOnClickListener {
            launchIfShizuku(BuildPropActivity::class.java)
        }
        binding.btnPermissionManager.setOnClickListener {
            launchIfShizuku(PermissionManagerActivity::class.java)
        }

        // Device
        binding.btnBatteryStats.setOnClickListener {
            startActivity(Intent(this, BatteryStatsActivity::class.java))
        }
        binding.btnNetworkInfo.setOnClickListener {
            startActivity(Intent(this, NetworkInfoActivity::class.java))
        }
        binding.btnAppManager.setOnClickListener {
            launchIfShizuku(AppManagerActivity::class.java)
        }
        binding.btnProcessManager.setOnClickListener {
            launchIfShizuku(ProcessManagerActivity::class.java)
        }

        // Debug
        binding.btnStressTest.setOnClickListener {
            startActivity(Intent(this, StressTestActivity::class.java))
        }
        binding.btnLogcat.setOnClickListener {
            launchIfShizuku(LogcatActivity::class.java)
        }
        binding.btnTerminal.setOnClickListener {
            launchIfShizuku(ShellTerminalActivity::class.java)
        }

        // Extras
        binding.btnVolumeManager.setOnClickListener {
            startActivity(Intent(this, VolumeManagerActivity::class.java))
        }
        binding.btnSpotifyPlayer.setOnClickListener {
            startActivity(Intent(this, SpotifyPlayerActivity::class.java))
        }
        binding.btnWifiHotspot.setOnClickListener {
            launchIfShizuku(WifiHotspotActivity::class.java)
        }
        binding.btnAppLauncher.setOnClickListener {
            startActivity(Intent(this, AppLauncherActivity::class.java))
        }
        binding.btnIconCustomizer.setOnClickListener {
            startActivity(Intent(this, IconCustomizerActivity::class.java))
        }

        // Utility
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.btnBackupRestore.setOnClickListener {
            startActivity(Intent(this, BackupRestoreActivity::class.java))
        }
    }

    private fun launchIfShizuku(activity: Class<*>) {
        if (!ShizukuService.isAvailable()) {
            Toast.makeText(this, "Shizuku not connected", Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(Intent(this, activity))
    }

    private fun checkServiceStatus() {
        val shizukuOk = ShizukuService.isAvailable()
        binding.tvShizukuStatus.text = if (shizukuOk) "Shizuku: Connected" else "Shizuku: Not Connected"
        binding.tvShizukuStatus.setTextColor(ContextCompat.getColor(this, if (shizukuOk) R.color.green else R.color.red))

        if (!shizukuOk) requestShizukuPermission()

        try {
            DhizukuService.init(this)
            binding.tvDhizukuStatus.text = if (DhizukuService.isAvailable) "Dhizuku: Available" else "Dhizuku: Not Installed"
            binding.tvDhizukuStatus.setTextColor(ContextCompat.getColor(this, if (DhizukuService.isAvailable) R.color.green else R.color.orange))
            if (DhizukuService.isAvailable && !DhizukuService.isPermissionGranted) {
                DhizukuService.requestPermission { checkServiceStatus() }
            }
        } catch (e: Exception) {
            binding.tvDhizukuStatus.text = "Dhizuku: Error"
            binding.tvDhizukuStatus.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
    }

    private fun requestShizukuPermission() {
        if (Shizuku.isPreV11()) return
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(1001)
        }
    }

    private fun showDebloatOemPicker() {
        if (!ShizukuService.isAvailable()) {
            Toast.makeText(this, "Shizuku not connected", Toast.LENGTH_SHORT).show()
            return
        }
        val oemList = OEM.values().filter { it != OEM.ALL } + OEM.ALL
        AlertDialog.Builder(this)
            .setTitle("Select OEM")
            .setItems(oemList.map { it.displayName }.toTypedArray()) { _, which ->
                startActivity(Intent(this, PackageListActivity::class.java).apply {
                    putExtra("OEM", oemList[which].name)
                })
            }.show()
    }
}
