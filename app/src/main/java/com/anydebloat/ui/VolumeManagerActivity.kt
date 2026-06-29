package com.anydebloat.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivityVolumeManagerBinding
import com.anydebloat.service.VolumeManagerService
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VolumeManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVolumeManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVolumeManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Volume Manager"

        setupUI()
        updateStatus()
    }

    private fun setupUI() {
        binding.switchEnabled.setOnCheckedChangeListener { _, isChecked ->
            VolumeManagerService.isRunning.let {
                // Store preference
                getSharedPreferences("volume_manager_prefs", MODE_PRIVATE)
                    .edit().putBoolean("service_enabled", isChecked).apply()
            }
        }

        binding.btnVolumeUpAction.setOnClickListener {
            showActionPicker("volume_up_action", "Volume Up (2x)")
        }

        binding.btnVolumeDownAction.setOnClickListener {
            showActionPicker("volume_down_action", "Volume Down")
        }

        binding.btnSetShortcut1.setOnClickListener { showAppPicker(1) }
        binding.btnSetShortcut2.setOnClickListener { showAppPicker(2) }
        binding.btnSetShortcut3.setOnClickListener { showAppPicker(3) }
        binding.btnSetShortcut4.setOnClickListener { showAppPicker(4) }
        binding.btnSetShortcut5.setOnClickListener { showAppPicker(5) }
    }

    private fun updateStatus() {
        binding.tvStatus.text = if (VolumeManagerService.isRunning)
            "Service: Running" else "Service: Not running (enable in Settings > Accessibility)"
        binding.switchEnabled.isChecked = getSharedPreferences("volume_manager_prefs", MODE_PRIVATE)
            .getBoolean("service_enabled", false)
    }

    private fun showActionPicker(prefKey: String, label: String) {
        val actions = arrayOf("Open App Launcher", "App Shortcut 1-5", "Screenshot", "Lock Screen", "Recent Apps", "None")
        val actionValues = arrayOf("launcher", "app", "screenshot", "lock", "recent", "none")

        AlertDialog.Builder(this)
            .setTitle("$label Action")
            .setItems(actions) { _, which ->
                val value = actionValues[which]
                getSharedPreferences("volume_manager_prefs", MODE_PRIVATE)
                    .edit().putString(prefKey, value).apply()
                Toast.makeText(this, "Set to: ${actions[which]}", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showAppPicker(slot: Int) {
        val pm = packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
            .sortedBy { it.loadLabel(pm).toString() }

        val names = apps.map { it.loadLabel(pm).toString() }.toTypedArray()
        val packages = apps.map { it.packageName }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Select App for Shortcut $slot")
            .setItems(names) { _, which ->
                getSharedPreferences("volume_manager_prefs", MODE_PRIVATE)
                    .edit().putString("shortcut_app_$slot", packages[which]).apply()
                Toast.makeText(this, "Set to: ${names[which]}", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
