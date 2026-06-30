package com.anydebloat.launcher

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.anydebloat.launcher.config.LauncherConfig
import com.anydebloat.launcher.config.LauncherConfigManager
import com.anydebloat.launcher.ui.HomeScreenManager
import com.anydebloat.launcher.widgets.WidgetFactory

class AnyToolLauncherActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var homeScreenManager: HomeScreenManager
    private lateinit var config: LauncherConfig
    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var gridLayout: GridLayout
    private lateinit var widgetContainer: LinearLayout

    private var currentPage = 0
    private var allApps = listOf<ResolveInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(android.R.layout.activity_list_item)
        
        homeScreenManager = HomeScreenManager(this)
        config = LauncherConfigManager.loadConfig(filesDir)
        gestureDetector = GestureDetectorCompat(this, this)
        
        setupViews()
        loadApps()
        loadHomeScreen()
        setupGestures()
    }

    private fun setupViews() {
        gridLayout = GridLayout(this).apply {
            columnCount = config.grid.columns
            rowCount = config.grid.rows
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        
        widgetContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun loadApps() {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        allApps = packageManager.queryIntentActivities(intent, 0)
            .sortedBy { it.loadLabel(packageManager).toString() }
    }

    private fun loadHomeScreen() {
        gridLayout.removeAllViews()
        
        val pages = homeScreenManager.getHomePages()
        val currentPageLayout = pages.find { it.pageIndex == currentPage }
        
        if (currentPageLayout != null) {
            currentPageLayout.apps.forEach { appItem ->
                val appView = createAppIcon(appItem.packageName, appItem.label)
                val params = GridLayout.LayoutParams().apply {
                    columnSpec = GridLayout.spec(appItem.gridX)
                    rowSpec = GridLayout.spec(appItem.gridY)
                    width = config.grid.iconSize
                    height = config.grid.iconSize + 40
                }
                gridLayout.addView(appView, params)
            }
            
            currentPageLayout.widgets.forEach { widgetItem ->
                val widget = WidgetFactory.createWidget(widgetItem.type, this)
                widget?.updateWidget()
                if (widget != null) {
                    widgetContainer.addView(widget)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createAppIcon(packageName: String, label: String): View {
        val container = android.widget.FrameLayout(this).apply {
            layoutParams = android.widget.FrameLayout.LayoutParams(
                config.grid.iconSize,
                config.grid.iconSize + 40
            )
        }
        
        val iconView = ImageView(this).apply {
            layoutParams = android.widget.FrameLayout.LayoutParams(
                config.grid.iconSize,
                config.grid.iconSize
            )
            try {
                setImageDrawable(packageManager.getApplicationIcon(packageName))
            } catch (e: Exception) {
                setImageDrawable(getDrawable(android.R.drawable.ic_dialog_info))
            }
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            setOnClickListener { launchApp(packageName) }
            setOnLongClickListener { showAppMenu(packageName, label); true }
        }
        
        val labelView = TextView(this).apply {
            layoutParams = android.widget.FrameLayout.LayoutParams(
                config.grid.iconSize,
                40,
                android.view.Gravity.BOTTOM
            )
            text = label.take(10)
            textSize = config.appearance.fontSize.toFloat()
            gravity = android.view.Gravity.CENTER
            maxLines = 1
        }
        
        container.addView(iconView)
        container.addView(labelView)
        return container
    }

    private fun launchApp(packageName: String) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showAppMenu(packageName: String, label: String) {
        android.app.AlertDialog.Builder(this)
            .setTitle(label)
            .setItems(arrayOf("Open", "Add to Favorites", "Remove", "Info")) { _, which ->
                when (which) {
                    0 -> launchApp(packageName)
                    1 -> homeScreenManager.addFavorite(packageName)
                    2 -> homeScreenManager.removeAppFromHome(packageName)
                    3 -> showAppInfo(packageName)
                }
            }
            .show()
    }

    private fun showAppInfo(packageName: String) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.parse("package:$packageName")
        }
        startActivity(intent)
    }

    private fun setupGestures() {
        window.decorView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }

    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean = false
    override fun onDown(e: MotionEvent): Boolean = true
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 != null && e2 != null) {
            val diffY = e2.y - e1.y
            when {
                diffY > 100 && config.gestures.swipeDown == "notifications" -> {
                    startActivity(Intent(Intent.ACTION_VIEW))
                    return true
                }
                diffY < -100 && config.gestures.swipeUp == "app_drawer" -> {
                    showAppDrawer()
                    return true
                }
            }
        }
        return false
    }

    private fun showAppDrawer() {
        android.app.AlertDialog.Builder(this)
            .setTitle("All Apps")
            .setItems(allApps.map { it.loadLabel(packageManager).toString() }.toTypedArray()) { _, which ->
                launchApp(allApps[which].activityInfo.packageName)
            }
            .show()
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean = false

    override fun onLongPress(e: MotionEvent) {
        if (config.gestures.longPress == "app_menu") {
            showAppDrawer()
        }
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean = false
}
