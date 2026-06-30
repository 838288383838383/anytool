package com.anydebloat.launcher

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.anydebloat.launcher.config.*
import java.io.File

class LauncherSettingsActivity : AppCompatActivity() {

    private lateinit var config: LauncherConfig
    private val configManager = LauncherConfigManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        config = configManager.loadConfig(filesDir)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
        }
        
        addSettingCategory(layout, "General Settings")
        addToggleSetting(layout, "Auto Rotate", config.general.autoRotate) { checked ->
            config = config.copy(general = config.general.copy(autoRotate = checked))
        }
        
        addSettingCategory(layout, "Grid Configuration")
        addSliderSetting(layout, "Columns: ${config.grid.columns}") { value ->
            config = config.copy(grid = config.grid.copy(columns = value))
        }
        addSliderSetting(layout, "Rows: ${config.grid.rows}") { value ->
            config = config.copy(grid = config.grid.copy(rows = value))
        }
        addSliderSetting(layout, "Icon Size: ${config.grid.iconSize}px") { value ->
            config = config.copy(grid = config.grid.copy(iconSize = value))
        }
        
        addSettingCategory(layout, "Appearance")
        addColorSelector(layout, "Theme", arrayOf("Dark", "Light", "Custom")) { theme ->
            config = config.copy(appearance = config.appearance.copy(theme = theme.lowercase()))
        }
        
        addSettingCategory(layout, "Gestures")
        addDropdownSetting(layout, "Swipe Up Action", 
            arrayOf("App Drawer", "Search", "Notifications", "None")) { action ->
            val actionMap = mapOf(
                "App Drawer" to "app_drawer",
                "Search" to "search",
                "Notifications" to "notifications",
                "None" to "none"
            )
            config = config.copy(
                gestures = config.gestures.copy(swipeUp = actionMap[action] ?: "app_drawer")
            )
        }
        
        addSettingCategory(layout, "Widgets")
        addToggleSetting(layout, "Enable Widgets", config.widgets.enabled) { checked ->
            config = config.copy(widgets = config.widgets.copy(enabled = checked))
        }
        addToggleSetting(layout, "Show Clock", config.widgets.showClock) { checked ->
            config = config.copy(widgets = config.widgets.copy(showClock = checked))
        }
        addToggleSetting(layout, "Show Weather", config.widgets.showWeather) { checked ->
            config = config.copy(widgets = config.widgets.copy(showWeather = checked))
        }
        addToggleSetting(layout, "Show System Stats", config.widgets.showSystemStats) { checked ->
            config = config.copy(widgets = config.widgets.copy(showSystemStats = checked))
        }
        
        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 32, 0, 0) }
        }
        
        val saveButton = Button(this).apply {
            text = "Save Configuration"
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply { setMargins(0, 0, 8, 0) }
            setOnClickListener { saveConfig() }
        }
        
        val resetButton = Button(this).apply {
            text = "Reset to Default"
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            setOnClickListener { resetConfig() }
        }
        
        buttonLayout.addView(saveButton)
        buttonLayout.addView(resetButton)
        layout.addView(buttonLayout)
        
        val scrollView = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            addView(layout)
        }
        
        setContentView(scrollView)
        supportActionBar?.title = "Launcher Settings"
    }

    private fun addSettingCategory(parent: LinearLayout, title: String) {
        val categoryView = TextView(this).apply {
            text = title
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 16, 0, 8)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        parent.addView(categoryView)
    }

    private fun addToggleSetting(parent: LinearLayout, label: String, defaultValue: Boolean, callback: (Boolean) -> Unit) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 8, 0, 8) }
        }
        
        val labelView = TextView(this).apply {
            text = label
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        
        val toggleView = Switch(this).apply {
            isChecked = defaultValue
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setOnCheckedChangeListener { _, isChecked -> callback(isChecked) }
        }
        
        layout.addView(labelView)
        layout.addView(toggleView)
        parent.addView(layout)
    }

    private fun addSliderSetting(parent: LinearLayout, label: String, callback: (Int) -> Unit) {
        val labelView = TextView(this).apply {
            text = label
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        val seekBar = SeekBar(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 8, 0, 16) }
            max = 100
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) callback(progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        
        parent.addView(labelView)
        parent.addView(seekBar)
    }

    private fun addColorSelector(parent: LinearLayout, label: String, options: Array<String>, callback: (String) -> Unit) {
        val labelView = TextView(this).apply {
            text = label
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 8, 0, 8) }
        }
        
        val button = Button(this).apply {
            text = "Select Theme"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                AlertDialog.Builder(this@LauncherSettingsActivity)
                    .setTitle("Choose Theme")
                    .setItems(options) { _, which -> callback(options[which]) }
                    .show()
            }
        }
        
        parent.addView(labelView)
        parent.addView(button)
    }

    private fun addDropdownSetting(parent: LinearLayout, label: String, options: Array<String>, callback: (String) -> Unit) {
        val labelView = TextView(this).apply {
            text = label
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 8, 0, 8) }
        }
        
        val spinner = Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 16) }
            adapter = ArrayAdapter(this@LauncherSettingsActivity, android.R.layout.simple_spinner_item, options)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    callback(options[position])
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        
        parent.addView(labelView)
        parent.addView(spinner)
    }

    private fun saveConfig() {
        configManager.saveConfig(config, filesDir)
        Toast.makeText(this, "Configuration saved!", Toast.LENGTH_SHORT).show()
    }

    private fun resetConfig() {
        AlertDialog.Builder(this)
            .setTitle("Reset Configuration?")
            .setMessage("This will reset all settings to default values.")
            .setPositiveButton("Reset") { _, _ ->
                config = LauncherConfig()
                saveConfig()
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
