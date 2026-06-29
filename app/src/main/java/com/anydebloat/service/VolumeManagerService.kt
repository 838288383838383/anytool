package com.anydebloat.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.anydebloat.ui.AppLauncherActivity

class VolumeManagerService : AccessibilityService() {

    companion object {
        private const val TAG = "VolumeManager"
        private const val PREFS = "volume_manager_prefs"
        private const val KEY_VOLUME_UP_COUNT = "volume_up_count"
        private const val KEY_LAST_PRESS = "last_press"
        private const val KEY_ENABLED = "service_enabled"
        private const val KEY_VOLUME_UP_ACTION = "volume_up_action"
        private const val KEY_VOLUME_DOWN_ACTION = "volume_down_action"
        private const val DOUBLE_PRESS_WINDOW = 800L

        var isRunning = false
            private set
    }

    private var volumeUpCount = 0
    private var lastVolumeUpTime = 0L
    private lateinit var prefs: SharedPreferences

    override fun onServiceConnected() {
        super.onServiceConnected()
        isRunning = true
        prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        volumeUpCount = prefs.getInt(KEY_VOLUME_UP_COUNT, 0)
        lastVolumeUpTime = prefs.getLong(KEY_LAST_PRESS, 0)

        serviceInfo = serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                    AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
        Log.d(TAG, "VolumeManagerService connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        if (!prefs.getBoolean(KEY_ENABLED, false)) return false
        if (event == null) return false

        val volumeUpAction = prefs.getString(KEY_VOLUME_UP_ACTION, "launcher") ?: "launcher"
        val volumeDownAction = prefs.getString(KEY_VOLUME_DOWN_ACTION, "none") ?: "none"

        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    handleVolumeUp(volumeUpAction)
                    return true
                }
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    handleVolumeDown(volumeDownAction)
                    return true
                }
            }
        }
        return false
    }

    private fun handleVolumeUp(action: String) {
        val now = SystemClock.elapsedRealtime()
        if (now - lastVolumeUpTime < DOUBLE_PRESS_WINDOW) {
            volumeUpCount++
        } else {
            volumeUpCount = 1
        }
        lastVolumeUpTime = now
        prefs.edit()
            .putInt(KEY_VOLUME_UP_COUNT, volumeUpCount)
            .putLong(KEY_LAST_PRESS, lastVolumeUpTime)
            .apply()

        if (volumeUpCount >= 2) {
            volumeUpCount = 0
            prefs.edit().putInt(KEY_VOLUME_UP_COUNT, 0).apply()
            executeAction(action)
        }
    }

    private fun handleVolumeDown(action: String) {
        if (action != "none") {
            executeAction(action)
        }
    }

    private fun executeAction(action: String) {
        when (action) {
            "launcher" -> {
                val intent = Intent(this, AppLauncherActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            }
            "app1", "app2", "app3", "app4", "app5" -> {
                val appNum = action.removePrefix("app").toIntOrNull() ?: return
                val pkg = prefs.getString("shortcut_app_$appNum", null) ?: return
                val launchIntent = packageManager.getLaunchIntentForPackage(pkg)
                if (launchIntent != null) {
                    startActivity(launchIntent)
                }
            }
            "screenshot" -> {
                performGlobalAction(GLOBAL_ACTION_TAKE_SCREENSHOT)
            }
            "lock" -> {
                performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
            }
            "recent" -> {
                performGlobalAction(GLOBAL_ACTION_RECENTS)
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ENABLED, enabled).apply()
    }

    fun setVolumeUpAction(action: String) {
        prefs.edit().putString(KEY_VOLUME_UP_ACTION, action).apply()
    }

    fun setVolumeDownAction(action: String) {
        prefs.edit().putString(KEY_VOLUME_DOWN_ACTION, action).apply()
    }

    fun setShortcutApp(slot: Int, packageName: String) {
        prefs.edit().putString("shortcut_app_$slot", packageName).apply()
    }

    fun isEnabled(): Boolean = prefs.getBoolean(KEY_ENABLED, false)

    override fun onInterrupt() {
        isRunning = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }
}
