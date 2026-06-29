package com.anydebloat.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivitySystemTweaksBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SystemTweaksActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySystemTweaksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySystemTweaksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "System Tweaks"

        setupButtons()
    }

    private fun setupButtons() {
        binding.btnDisableAnimations.setOnClickListener {
            applyTweak("Disable Animations", listOf(
                "settings put global window_animation_scale 0",
                "settings put global transition_animation_scale 0",
                "settings put global animator_duration_scale 0"
            ))
        }

        binding.btnEnableAnimations.setOnClickListener {
            applyTweak("Enable Animations", listOf(
                "settings put global window_animation_scale 1",
                "settings put global transition_animation_scale 1",
                "settings put global animator_duration_scale 1"
            ))
        }

        binding.btnDisableToast.setOnClickListener {
            applyTweak("Disable Toast Notifications", listOf(
                "settings put global heads_up_notifications_enabled 0"
            ))
        }

        binding.btnEnableToast.setOnClickListener {
            applyTweak("Enable Toast Notifications", listOf(
                "settings put global heads_up_notifications_enabled 1"
            ))
        }

        binding.btnDpiChange.setOnClickListener {
            showDpiDialog()
        }

        binding.btnAutoRotate.setOnClickListener {
            applyTweak("Enable Auto Rotate", listOf(
                "settings put system accelerometer_rotation 1"
            ))
        }

        binding.btnImmersive.setOnClickListener {
            applyTweak("Enable Immersive Mode", listOf(
                "settings put global policy_control immersive.full=*"
            ))
        }

        binding.btnDisableImmersive.setOnClickListener {
            applyTweak("Disable Immersive Mode", listOf(
                "settings put global policy_control null"
            ))
        }
    }

    private fun applyTweak(name: String, commands: List<String>) {
        if (!ShizukuService.isAvailable()) {
            Toast.makeText(this, "Shizuku not connected", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            var success = true
            withContext(Dispatchers.IO) {
                for (cmd in commands) {
                    val result = ShizukuService.executeCommand(cmd)
                    if (!result.first) {
                        success = false
                    }
                }
            }

            val msg = if (success) "$name applied" else "Failed to apply $name"
            Toast.makeText(this@SystemTweaksActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDpiDialog() {
        val input = android.widget.EditText(this).apply {
            hint = "Enter DPI value (e.g. 420)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        AlertDialog.Builder(this)
            .setTitle("Change DPI")
            .setView(input)
            .setPositiveButton("Apply") { _, _ ->
                val dpi = input.text.toString()
                if (dpi.isNotEmpty()) {
                    applyTweak("DPI Change", listOf("wm density $dpi"))
                }
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Reset") { _, _ ->
                applyTweak("DPI Reset", listOf("wm density reset"))
            }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
