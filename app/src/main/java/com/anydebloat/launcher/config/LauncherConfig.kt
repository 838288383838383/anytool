package com.anydebloat.launcher.config

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.File

data class LauncherConfig(
    @SerializedName("general")
    val general: GeneralConfig = GeneralConfig(),
    
    @SerializedName("grid")
    val grid: GridConfig = GridConfig(),
    
    @SerializedName("appearance")
    val appearance: AppearanceConfig = AppearanceConfig(),
    
    @SerializedName("gestures")
    val gestures: GesturesConfig = GesturesConfig(),
    
    @SerializedName("widgets")
    val widgets: WidgetsConfig = WidgetsConfig()
)

data class GeneralConfig(
    @SerializedName("launcher_name")
    val launcherName: String = "AnyTool Launcher",
    
    @SerializedName("show_status_bar")
    val showStatusBar: Boolean = true,
    
    @SerializedName("app_drawer_style")
    val appDrawerStyle: String = "grid",
    
    @SerializedName("default_page")
    val defaultPage: Int = 0,
    
    @SerializedName("auto_rotate")
    val autoRotate: Boolean = false
)

data class GridConfig(
    @SerializedName("columns")
    val columns: Int = 4,
    
    @SerializedName("rows")
    val rows: Int = 6,
    
    @SerializedName("icon_size")
    val iconSize: Int = 64,
    
    @SerializedName("spacing")
    val spacing: Int = 12,
    
    @SerializedName("allow_scrolling")
    val allowScrolling: Boolean = true,
    
    @SerializedName("pages_enabled")
    val pagesEnabled: Boolean = true
)

data class AppearanceConfig(
    @SerializedName("theme")
    val theme: String = "dark",
    
    @SerializedName("primary_color")
    val primaryColor: String = "#2196F3",
    
    @SerializedName("secondary_color")
    val secondaryColor: String = "#1976D2",
    
    @SerializedName("accent_color")
    val accentColor: String = "#FF5722",
    
    @SerializedName("background_image")
    val backgroundImage: String = "",
    
    @SerializedName("icon_pack")
    val iconPack: String = "default",
    
    @SerializedName("transparency")
    val transparency: Float = 0.95f,
    
    @SerializedName("font_size")
    val fontSize: Int = 12
)

data class GesturesConfig(
    @SerializedName("swipe_up")
    val swipeUp: String = "app_drawer",
    
    @SerializedName("swipe_down")
    val swipeDown: String = "notifications",
    
    @SerializedName("long_press")
    val longPress: String = "app_menu",
    
    @SerializedName("double_tap")
    val doubleTap: String = "lock_screen",
    
    @SerializedName("pinch")
    val pinch: String = "zoom",
    
    @SerializedName("haptic_feedback")
    val hapticFeedback: Boolean = true
)

data class WidgetsConfig(
    @SerializedName("enabled")
    val enabled: Boolean = true,
    
    @SerializedName("widget_size")
    val widgetSize: Int = 128,
    
    @SerializedName("show_clock")
    val showClock: Boolean = true,
    
    @SerializedName("show_weather")
    val showWeather: Boolean = true,
    
    @SerializedName("show_music")
    val showMusic: Boolean = true,
    
    @SerializedName("show_system_stats")
    val showSystemStats: Boolean = true,
    
    @SerializedName("update_interval")
    val updateInterval: Long = 5000
)

object LauncherConfigManager {
    
    private const val CONFIG_FILE = "launcher_config.json"
    private val gson = Gson()
    
    fun loadConfig(configDir: File): LauncherConfig {
        return try {
            val configFile = File(configDir, CONFIG_FILE)
            if (configFile.exists()) {
                gson.fromJson(configFile.readText(), LauncherConfig::class.java)
            } else {
                LauncherConfig()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LauncherConfig()
        }
    }
    
    fun saveConfig(config: LauncherConfig, configDir: File) {
        try {
            val configFile = File(configDir, CONFIG_FILE)
            configFile.writeText(gson.toJson(config))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun getDefaultConfig(): LauncherConfig {
        return LauncherConfig()
    }
    
    fun getConfigAsJson(config: LauncherConfig): String {
        return gson.toJson(config)
    }
}
