package com.anydebloat.launcher.ui

import android.content.Context
import android.content.SharedPreferences
import com.anydebloat.launcher.config.LauncherConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class HomePageLayout(
    val pageIndex: Int,
    val apps: List<AppLayoutItem> = emptyList(),
    val widgets: List<WidgetLayoutItem> = emptyList()
)

data class AppLayoutItem(
    val packageName: String,
    val gridX: Int,
    val gridY: Int,
    val label: String = ""
)

data class WidgetLayoutItem(
    val widgetId: String,
    val gridX: Int,
    val gridY: Int,
    val width: Int,
    val height: Int,
    val type: String // "clock", "weather", "music", "stats"
)

class HomeScreenManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("launcher_home", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val KEY_PAGES = "home_pages"
        private const val KEY_FAVORITES = "favorites"
    }
    
    fun getHomePages(): List<HomePageLayout> {
        return try {
            val json = prefs.getString(KEY_PAGES, "[]") ?: "[]"
            val type = object : TypeToken<List<HomePageLayout>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun savePage(page: HomePageLayout) {
        try {
            val pages = getHomePages().toMutableList()
            val index = pages.indexOfFirst { it.pageIndex == page.pageIndex }
            
            if (index >= 0) {
                pages[index] = page
            } else {
                pages.add(page)
            }
            
            prefs.edit().putString(KEY_PAGES, gson.toJson(pages)).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun addAppToHome(packageName: String, label: String, gridX: Int, gridY: Int, pageIndex: Int = 0) {
        val pages = getHomePages().toMutableList()
        var targetPage = pages.find { it.pageIndex == pageIndex }
        
        if (targetPage == null) {
            targetPage = HomePageLayout(pageIndex)
            pages.add(targetPage)
        }
        
        val newApps = targetPage.apps.toMutableList()
        newApps.add(AppLayoutItem(packageName, gridX, gridY, label))
        
        savePage(targetPage.copy(apps = newApps))
    }
    
    fun removeAppFromHome(packageName: String) {
        val pages = getHomePages().toMutableList()
        
        pages.forEach { page ->
            val newApps = page.apps.filter { it.packageName != packageName }
            if (newApps.size != page.apps.size) {
                savePage(page.copy(apps = newApps))
            }
        }
    }
    
    fun moveApp(packageName: String, gridX: Int, gridY: Int) {
        val pages = getHomePages().toMutableList()
        
        pages.forEach { page ->
            val appIndex = page.apps.indexOfFirst { it.packageName == packageName }
            if (appIndex >= 0) {
                val newApps = page.apps.toMutableList()
                newApps[appIndex] = newApps[appIndex].copy(gridX = gridX, gridY = gridY)
                savePage(page.copy(apps = newApps))
            }
        }
    }
    
    fun addWidget(widget: WidgetLayoutItem, pageIndex: Int = 0) {
        val pages = getHomePages().toMutableList()
        var targetPage = pages.find { it.pageIndex == pageIndex }
        
        if (targetPage == null) {
            targetPage = HomePageLayout(pageIndex)
            pages.add(targetPage)
        }
        
        val newWidgets = targetPage.widgets.toMutableList()
        newWidgets.add(widget)
        
        savePage(targetPage.copy(widgets = newWidgets))
    }
    
    fun removeWidget(widgetId: String) {
        val pages = getHomePages().toMutableList()
        
        pages.forEach { page ->
            val newWidgets = page.widgets.filter { it.widgetId != widgetId }
            if (newWidgets.size != page.widgets.size) {
                savePage(page.copy(widgets = newWidgets))
            }
        }
    }
    
    fun getFavorites(): List<String> {
        return try {
            val json = prefs.getString(KEY_FAVORITES, "[]") ?: "[]"
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun addFavorite(packageName: String) {
        val favorites = getFavorites().toMutableList()
        if (!favorites.contains(packageName)) {
            favorites.add(packageName)
            prefs.edit().putString(KEY_FAVORITES, gson.toJson(favorites)).apply()
        }
    }
    
    fun removeFavorite(packageName: String) {
        val favorites = getFavorites().toMutableList()
        favorites.remove(packageName)
        prefs.edit().putString(KEY_FAVORITES, gson.toJson(favorites)).apply()
    }
}
