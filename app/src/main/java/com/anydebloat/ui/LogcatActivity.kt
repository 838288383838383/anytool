package com.anydebloat.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivityLogcatBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogcatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogcatBinding
    private var logJob: Job? = null
    private var isStreaming = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogcatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Logcat Viewer"

        binding.btnStart.setOnClickListener { startStreaming() }
        binding.btnStop.setOnClickListener { stopStreaming() }
        binding.btnClear.setOnClickListener { binding.tvLogcat.text = "" }
        binding.btnFilter.setOnClickListener { showFilterDialog() }
    }

    private fun startStreaming() {
        isStreaming = true
        binding.btnStart.isEnabled = false
        binding.btnStop.isEnabled = true

        logJob = lifecycleScope.launch {
            while (isActive && isStreaming) {
                val result = withContext(Dispatchers.IO) {
                    ShizukuService.executeCommand("logcat -d -t 50 *:V")
                }
                if (result.first && result.second.isNotEmpty()) {
                    val current = binding.tvLogcat.text.toString()
                    if (current.length > 50000) {
                        binding.tvLogcat.text = result.second
                    } else {
                        binding.tvLogcat.append(result.second + "\n")
                    }
                }
                delay(2000)
            }
        }
    }

    private fun stopStreaming() {
        isStreaming = false
        logJob?.cancel()
        binding.btnStart.isEnabled = true
        binding.btnStop.isEnabled = false
    }

    private fun showFilterDialog() {
        val input = android.widget.EditText(this).apply {
            hint = "Log tag or text filter"
        }
        AlertDialog.Builder(this)
            .setTitle("Filter Logs")
            .setView(input)
            .setPositiveButton("Apply") { _, _ ->
                val filter = input.text.toString()
                if (filter.isNotEmpty()) {
                    applyFilter(filter)
                }
            }
            .setNegativeButton("Cancel", null).show()
    }

    private fun applyFilter(filter: String) {
        stopStreaming()
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                ShizukuService.executeCommand("logcat -d -s $filter")
            }
            binding.tvLogcat.text = if (result.first) result.second else "Error"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopStreaming()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
