package com.anydebloat.launcher.widgets

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import java.util.*

sealed class LauncherWidget(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    abstract fun updateWidget()
    abstract fun getWidgetType(): String
}

class ClockWidget(context: Context, attrs: AttributeSet? = null) : LauncherWidget(context, attrs) {
    
    private lateinit var timeView: TextView
    private lateinit var dateView: TextView
    
    init {
        LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, this)
        timeView = findViewById(android.R.id.text1)
        setupViews()
    }
    
    private fun setupViews() {
        timeView.textSize = 24f
    }
    
    override fun updateWidget() {
        val calendar = Calendar.getInstance()
        val time = DateFormat.getTimeFormat(context).format(calendar.time)
        val date = DateFormat.getDateFormat(context).format(calendar.time)
        
        timeView.text = "$time\n$date"
        
        postDelayed({ updateWidget() }, 1000)
    }
    
    override fun getWidgetType(): String = "clock"
}

class WeatherWidget(context: Context, attrs: AttributeSet? = null) : LauncherWidget(context, attrs) {
    
    private lateinit var tempView: TextView
    
    init {
        LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, this)
        tempView = findViewById(android.R.id.text1)
        tempView.text = "Weather: --°C"
    }
    
    override fun updateWidget() {
        try {
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val temp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
            tempView.text = "🌡️ ${temp / 10}°C"
        } catch (e: Exception) {
            tempView.text = "Weather unavailable"
        }
    }
    
    override fun getWidgetType(): String = "weather"
}

class SystemStatsWidget(context: Context, attrs: AttributeSet? = null) : LauncherWidget(context, attrs) {
    
    private lateinit var statsView: TextView
    
    init {
        LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, this)
        statsView = findViewById(android.R.id.text1)
        statsView.textSize = 12f
        updateWidget()
    }
    
    override fun updateWidget() {
        val runtime = Runtime.getRuntime()
        val totalMemory = runtime.totalMemory() / (1024 * 1024)
        val freeMemory = runtime.freeMemory() / (1024 * 1024)
        val usedMemory = totalMemory - freeMemory
        
        val cpuCores = Runtime.getRuntime().availableProcessors()
        
        statsView.text = """
            RAM: ${usedMemory}MB / ${totalMemory}MB
            CPU: $cpuCores cores
        """.trimIndent()
    }
    
    override fun getWidgetType(): String = "stats"
}

class MusicWidget(context: Context, attrs: AttributeSet? = null) : LauncherWidget(context, attrs) {
    
    private lateinit var musicView: TextView
    
    init {
        LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, this)
        musicView = findViewById(android.R.id.text1)
        musicView.text = "🎵 No music playing"
    }
    
    override fun updateWidget() {
        musicView.text = "🎵 Music Widget"
    }
    
    override fun getWidgetType(): String = "music"
}

class CLIPipeWidget(context: Context, private val command: String, attrs: AttributeSet? = null) : LauncherWidget(context, attrs) {

    private lateinit var outputView: TextView
    private var updateInterval: Long = 2000

    init {
        LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, this)
        outputView = findViewById(android.R.id.text1)
        outputView.textSize = 12f
        updateWidget()
    }

    override fun updateWidget() {
        Thread {
            try {
                val proc = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
                val out = proc.inputStream.bufferedReader().use { it.readText() }
                post { outputView.text = out.trim().take(1024) }
                proc.waitFor()
            } catch (e: Exception) {
                post { outputView.text = "Error executing: ${command.split(" ").first()}" }
            }
        }.start()

        postDelayed({ updateWidget() }, updateInterval)
    }

    override fun getWidgetType(): String = "cli"
}

object WidgetFactory {
    fun createWidget(type: String, context: Context, vararg args: String): LauncherWidget? {
        return when (type) {
            "clock" -> ClockWidget(context)
            "weather" -> WeatherWidget(context)
            "stats" -> SystemStatsWidget(context)
            "music" -> MusicWidget(context)
            "cli" -> {
                val cmd = if (args.isNotEmpty()) args[0] else "echo 'No command'"
                CLIPipeWidget(context, cmd)
            }
            else -> null
        }
    }
}
