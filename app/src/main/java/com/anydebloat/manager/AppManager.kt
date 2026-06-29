package com.anydebloat.manager

import android.content.Context
import android.content.SharedPreferences
import com.anydebloat.models.DebloatMode
import com.anydebloat.models.OEM
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("anydebloat_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_SELECTED_MODE = "selected_mode"
        private const val KEY_SELECTED_OEM = "selected_oem"
        private const val KEY_DEBLOAT_HISTORY = "debloat_history"
        private const val KEY_BACKUP_LISTS = "backup_lists"

        @Volatile
        private var instance: AppManager? = null

        fun getInstance(context: Context): AppManager {
            return instance ?: synchronized(this) {
                instance ?: AppManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun setSelectedMode(mode: DebloatMode) {
        prefs.edit().putString(KEY_SELECTED_MODE, mode.name).apply()
    }

    fun getSelectedMode(): DebloatMode {
        val name = prefs.getString(KEY_SELECTED_MODE, DebloatMode.NORMAL.name)
        return try {
            DebloatMode.valueOf(name!!)
        } catch (e: Exception) {
            DebloatMode.NORMAL
        }
    }

    fun setSelectedOEM(oem: OEM) {
        prefs.edit().putString(KEY_SELECTED_OEM, oem.name).apply()
    }

    fun getSelectedOEM(): OEM {
        val name = prefs.getString(KEY_SELECTED_OEM, OEM.ALL.name)
        return try {
            OEM.valueOf(name!!)
        } catch (e: Exception) {
            OEM.ALL
        }
    }

    fun saveBackup(packageNames: List<String>, label: String) {
        val backups = getBackups().toMutableList()
        backups.add(BackupEntry(label, System.currentTimeMillis(), packageNames))
        prefs.edit().putString(KEY_BACKUP_LISTS, gson.toJson(backups)).apply()
    }

    fun getBackups(): List<BackupEntry> {
        val json = prefs.getString(KEY_BACKUP_LISTS, "[]")
        val type = object : TypeToken<List<BackupEntry>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun deleteBackup(label: String) {
        val backups = getBackups().toMutableList()
        backups.removeAll { it.label == label }
        prefs.edit().putString(KEY_BACKUP_LISTS, gson.toJson(backups)).apply()
    }

    fun addToHistory(packageName: String, success: Boolean, mode: String) {
        val history = getHistory().toMutableList()
        history.add(HistoryEntry(packageName, success, mode, System.currentTimeMillis()))
        // Keep last 500 entries
        if (history.size > 500) {
            history.subList(0, history.size - 500).clear()
        }
        prefs.edit().putString(KEY_DEBLOAT_HISTORY, gson.toJson(history)).apply()
    }

    fun getHistory(): List<HistoryEntry> {
        val json = prefs.getString(KEY_DEBLOAT_HISTORY, "[]")
        val type = object : TypeToken<List<HistoryEntry>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clearHistory() {
        prefs.edit().remove(KEY_DEBLOAT_HISTORY).apply()
    }

    data class BackupEntry(
        val label: String,
        val timestamp: Long,
        val packageNames: List<String>
    )

    data class HistoryEntry(
        val packageName: String,
        val success: Boolean,
        val mode: String,
        val timestamp: Long
    )
}
