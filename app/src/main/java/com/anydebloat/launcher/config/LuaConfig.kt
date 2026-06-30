package com.anydebloat.launcher.config

import android.content.Context
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object LuaConfigManager {
    private const val LUA_DIR = "launcher_lua"

    fun saveRawLua(context: Context, name: String, content: String) {
        val dir = File(context.filesDir, LUA_DIR)
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, name)
        file.writeText(content)
    }

    fun loadRawLua(context: Context, name: String): String? {
        val file = File(File(context.filesDir, LUA_DIR), name)
        return if (file.exists()) file.readText() else null
    }

    fun importLuaFromUrl(context: Context, url: String, name: String): Boolean {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.requestMethod = "GET"
            connection.inputStream.bufferedReader().use { reader ->
                val content = reader.readText()
                saveRawLua(context, name, content)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Very small parser: extract simple assignments like key = "value" or key = 123
    fun parseSimpleAssignments(luaContent: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val regex = Regex("^\\s*([A-Za-z0-9_]+)\\s*=\\s*\"?([^\";\\n]+)\"?", RegexOption.MULTILINE)
        regex.findAll(luaContent).forEach { mr ->
            val key = mr.groupValues[1]
            val value = mr.groupValues[2].trim()
            map[key] = value
        }
        return map
    }
}
