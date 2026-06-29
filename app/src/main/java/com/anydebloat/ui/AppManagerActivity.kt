package com.anydebloat.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anydebloat.adapter.AppListAdapter
import com.anydebloat.databinding.ActivityAppManagerBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppManagerBinding
    private lateinit var adapter: AppListAdapter
    private var allApps = mutableListOf<AppInfo>()

    data class AppInfo(
        val packageName: String,
        val name: String,
        val version: String,
        val size: String,
        val isSystem: Boolean
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "App Manager"

        adapter = AppListAdapter { pkg, action -> handleAppAction(pkg, action) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadApps()
    }

    private fun loadApps() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            allApps = withContext(Dispatchers.IO) {
                val pm = packageManager
                val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
                packages.map { pkg ->
                    val appInfo = pkg.applicationInfo
                    val size = try {
                        val sizeFile = java.io.File(appInfo.sourceDir)
                        formatSize(sizeFile.length())
                    } catch (e: Exception) { "N/A" }

                    AppInfo(
                        packageName = pkg.packageName,
                        name = appInfo.loadLabel(pm).toString(),
                        version = pkg.versionName ?: "N/A",
                        size = size,
                        isSystem = (appInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
                    )
                }.sortedBy { it.name }
            }.toMutableList()

            binding.progressBar.visibility = View.GONE
            adapter.submitList(allApps)
        }
    }

    private fun handleAppAction(packageName: String, action: String) {
        when (action) {
            "disable" -> {
                AlertDialog.Builder(this)
                    .setTitle("Disable App")
                    .setMessage("Disable $packageName?")
                    .setPositiveButton("Disable") { _, _ ->
                        lifecycleScope.launch {
                            val result = withContext(Dispatchers.IO) {
                                ShizukuService.disablePackage(packageName)
                            }
                            Toast.makeText(this@AppManagerActivity,
                                if (result.first) "Disabled" else "Failed: ${result.second}",
                                Toast.LENGTH_SHORT).show()
                            loadApps()
                        }
                    }
                    .setNegativeButton("Cancel", null).show()
            }
            "enable" -> {
                lifecycleScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        ShizukuService.enablePackage(packageName)
                    }
                    Toast.makeText(this@AppManagerActivity,
                        if (result.first) "Enabled" else "Failed",
                        Toast.LENGTH_SHORT).show()
                    loadApps()
                }
            }
            "force_stop" -> {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        ShizukuService.forceStopPackage(packageName)
                    }
                    Toast.makeText(this@AppManagerActivity, "Force stopped", Toast.LENGTH_SHORT).show()
                }
            }
            "clear_data" -> {
                AlertDialog.Builder(this)
                    .setTitle("Clear Data")
                    .setMessage("Clear all data for $packageName?")
                    .setPositiveButton("Clear") { _, _ ->
                        lifecycleScope.launch {
                            val result = withContext(Dispatchers.IO) {
                                ShizukuService.clearPackageData(packageName)
                            }
                            Toast.makeText(this@AppManagerActivity,
                                if (result.first) "Data cleared" else "Failed",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancel", null).show()
            }
        }
    }

    private fun formatSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1048576 -> "${bytes / 1024} KB"
            bytes < 1073741824 -> "${bytes / 1048576} MB"
            else -> "${bytes / 1073741824} GB"
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
