package com.anydebloat.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anydebloat.R
import com.anydebloat.databinding.ActivitySettingsBinding
import com.anydebloat.manager.AppManager
import com.anydebloat.models.DebloatMode
import com.anydebloat.models.OEM

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var appManager: AppManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appManager = AppManager.getInstance(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        loadSettings()
        setupListeners()
    }

    private fun loadSettings() {
        // Load default OEM
        val defaultOEM = appManager.getSelectedOEM()
        when (defaultOEM) {
            OEM.SAMSUNG -> binding.radioSamsung.isChecked = true
            OEM.XIAOMI -> binding.radioXiaomi.isChecked = true
            OEM.GENERIC -> binding.radioGeneric.isChecked = true
            OEM.ALL -> binding.radioAll.isChecked = true
        }

        // Load default mode
        val defaultMode = appManager.getSelectedMode()
        when (defaultMode) {
            DebloatMode.NORMAL -> binding.radioNormal.isChecked = true
            DebloatMode.ZERO_DAY -> binding.radioZeroDay.isChecked = true
            DebloatMode.BRUTE -> binding.radioBrute.isChecked = true
        }

        // Load history count
        val historyCount = appManager.getHistory().size
        binding.tvHistoryCount.text = "$historyCount operations logged"
    }

    private fun setupListeners() {
        binding.radioGroupOEM.setOnCheckedChangeListener { _, checkedId ->
            val oem = when (checkedId) {
                R.id.radioSamsung -> OEM.SAMSUNG
                R.id.radioXiaomi -> OEM.XIAOMI
                R.id.radioGeneric -> OEM.GENERIC
                R.id.radioAll -> OEM.ALL
                else -> OEM.ALL
            }
            appManager.setSelectedOEM(oem)
        }

        binding.radioGroupMode.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.radioNormal -> DebloatMode.NORMAL
                R.id.radioZeroDay -> DebloatMode.ZERO_DAY
                R.id.radioBrute -> DebloatMode.BRUTE
                else -> DebloatMode.NORMAL
            }
            appManager.setSelectedMode(mode)
        }

        binding.btnClearHistory.setOnClickListener {
            appManager.clearHistory()
            binding.tvHistoryCount.text = "0 operations logged"
            Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
