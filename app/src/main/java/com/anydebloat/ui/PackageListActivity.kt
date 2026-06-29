package com.anydebloat.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anydebloat.R
import com.anydebloat.adapter.PackageAdapter
import com.anydebloat.databinding.ActivityPackageListBinding
import com.anydebloat.debloater.Debloater
import com.anydebloat.manager.AppManager
import com.anydebloat.models.DebloatMode
import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo
import com.anydebloat.packagelists.PackageDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PackageListActivity : AppCompatActivity(), PackageAdapter.OnPackageClickListener {

    private lateinit var binding: ActivityPackageListBinding
    private lateinit var adapter: PackageAdapter
    private lateinit var appManager: AppManager
    private var allPackages = listOf<PackageInfo>()
    private var filteredPackages = listOf<PackageInfo>()
    private var currentOEM = OEM.ALL
    private var currentMode = DebloatMode.NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPackageListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appManager = AppManager.getInstance(this)
        currentMode = appManager.getSelectedMode()

        val oemName = intent.getStringExtra("OEM") ?: OEM.ALL.name
        currentOEM = try {
            OEM.valueOf(oemName)
        } catch (e: Exception) {
            OEM.ALL
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${currentOEM.displayName} Packages"

        setupRecyclerView()
        setupButtons()
        loadPackages()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_package_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_select_all -> {
                selectAllPackages()
                true
            }
            R.id.action_deselect_all -> {
                deselectAllPackages()
                true
            }
            R.id.action_refresh -> {
                loadPackages()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        adapter = PackageAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnDebloat.setOnClickListener {
            debloatSelectedPackages()
        }

        binding.btnRestore.setOnClickListener {
            restoreSelectedPackages()
        }

        // Mode selector
        binding.radioGroupMode.setOnCheckedChangeListener { _, checkedId ->
            currentMode = when (checkedId) {
                R.id.radioNormal -> DebloatMode.NORMAL
                R.id.radioZeroDay -> DebloatMode.ZERO_DAY
                R.id.radioBrute -> DebloatMode.BRUTE
                else -> DebloatMode.NORMAL
            }
            appManager.setSelectedMode(currentMode)
        }

        // Set initial mode
        when (currentMode) {
            DebloatMode.NORMAL -> binding.radioNormal.isChecked = true
            DebloatMode.ZERO_DAY -> binding.radioZeroDay.isChecked = true
            DebloatMode.BRUTE -> binding.radioBrute.isChecked = true
        }

        // Search
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPackages(newText)
                return true
            }
        })
    }

    private fun loadPackages() {
        allPackages = PackageDatabase.getPackagesByOEM(currentOEM)
        updatePackageStates()
        filteredPackages = allPackages
        adapter.submitList(filteredPackages)
        binding.tvPackageCount.text = "${filteredPackages.size} packages"
    }

    private fun updatePackageStates() {
        allPackages.forEach { pkg ->
            try {
                val pm = packageManager
                val appInfo = pm.getApplicationInfo(pkg.packageName, 0)
                pkg.isInstalled = true
                pkg.isEnabled = pm.getApplicationEnabledSetting(pkg.packageName) !=
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
            } catch (e: PackageManager.NameNotFoundException) {
                pkg.isInstalled = false
            }
        }
    }

    private fun filterPackages(query: String?) {
        filteredPackages = if (query.isNullOrBlank()) {
            allPackages
        } else {
            allPackages.filter {
                it.packageName.contains(query, ignoreCase = true) ||
                    it.displayName.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
            }
        }
        adapter.submitList(filteredPackages)
        binding.tvPackageCount.text = "${filteredPackages.size} packages"
    }

    private fun selectAllPackages() {
        filteredPackages.forEach { it.isSelected = it.isInstalled && it.isEnabled }
        adapter.submitList(filteredPackages.toList())
        updateSelectionCount()
    }

    private fun deselectAllPackages() {
        filteredPackages.forEach { it.isSelected = false }
        adapter.submitList(filteredPackages.toList())
        updateSelectionCount()
    }

    private fun updateSelectionCount() {
        val selected = filteredPackages.count { it.isSelected }
        binding.tvSelectionCount.text = "$selected selected"
    }

    private fun debloatSelectedPackages() {
        val selected = filteredPackages.filter { it.isSelected }

        if (selected.isEmpty()) {
            Toast.makeText(this, "No packages selected", Toast.LENGTH_SHORT).show()
            return
        }

        val message = "Remove ${selected.size} packages using ${currentMode.displayName} mode?"

        AlertDialog.Builder(this)
            .setTitle("Confirm Debloat")
            .setMessage(message)
            .setPositiveButton("Debloat") { _, _ ->
                performDebloat(selected)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performDebloat(packages: List<PackageInfo>) {
        lifecycleScope.launch {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.btnDebloat.isEnabled = false
            binding.btnRestore.isEnabled = false

            var successCount = 0
            var failCount = 0

            withContext(Dispatchers.IO) {
                Debloater.debloatMultiple(packages, currentMode) { progress, total, result ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.progressBar.progress = progress
                        binding.progressBar.max = total

                        if (result.success) successCount++ else failCount++

                        appManager.addToHistory(
                            result.packageName,
                            result.success,
                            result.method.name
                        )

                        binding.tvStatus.text = "Progress: $progress/$total"
                    }
                }
            }

            binding.progressBar.visibility = android.view.View.GONE
            binding.btnDebloat.isEnabled = true
            binding.btnRestore.isEnabled = true

            // Update package states
            updatePackageStates()
            adapter.submitList(filteredPackages.toList())

            AlertDialog.Builder(this@PackageListActivity)
                .setTitle("Debloat Complete")
                .setMessage("Success: $successCount\nFailed: $failCount\nTotal: ${packages.size}")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun restoreSelectedPackages() {
        val selected = filteredPackages.filter { it.isSelected }

        if (selected.isEmpty()) {
            Toast.makeText(this, "No packages selected", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Confirm Restore")
            .setMessage("Restore ${selected.size} packages?")
            .setPositiveButton("Restore") { _, _ ->
                performRestore(selected)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performRestore(packages: List<PackageInfo>) {
        lifecycleScope.launch {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.btnDebloat.isEnabled = false
            binding.btnRestore.isEnabled = false

            var successCount = 0

            withContext(Dispatchers.IO) {
                packages.forEachIndexed { index, pkg ->
                    val result = Debloater.restorePackage(pkg.packageName)
                    if (result.success) successCount++

                    withContext(Dispatchers.Main) {
                        binding.progressBar.progress = index + 1
                        binding.progressBar.max = packages.size
                        binding.tvStatus.text = "Restoring: ${index + 1}/${packages.size}"
                    }
                }
            }

            binding.progressBar.visibility = android.view.View.GONE
            binding.btnDebloat.isEnabled = true
            binding.btnRestore.isEnabled = true

            updatePackageStates()
            adapter.submitList(filteredPackages.toList())

            Toast.makeText(
                this@PackageListActivity,
                "Restored $successCount/${packages.size} packages",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onPackageClick(position: Int, packageInfo: PackageInfo) {
        packageInfo.isSelected = !packageInfo.isSelected
        adapter.notifyItemChanged(position)
        updateSelectionCount()
    }

    override fun onPackageLongClick(position: Int, packageInfo: PackageInfo) {
        AlertDialog.Builder(this)
            .setTitle(packageInfo.displayName)
            .setMessage(
                "Package: ${packageInfo.packageName}\n" +
                    "Category: ${packageInfo.category}\n" +
                    "Installed: ${if (packageInfo.isInstalled) "Yes" else "No"}\n" +
                    "Enabled: ${if (packageInfo.isEnabled) "Yes" else "No"}"
            )
            .setPositiveButton("OK", null)
            .show()
    }
}
