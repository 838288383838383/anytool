package com.anydebloat.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivityStressTestBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.RandomAccessFile
import kotlin.random.Random

class StressTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStressTestBinding
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var cpuThreads = mutableListOf<Thread>()
    private var ramAllocations = mutableListOf<ByteArray>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStressTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Stress Test"

        setupButtons()
    }

    private fun setupButtons() {
        binding.btnCpuStress.setOnClickListener {
            showCpuDialog()
        }

        binding.btnRamStress.setOnClickListener {
            showRamDialog()
        }

        binding.btnStopAll.setOnClickListener {
            stopAllTests()
        }

        binding.btnDeviceInfo.setOnClickListener {
            showDeviceInfo()
        }
    }

    private fun showCpuDialog() {
        val items = arrayOf("1 Thread", "2 Threads", "4 Threads", "8 Threads")
        AlertDialog.Builder(this)
            .setTitle("CPU Stress Test")
            .setItems(items) { _, which ->
                val threads = intArrayOf(1, 2, 4, 8)[which]
                startCpuStress(threads)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startCpuStress(threadCount: Int) {
        isRunning = true
        binding.tvStatus.text = "CPU stress: $threadCount threads running..."
        binding.tvStatus.setTextColor(getColor(android.R.color.holo_green_light))

        for (i in 0 until threadCount) {
            val thread = Thread {
                var x = 0.0
                while (isRunning) {
                    x += Math.sin(Random.nextDouble()) * Math.cos(Random.nextDouble())
                }
            }
            cpuThreads.add(thread)
            thread.start()
        }
    }

    private fun showRamDialog() {
        val items = arrayOf("100 MB", "256 MB", "512 MB", "1 GB")
        AlertDialog.Builder(this)
            .setTitle("RAM Stress Test")
            .setItems(items) { _, which ->
                val mb = intArrayOf(100, 256, 512, 1024)[which]
                startRamStress(mb)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startRamStress(mb: Int) {
        isRunning = true
        binding.tvStatus.text = "RAM stress: allocating ${mb}MB..."
        binding.tvStatus.setTextColor(getColor(android.R.color.holo_green_light))

        try {
            val bytes = ByteArray(mb * 1024 * 1024)
            Random.nextBytes(bytes)
            ramAllocations.add(bytes)
            binding.tvStatus.text = "RAM stress: ${mb}MB allocated"
        } catch (e: OutOfMemoryError) {
            binding.tvStatus.text = "Out of memory at ${mb}MB"
            binding.tvStatus.setTextColor(getColor(android.R.color.holo_red_light))
        }
    }

    private fun stopAllTests() {
        isRunning = false
        cpuThreads.forEach { it.interrupt() }
        cpuThreads.clear()
        ramAllocations.clear()
        System.gc()

        binding.tvStatus.text = "All tests stopped"
        binding.tvStatus.setTextColor(getColor(android.R.color.holo_green_light))
    }

    private fun showDeviceInfo() {
        val info = buildString {
            appendLine("CPU Cores: ${Runtime.getRuntime().availableProcessors()}")
            appendLine("Max Memory: ${Runtime.getRuntime().maxMemory() / 1024 / 1024}MB")
            appendLine("Total Memory: ${Runtime.getRuntime().totalMemory() / 1024 / 1024}MB")
            appendLine("Free Memory: ${Runtime.getRuntime().freeMemory() / 1024 / 1024}MB")
            appendLine()

            try {
                val reader = RandomAccessFile("/proc/cpuinfo", "r")
                val content = reader.readLine()
                appendLine("CPU: $content")
                reader.close()
            } catch (e: Exception) {
                appendLine("CPU: Unknown")
            }

            try {
                val reader = RandomAccessFile("/proc/meminfo", "r")
                val line = reader.readLine()
                appendLine("RAM Total: ${line?.split(":")?.get(1)?.trim()}")
                reader.close()
            } catch (e: Exception) {
                appendLine("RAM: Unknown")
            }

            appendLine()
            appendLine("Android: ${android.os.Build.VERSION.RELEASE}")
            appendLine("SDK: ${android.os.Build.VERSION.SDK_INT}")
            appendLine("Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
        }

        AlertDialog.Builder(this)
            .setTitle("Device Info")
            .setMessage(info)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAllTests()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
