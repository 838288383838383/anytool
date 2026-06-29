package com.anydebloat.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivityProcessManagerBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProcessManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProcessManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProcessManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Process Manager"

        loadProcesses()

        binding.btnRefresh.setOnClickListener { loadProcesses() }
        binding.btnKillSelected.setOnClickListener { killSelectedProcess() }
    }

    private fun loadProcesses() {
        lifecycleScope.launch {
            binding.progressBar.visibility = android.view.View.VISIBLE
            val result = withContext(Dispatchers.IO) {
                ShizukuService.executeCommand("ps -A -o PID,USER,NAME,%CPU,%MEM")
            }
            binding.progressBar.visibility = android.view.View.GONE
            if (result.first) {
                binding.tvProcesses.text = result.second
            } else {
                binding.tvProcesses.text = "Error: ${result.second}"
            }
        }
    }

    private fun killSelectedProcess() {
        val input = android.widget.EditText(this).apply {
            hint = "Package or PID to kill"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
        }
        AlertDialog.Builder(this)
            .setTitle("Kill Process")
            .setView(input)
            .setPositiveButton("Kill") { _, _ ->
                val target = input.text.toString().trim()
                if (target.isNotEmpty()) {
                    lifecycleScope.launch {
                        val result = withContext(Dispatchers.IO) {
                            ShizukuService.forceStopPackage(target)
                        }
                        Toast.makeText(this@ProcessManagerActivity,
                            if (result.first) "Process killed" else "Failed",
                            Toast.LENGTH_SHORT).show()
                        loadProcesses()
                    }
                }
            }
            .setNegativeButton("Cancel", null).show()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
