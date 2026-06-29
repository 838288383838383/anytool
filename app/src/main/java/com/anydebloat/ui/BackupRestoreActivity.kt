package com.anydebloat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anydebloat.R
import com.anydebloat.adapter.BackupAdapter
import com.anydebloat.databinding.ActivityBackupRestoreBinding
import com.anydebloat.debloater.Debloater
import com.anydebloat.manager.AppManager
import com.anydebloat.models.PackageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BackupRestoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBackupRestoreBinding
    private lateinit var appManager: AppManager
    private lateinit var adapter: BackupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackupRestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appManager = AppManager.getInstance(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Backup & Restore"

        setupRecyclerView()
        loadBackups()
    }

    private fun setupRecyclerView() {
        adapter = BackupAdapter(
            onRestoreClick = { backup ->
                restoreBackup(backup)
            },
            onDeleteClick = { backup ->
                deleteBackup(backup)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun loadBackups() {
        val backups = appManager.getBackups()
        adapter.submitList(backups)

        if (backups.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    private fun restoreBackup(backup: AppManager.BackupEntry) {
        val packages = backup.packageNames.map { name ->
            PackageInfo(
                packageName = name,
                displayName = name,
                oem = com.anydebloat.models.OEM.ALL,
                category = "Restore"
            )
        }

        AlertDialog.Builder(this)
            .setTitle("Confirm Restore")
            .setMessage("Restore ${packages.size} packages from backup '${backup.label}'?")
            .setPositiveButton("Restore") { _, _ ->
                performRestore(packages)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performRestore(packages: List<PackageInfo>) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE

            var successCount = 0

            withContext(Dispatchers.IO) {
                packages.forEachIndexed { index, pkg ->
                    val result = Debloater.restorePackage(pkg.packageName)
                    if (result.success) successCount++

                    withContext(Dispatchers.Main) {
                        binding.progressBar.progress = index + 1
                        binding.progressBar.max = packages.size
                    }
                }
            }

            binding.progressBar.visibility = View.GONE

            Toast.makeText(
                this@BackupRestoreActivity,
                "Restored $successCount/${packages.size} packages",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun deleteBackup(backup: AppManager.BackupEntry) {
        AlertDialog.Builder(this)
            .setTitle("Delete Backup")
            .setMessage("Delete backup '${backup.label}'?")
            .setPositiveButton("Delete") { _, _ ->
                appManager.deleteBackup(backup.label)
                loadBackups()
                Toast.makeText(this, "Backup deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
