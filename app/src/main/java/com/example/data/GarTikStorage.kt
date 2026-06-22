package com.example.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

data class DownloadItem(
    val id: String,
    val url: String,
    val user: String,
    val caption: String,
    val resolution: String,
    val audioOnly: Boolean,
    val musicTitle: String,
    val outputFileName: String,
    val sizeBytes: Long,
    val timestamp: String,
    val isDownloaded: Boolean = true
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("url", url)
        json.put("user", user)
        json.put("caption", caption)
        json.put("resolution", resolution)
        json.put("audioOnly", audioOnly)
        json.put("musicTitle", musicTitle)
        json.put("outputFileName", outputFileName)
        json.put("sizeBytes", sizeBytes)
        json.put("timestamp", timestamp)
        json.put("isDownloaded", isDownloaded)
        return json
    }

    companion object {
        fun fromJson(json: JSONObject): DownloadItem {
            return DownloadItem(
                id = json.optString("id", ""),
                url = json.optString("url", ""),
                user = json.optString("user", "unknown"),
                caption = json.optString("caption", ""),
                resolution = json.optString("resolution", "1080p"),
                audioOnly = json.optBoolean("audioOnly", false),
                musicTitle = json.optString("musicTitle", "Original Sound"),
                outputFileName = json.optString("outputFileName", ""),
                sizeBytes = json.optLong("sizeBytes", 0L),
                timestamp = json.optString("timestamp", ""),
                isDownloaded = json.optBoolean("isDownloaded", true)
            )
        }
    }
}

enum class ThemeMode {
    LIGHT,
    DARK,
    MATERIAL_YOU,
    FROSTED_GLASS,
    GRADIENT_GLASS,
    LIQUID_GLASS
}

class GarTikStorage(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("gartik_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DOWNLOADS = "downloads_history"
        private const val KEY_SCRAPER_SCRIPT = "scraper_script"
        private const val KEY_PROXY_ENABLED = "proxy_enabled"
        private const val KEY_WATERMARK_DISABLED = "watermark_disabled"
        private const val KEY_SELECTED_QUALITY = "selected_quality"
        private const val KEY_THEME_MODE = "theme_mode"
    }

    fun getThemeMode(): ThemeMode {
        val name = prefs.getString(KEY_THEME_MODE, ThemeMode.LIGHT.name) ?: ThemeMode.LIGHT.name
        return try {
            ThemeMode.valueOf(name)
        } catch (e: Exception) {
            ThemeMode.LIGHT
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }

    fun getScraperScript(): String {
        val saved = prefs.getString(KEY_SCRAPER_SCRIPT, null)
        if (saved != null) return saved

        return try {
            context.assets.open("default_scraper.js").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            "// Error loading default_scraper.js asset code"
        }
    }

    fun saveScraperScript(script: String) {
        prefs.edit().putString(KEY_SCRAPER_SCRIPT, script).apply()
    }

    fun resetScraperScript() {
        prefs.edit().remove(KEY_SCRAPER_SCRIPT).apply()
    }

    fun isProxyEnabled(): Boolean {
        return prefs.getBoolean(KEY_PROXY_ENABLED, false)
    }

    fun setProxyEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PROXY_ENABLED, enabled).apply()
    }

    fun isWatermarkDisabled(): Boolean {
        return prefs.getBoolean(KEY_WATERMARK_DISABLED, true)
    }

    fun setWatermarkDisabled(disabled: Boolean) {
        prefs.edit().putBoolean(KEY_WATERMARK_DISABLED, disabled).apply()
    }

    fun getSelectedQuality(): String {
        return prefs.getString(KEY_SELECTED_QUALITY, "1080p") ?: "1080p"
    }

    fun setSelectedQuality(quality: String) {
        prefs.edit().putString(KEY_SELECTED_QUALITY, quality).apply()
    }

    fun getDownloadHistory(): List<DownloadItem> {
        val rawJson = prefs.getString(KEY_DOWNLOADS, null) ?: return emptyList()
        return try {
            val list = mutableListOf<DownloadItem>()
            val array = JSONArray(rawJson)
            for (i in 0 until array.length()) {
                list.add(DownloadItem.fromJson(array.getJSONObject(i)))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveDownloadHistory(history: List<DownloadItem>) {
        val array = JSONArray()
        history.forEach { array.put(it.toJson()) }
        prefs.edit().putString(KEY_DOWNLOADS, array.toString()).apply()
    }

    fun addDownloadItem(item: DownloadItem) {
        val history = getDownloadHistory().toMutableList()
        history.add(0, item)
        saveDownloadHistory(history)
    }

    fun deleteDownloadItem(id: String) {
        val history = getDownloadHistory().filter { it.id != id }
        saveDownloadHistory(history)
    }

    fun clearHistory() {
        saveDownloadHistory(emptyList())
    }
}
