package com.anydebloat.ui

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivityShellTerminalBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class ShellTerminalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShellTerminalBinding
    private val commandHistory = mutableListOf<String>()
    private var currentMode = ShellMode.ADB_SHIZUKU
    private var currentDistro: DistroInfo? = null
    private var rishPath: String? = null

    enum class ShellMode(val displayName: String) {
        ADB_SHIZUKU("ADB (Shizuku)"),
        RISH("rish (Shizuku)"),
        LINUX_SANDBOX("Linux Sandbox (proot)")
    }

    data class DistroInfo(
        val id: String,
        val name: String,
        val icon: String,
        val color: String,
        val description: String,
        val termuxPackage: String,
    )

    private val distros = listOf(
        DistroInfo("debian", "Debian", "Debian", "#A80030", "Stable, reliable, huge package repo", "debian"),
        DistroInfo("ubuntu", "Ubuntu", "Ubuntu", "#E95420", "Most popular Linux desktop distro", "ubuntu"),
        DistroInfo("arch", "Arch Linux", "Arch", "#1793D1", "Bleeding-edge, rolling release, DIY", "archlinux"),
        DistroInfo("gentoo", "Gentoo", "Gentoo", "#54487A", "Compile everything from source", "gentoo"),
        DistroInfo("opensuse", "openSUSE", "openSUSE", "#73BA25", "YaST, Btrfs snapshots, Leap/Tumbleweed", "opensuse"),
        DistroInfo("nixos", "NixOS", "NixOS", "#7EBAE4", "Declarative config, reproducible builds", "nixos"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShellTerminalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Shell Terminal"

        rishPath = findRish()
        setupModeSelector()
        printHeader()

        binding.btnRun.setOnClickListener {
            val cmd = binding.etCommand.text.toString().trim()
            if (cmd.isNotEmpty()) {
                executeCommand(cmd)
                commandHistory.add(cmd)
                binding.etCommand.text?.clear()
            }
        }

        binding.btnClear.setOnClickListener { binding.tvOutput.text = "" }
        binding.btnDistroPicker.setOnClickListener { showDistroPickerDialog() }
    }

    private fun setupModeSelector() {
        val modes = ShellMode.values().map { it.displayName }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, modes)
        binding.spinnerMode.adapter = adapter
        binding.spinnerMode.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                currentMode = ShellMode.values()[position]
                binding.btnDistroPicker.visibility = if (currentMode == ShellMode.LINUX_SANDBOX) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }
                printHeader()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        if (rishPath != null) {
            binding.spinnerMode.setSelection(1)
        }
    }

    private fun showDistroPickerDialog() {
        val names = distros.map { "${it.icon}  ${it.name}  —  ${it.description}" }.toTypedArray()
        val checkedIndex = currentDistro?.let { d -> distros.indexOfFirst { it.id == d.id } } ?: 0

        AlertDialog.Builder(this)
            .setTitle("Select Linux Distribution")
            .setSingleChoiceItems(names, checkedIndex) { dialog, which ->
                currentDistro = distros[which]
                dialog.dismiss()
                printHeader()
                Toast.makeText(this, "Selected: ${currentDistro!!.name}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun printHeader() {
        val header = buildString {
            appendLine("AnyTool Terminal v1.0")
            appendLine("Mode: ${currentMode.displayName}")
            when (currentMode) {
                ShellMode.ADB_SHIZUKU -> {
                    appendLine("Shell: ADB via Shizuku")
                    appendLine("Status: ${if (ShizukuService.isAvailable()) "Connected" else "Disconnected"}")
                }
                ShellMode.RISH -> {
                    if (rishPath != null) {
                        appendLine("rish: $rishPath")
                        appendLine("Status: Found")
                    } else {
                        appendLine("rish: NOT FOUND")
                        appendLine("Install rish in Termux or copy to /sdcard/")
                    }
                }
                ShellMode.LINUX_SANDBOX -> {
                    val distro = currentDistro
                    if (distro != null) {
                        appendLine("Distro: ${distro.name} (${distro.id})")
                        appendLine("proot-distro: ${if (isTermuxInstalled()) "Termux installed" else "Termux NOT found"}")
                        appendLine("proot-distro: ${if (isProotDistroInstalled()) "installed" else "NOT installed — run: pkg install proot-distro"}")
                    } else {
                        appendLine("Distro: NONE SELECTED — tap the distro picker button")
                    }
                }
            }
            appendLine("---")
        }
        binding.tvOutput.text = header
    }

    private fun isTermuxInstalled(): Boolean {
        return try {
            packageManager.getPackageInfo("com.termux", 0)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun isProotDistroInstalled(): Boolean {
        if (!isTermuxInstalled()) return false
        return try {
            val result = executeCommandViaTermux("which proot-distro")
            result.contains("proot-distro")
        } catch (_: Exception) {
            false
        }
    }

    private fun findRish(): String? {
        val searchPaths = listOf(
            "/data/data/com.termux/files/usr/bin/rish",
            "/data/data/com.termux/files/home/.rish/rish",
            "/data/data/com.termux/files/home/bin/rish",
            File(Environment.getExternalStorageDirectory(), "rish").absolutePath,
            File(Environment.getExternalStorageDirectory(), "Download/rish").absolutePath,
            File(Environment.getExternalStorageDirectory(), "Android/data/com.termux/files/home/rish").absolutePath,
            "/sdcard/rish",
            "/sdcard/Download/rish",
            "/system/bin/rish",
        )

        for (path in searchPaths) {
            val file = File(path)
            if (file.exists() && file.canExecute()) return path
        }

        val sdcard = Environment.getExternalStorageDirectory()
        sdcard.listFiles()?.forEach { f ->
            if (f.name == "rish" && f.canExecute()) return f.absolutePath
        }
        File(sdcard, "Download").listFiles()?.forEach { f ->
            if (f.name == "rish" && f.canExecute()) return f.absolutePath
        }

        return null
    }

    private fun executeCommand(command: String) {
        val prompt = when (currentMode) {
            ShellMode.ADB_SHIZUKU -> "$ "
            ShellMode.RISH -> if (rishPath != null) "rish> " else "> "
            ShellMode.LINUX_SANDBOX -> {
                val d = currentDistro
                if (d != null) "${d.id}> " else "linux> "
            }
        }
        binding.tvOutput.append("$prompt$command\n")

        when (currentMode) {
            ShellMode.RISH -> {
                if (rishPath == null) {
                    binding.tvOutput.append("Error: rish not found. Use ADB mode or install rish.\n\n")
                    return
                }
            }
            ShellMode.LINUX_SANDBOX -> {
                if (currentDistro == null) {
                    binding.tvOutput.append("Error: No distro selected. Use the distro picker button.\n\n")
                    return
                }
            }
            else -> {}
        }

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                when (currentMode) {
                    ShellMode.ADB_SHIZUKU -> ShizukuService.executeCommand(command)
                    ShellMode.RISH -> ShizukuService.executeCommand("$rishPath -c '$command' 2>&1")
                    ShellMode.LINUX_SANDBOX -> executeLinuxCommand(command)
                }
            }
            val output = if (result.first) result.second else "Error: ${result.second}"
            binding.tvOutput.append("$output\n\n")

            binding.scrollOutput.post {
                binding.scrollOutput.scrollTo(0, binding.tvOutput.height)
            }
        }
    }

    private fun executeLinuxCommand(command: String): Pair<Boolean, String> {
        val distro = currentDistro ?: return Pair(false, "No distro selected")

        if (!isTermuxInstalled()) {
            return Pair(false, "Termux not installed.\nInstall Termux from F-Droid or GitHub:\nhttps://github.com/termux/termux-app/releases")
        }

        val prootAvailable = isProotDistroInstalled()

        return if (prootAvailable) {
            // proot-distro is installed — use it
            val prootCmd = "proot-distro login ${distro.id} -- sh -c '${command.replace("'", "'\\''")}' 2>&1"
            executeCommandViaTermux(prootCmd)
            Pair(true, executeCommandViaTermuxRaw(prootCmd))
        } else {
            // proot-distro not installed — try to install it or give instructions
            val installResult = executeCommandViaTermux("pkg install -y proot-distro 2>&1")
            if (installResult.contains("done") || installResult.contains("already installed")) {
                val prootCmd = "proot-distro login ${distro.id} -- sh -c '${command.replace("'", "'\\''")}' 2>&1"
                Pair(true, executeCommandViaTermuxRaw(prootCmd))
            } else {
                Pair(false, buildString {
                    appendLine("proot-distro not available.")
                    appendLine("")
                    appendLine("To set up Linux sandbox manually:")
                    appendLine("1. Install Termux from F-Droid")
                    appendLine("2. Open Termux and run:")
                    appendLine("   pkg update && pkg install proot-distro")
                    appendLine("3. Install your distro:")
                    appendLine("   proot-distro install ${distro.id}")
                    appendLine("4. Login:")
                    appendLine("   proot-distro login ${distro.id}")
                    appendLine("")
                    appendLine("Auto-install output: $installResult")
                })
            }
        }
    }

    private fun executeCommandViaTermux(command: String): String {
        return try {
            val intent = Intent().apply {
                component = ComponentName("com.termux", "com.termux.app.RunCommandService")
                action = "com.termux.RUN_COMMAND"
                setClassName("com.termux", "com.termux.app.RunCommandService")
                putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/sh")
                putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf("-c", command))
                putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startService(intent)
            "Command sent to Termux: $command"
        } catch (e: Exception) {
            "Failed to run in Termux: ${e.message}\nMake sure Termux is installed."
        }
    }

    private fun executeCommandViaTermuxRaw(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf(
                "su", "-c",
                "am start-foreground-service -n com.termux/.app.RunCommandService " +
                "-a com.termux.RUN_COMMAND " +
                "--es com.termux.RUN_COMMAND_PATH '/data/data/com.termux/files/usr/bin/sh' " +
                "--esa com.termux.RUN_COMMAND_ARGUMENTS '-c,$command' " +
                "--es com.termux.RUN_COMMAND_WORKDIR '/data/data/com.termux/files/home'"
            ))
            val stdout = BufferedReader(InputStreamReader(process.inputStream)).readText()
            val stderr = BufferedReader(InputStreamReader(process.errorStream)).readText()
            val exitCode = process.waitFor()

            if (exitCode == 0 && stdout.isNotBlank()) stdout.trim()
            else if (stderr.isNotBlank()) stderr.trim()
            else "Termux command sent (exit=$exitCode). Check Termux for output."
        } catch (e: Exception) {
            // Fallback: try direct intent
            try {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("pkg://com.termux")
                    putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/sh")
                    putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf("-c", command))
                }
                "Sent to Termux via intent. Check Termux app for output."
            } catch (e2: Exception) {
                "Cannot execute in Termux: ${e.message}"
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
