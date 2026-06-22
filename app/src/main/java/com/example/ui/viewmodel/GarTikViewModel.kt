package com.example.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DownloadItem
import com.example.data.GarTikStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

sealed class DownloaderState {
    object Idle : DownloaderState()
    object Processing : DownloaderState()
    data class Success(val item: DownloadItem) : DownloaderState()
    data class Error(val message: String) : DownloaderState()
}

class GarTikViewModel(context: Context) : ViewModel() {
    private val storage = GarTikStorage(context)

    // Form inputs and selections
    val inputUrl = mutableStateOf("")
    val selectedQuality = mutableStateOf(storage.getSelectedQuality())
    val isWatermarkDisabled = mutableStateOf(storage.isWatermarkDisabled())
    val isProxyEnabled = mutableStateOf(storage.isProxyEnabled())

    // Scraper Script
    val scraperScriptCode = mutableStateOf(storage.getScraperScript())

    // States
    private val _downloaderState = MutableStateFlow<DownloaderState>(DownloaderState.Idle)
    val downloaderState = _downloaderState.asStateFlow()

    // Download log entries for terminal panel
    val consoleLogs = mutableStateListOf<String>()

    // Local histories
    private val _downloadsList = MutableStateFlow<List<DownloadItem>>(emptyList())
    val downloadsList = _downloadsList.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        _downloadsList.value = storage.getDownloadHistory()
    }

    fun saveScript(newScript: String) {
        scraperScriptCode.value = newScript
        storage.saveScraperScript(newScript)
    }

    fun resetScriptToDefault() {
        storage.resetScraperScript()
        scraperScriptCode.value = storage.getScraperScript()
    }

    fun toggleProxy(enabled: Boolean) {
        isProxyEnabled.value = enabled
        storage.setProxyEnabled(enabled)
    }

    fun toggleWatermark(disabled: Boolean) {
        isWatermarkDisabled.value = disabled
        storage.setWatermarkDisabled(disabled)
    }

    fun selectQuality(quality: String) {
        selectedQuality.value = quality
        storage.setSelectedQuality(quality)
    }

    fun clearLogConsole() {
        consoleLogs.clear()
    }

    fun cleanHistory() {
        storage.clearHistory()
        loadHistory()
    }

    fun deleteItem(id: String) {
        storage.deleteDownloadItem(id)
        loadHistory()
    }

    fun generateSimulatedItem(url: String, quality: String): DownloadItem {
        val cleanUrl = url.trim()
        val videoIdPattern = Regex("""video/(\d+)""")
        val idMatch = videoIdPattern.find(cleanUrl)
        val extractedId = idMatch?.groupValues?.get(1) ?: (1000000000000000000L + Random.nextLong(8999999999999999999L)).toString().take(19)

        // Try to guess a handle or generate one
        val handlePattern = Regex("""@([a-zA-Z0-9_\.]+)""")
        val handleMatch = handlePattern.find(cleanUrl)
        val userHandle = handleMatch?.groupValues?.get(1) ?: listOf("garry_tiktok", "zen_creator", "m3_explorer", "foryou_hype", "tech_scout").random()

        val captionOptions = listOf(
            "Wow! Check out this incredible transition shot 😱🔥 #smooth #foryou #viral",
            "This recipe is so easy and delicious! Must try it this weekend 🍕🤤 #cooking #recipe #foodie",
            "When you try to code in Kotlin for 10 hours straight without coffee ☕️🤖 #programmerhumor #android #development",
            "Perfect morning vibe under the golden sunrise 🌅✨ #nature #peaceful #scenery",
            "Unbelievable dynamic visual effects using simple camera angles tutorial! 📸💡 #creatorguide #visualtips"
        )
        val caption = captionOptions.random()

        val audios = listOf(
            "Original Sound - @$userHandle",
            "Trending Beat Vol. 2 - Soundwave",
            "Aesthetic Acoustic Lo-Fi - Sunset Chords",
            "Neon Synthwave Drive - Outrun Track"
        )
        val musicTitle = audios.random()

        val sizeMb = when(quality) {
            "1080p" -> Random.nextDouble(15.0, 35.0)
            "720p" -> Random.nextDouble(9.0, 18.0)
            "480p" -> Random.nextDouble(4.0, 8.5)
            else -> Random.nextDouble(1.5, 3.2) // Audio Only
        }
        val sizeBytes = (sizeMb * 1024 * 1024).toLong()

        val format = if (quality == "Audio Only") "mp3" else "mp4"
        val ext = if (quality == "Audio Only") "mp3" else "mp4"
        val outputName = "${userHandle}_$extractedId.$ext"

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateString = sdf.format(Date())

        return DownloadItem(
            id = extractedId,
            url = cleanUrl,
            user = "@$userHandle",
            caption = caption,
            resolution = quality,
            audioOnly = (quality == "Audio Only"),
            musicTitle = musicTitle,
            outputFileName = outputName,
            sizeBytes = sizeBytes,
            timestamp = dateString
        )
    }

    fun startDownloadPipeline(url: String) {
        val trimmed = url.trim()
        if (trimmed.isEmpty()) {
            _downloaderState.value = DownloaderState.Error("Please paste a valid TikTok link")
            return
        }

        if (!trimmed.contains("tiktok.com")) {
            _downloaderState.value = DownloaderState.Error("Invalid TikTok link format. Please include 'tiktok.com'")
            return
        }

        viewModelScope.launch {
            _downloaderState.value = DownloaderState.Processing
            consoleLogs.clear()

            // 1. Start Scraping Logs simulation
            consoleLogs.add("[+] Processing URL: $trimmed")
            delay(500)
            consoleLogs.add("[+] TikTok Scraper initialised successfully")
            delay(400)
            consoleLogs.add("[+] Reading environment scripts...")
            delay(300)
            consoleLogs.add("[+] Custom JS Script loaded from device memory (${scraperScriptCode.value.length} bytes)")
            delay(600)
            
            val simulatedItem = generateSimulatedItem(trimmed, selectedQuality.value)

            consoleLogs.add("[+] Executing virtual Chromium browser emulator context...")
            delay(700)
            consoleLogs.add("[+] Bypass WAF protection rules setup completed")
            if (isProxyEnabled.value) {
                consoleLogs.add("[+] Routing requests via encrypted proxy channel")
                delay(500)
            }
            consoleLogs.add("[+] Parsing source structures...")
            delay(500)
            consoleLogs.add("[+] Video payload detected! ID: ${simulatedItem.id}")
            delay(400)
            consoleLogs.add("[+] Author credit fetched: ${simulatedItem.user}")
            delay(400)
            consoleLogs.add("[+] Headline text: \"${simulatedItem.caption.take(30)}...\"")
            delay(300)
            consoleLogs.add("[+] Best target CDN server streaming node found")
            
            // 2. Download Simulation with progress tracking
            delay(500)
            consoleLogs.add("[+] Downloading Stream chunk 1/4... [████░░░░░░░░░░░░░░░░] 25%")
            delay(400)
            consoleLogs.add("[+] Downloading Stream chunk 2/4... [██████████░░░░░░░░░░░░] 50%")
            delay(400)
            consoleLogs.add("[+] Downloading Stream chunk 3/4... [███████████████░░░░░░] 75%")
            delay(500)
            consoleLogs.add("[+] Downloading Stream chunk 4/4... [████████████████████] 100%")
            delay(300)
            consoleLogs.add("[+] Video binary size: %.2f MB successfully downloaded".format(simulatedItem.sizeBytes.toDouble() / (1024 * 1024)))

            // 3. Re-encode process (as stated in output logs of scraper.js)
            if (!simulatedItem.audioOnly) {
                consoleLogs.add("[+] Triggering FFmpeg Transcoder shell pipe...")
                delay(600)
                consoleLogs.add("[+] Re-encoding stream for resolution: ${simulatedItem.resolution}")
                delay(700)
                if (isWatermarkDisabled.value) {
                    consoleLogs.add("[+] Extracting raw audio overlay node to remove video watermark track")
                    delay(500)
                }
                consoleLogs.add("[+] High-definition rendering pipeline re-encoding finished successfully")
            } else {
                consoleLogs.add("[+] Extracting separate MP3 audio overlay track...")
                delay(600)
                consoleLogs.add("[+] Saving audio track: ${simulatedItem.musicTitle}")
                delay(400)
            }
            
            consoleLogs.add("[+] Output finalized: ${simulatedItem.outputFileName}")
            delay(300)
            consoleLogs.add("[+] PIPELINE COMPLETE! 200 SUCCESS.")

            // Save to storage
            storage.addDownloadItem(simulatedItem)
            loadHistory()

            _downloaderState.value = DownloaderState.Success(simulatedItem)
        }
    }

    fun resetState() {
        _downloaderState.value = DownloaderState.Idle
    }
}
