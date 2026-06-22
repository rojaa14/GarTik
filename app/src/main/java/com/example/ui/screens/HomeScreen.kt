package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DownloadItem
import com.example.ui.theme.GarTikPrimary
import com.example.ui.theme.GarTikSecondary
import com.example.ui.theme.GarTikTertiary
import com.example.ui.viewmodel.DownloaderState
import com.example.ui.viewmodel.GarTikViewModel

@Composable
fun HomeScreen(viewModel: GarTikViewModel) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val systemState by viewModel.downloaderState.collectAsState()
    val downloads by viewModel.downloadsList.collectAsState()

    var activeTab by remember { mutableStateOf("DOWNLOAD") } // "DOWNLOAD" or "HISTORY"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)) // Light gray slate
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .border(width = 0.dp, color = Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Circular mini-avatar logo
                val logoResId = context.resources.getIdentifier("gartik_logo", "drawable", context.packageName)
                if (logoResId != 0) {
                    Image(
                        painter = painterResource(id = logoResId),
                        contentDescription = "GarTik logo",
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(GarTikPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("G", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "GarTik",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "JS Scraper Extension",
                        fontSize = 11.sp,
                        color = GarTikSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Tabs toggle selector
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (activeTab == "DOWNLOAD") GarTikTertiary else Color.Transparent)
                        .clickable { activeTab = "DOWNLOAD" }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        "Download",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == "DOWNLOAD") Color.White else Color(0xFF64748B)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (activeTab == "HISTORY") GarTikTertiary else Color.Transparent)
                        .clickable { activeTab = "HISTORY" }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "History",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activeTab == "HISTORY") Color.White else Color(0xFF64748B)
                        )
                        if (downloads.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(GarTikPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    downloads.size.toString(),
                                    fontSize = 9.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Divider(color = Color(0xFFE2E8F0), thickness = 1.dp)

        AnimatedVisibility(
            visible = (activeTab == "DOWNLOAD"),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Clipboard insertion card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Paste TikTok Link",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = viewModel.inputUrl.value,
                                onValueChange = { viewModel.inputUrl.value = it },
                                placeholder = {
                                    Text(
                                        "https://www.tiktok.com/@creator/video/...",
                                        fontSize = 13.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                textStyle = TextStyle(fontSize = 13.sp, color = Color(0xFF1E293B)),
                                trailingIcon = {
                                    if (viewModel.inputUrl.value.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.inputUrl.value = "" }) {
                                            Icon(
                                                Icons.Default.Clear,
                                                contentDescription = "Clear input text",
                                                tint = Color(0xFF64748B)
                                            )
                                        }
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GarTikPrimary,
                                    unfocusedBorderColor = Color(0xFFCBD5E1),
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // Fast Paste Button
                            Button(
                                onClick = {
                                    val clipText = clipboardManager.getText()?.text
                                    if (!clipText.isNullOrEmpty()) {
                                        viewModel.inputUrl.value = clipText
                                        Toast.makeText(context, "Link pasted!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Clipboard is empty", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE2E8F0),
                                    contentColor = Color(0xFF334155)
                                ),
                                modifier = Modifier.height(54.dp)
                            ) {
                                Text("Paste", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // High Definition options picker
                        Text(
                            text = "Download Resolution Quality Selection:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        val options = listOf("1080p", "720p", "480p", "Audio Only")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            options.forEach { option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable { viewModel.selectQuality(option) }
                                        .padding(2.dp)
                                ) {
                                    RadioButton(
                                        selected = (viewModel.selectedQuality.value == option),
                                        onClick = { viewModel.selectQuality(option) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = GarTikPrimary,
                                            unselectedColor = Color(0xFF94A3B8)
                                        ),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = option,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (viewModel.selectedQuality.value == option) GarTikPrimary else Color(0xFF475569)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Launch Process Scrape Button
                Button(
                    onClick = {
                        viewModel.startDownloadPipeline(viewModel.inputUrl.value)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GarTikPrimary,
                        contentColor = Color.White
                    ),
                    enabled = (systemState !is DownloaderState.Processing)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = "start scraper download button"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (systemState is DownloaderState.Processing) "Processing Scraper Script..." else "Download with HD Scraper Pipe",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Log Panel
                Text(
                    text = "JS Scraper Execution Console Terminal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF475569)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)), // Pure terminal dark black-slate
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                        if (viewModel.consoleLogs.isEmpty() && systemState is DownloaderState.Idle) {
                            // Terminal welcome message
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Terminal,
                                    contentDescription = "Console",
                                    tint = Color(0xFF334155),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Terminal ready. Enter TikTok URL and press Download to monitor Scraper execution code live.",
                                    color = Color(0xFF64748B),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                )
                            }
                        } else {
                            val lazyListState = rememberLazyListState()
                            
                            // Scroll to bottom automatically as log lines streaming
                            LaunchedEffect(key1 = viewModel.consoleLogs.size) {
                                if (viewModel.consoleLogs.isNotEmpty()) {
                                    lazyListState.animateScrollToItem(viewModel.consoleLogs.size - 1)
                                }
                            }

                            LazyColumn(
                                state = lazyListState,
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(viewModel.consoleLogs) { logLine ->
                                    val logColor = when {
                                        logLine.contains("SUCCESS") || logLine.contains("100% completed") -> Color(0xFF22C55E) // Green code
                                        logLine.contains("Bypass") || logLine.contains("Routing") -> Color(0xFF38BDF8) // High definition light blue info
                                        logLine.contains("ERROR") || logLine.contains("failed") -> Color(0xFFEF4444) // Error alerts red
                                        else -> Color(0xFFE2E8F0) // Clean grayish white
                                    }
                                    Text(
                                        text = logLine,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        color = logColor,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Downloader message responses
                when (val currentStatus = systemState) {
                    is DownloaderState.Success -> {
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.NetworkCheck,
                                    contentDescription = "Success",
                                    tint = Color(0xFF15803D),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        "Saved: ${currentStatus.item.outputFileName}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF166534),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Resolution selected: ${currentStatus.item.resolution} | Size: %.2f MB".format(currentStatus.item.sizeBytes.toDouble() / (1024*1024)),
                                        fontSize = 10.sp,
                                        color = Color(0xFF15803D)
                                    )
                                }
                            }
                        }
                    }
                    is DownloaderState.Error -> {
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = "Error",
                                    tint = Color(0xFFB91C1C),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentStatus.message,
                                    fontSize = 12.sp,
                                    color = Color(0xFF991B1B),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }
        }

        // Animated Screen change to HISTORY tab
        AnimatedVisibility(
            visible = (activeTab == "HISTORY"),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header with actions block
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = "History icon",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "HD Downloads Folder History",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF334155)
                        )
                    }

                    if (downloads.isNotEmpty()) {
                        Text(
                            text = "Clear All",
                            color = GarTikPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clickable {
                                    viewModel.cleanHistory()
                                    Toast.makeText(context, "History cleared!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Empty State Screen
                if (downloads.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.History,
                                contentDescription = "no history icon element placeholder",
                                tint = Color(0xFFCBD5E1),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                "No Downloaded Media Yet",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF475569)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Once you run a successful scraping pipeline, your clean high resolution media item (.mp4/mp3) downloads will automatically appear in this local folder.",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                lineHeight = 18.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(downloads) { historyItem ->
                            HistoryItemCard(
                                item = historyItem,
                                onDelete = {
                                    viewModel.deleteItem(historyItem.id)
                                    Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(
    item: DownloadItem,
    onDelete: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // User ID tag and Icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (item.audioOnly) GarTikSecondary else GarTikPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (item.audioOnly) Icons.Default.MusicNote else Icons.Default.PlayArrow,
                            contentDescription = "play/music mode status bar visual badge icon",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.user,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF1E293B)
                    )
                }

                // Delete operation (touch target >= 48dp)
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete downloaded history record",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Caption headline text
            Text(
                text = item.caption,
                fontSize = 12.sp,
                color = Color(0xFF475569),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Information details footer row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "File: ${item.outputFileName}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(180.dp)
                    )
                    Text(
                        text = "Quality: ${item.resolution} | Size: %.2f MB".format(item.sizeBytes.toDouble() / (1024 * 1024)),
                        fontSize = 9.sp,
                        color = Color(0xFF64748B)
                    )
                }

                // Share element
                Row {
                    IconButton(
                        onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Downloaded matching TikTok via GarTik app!\nUser Link: ${item.url}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share video details link",
                            tint = GarTikSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Play link inside external browser
                    IconButton(
                        onClick = {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                            context.startActivity(browserIntent)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Play source video on tiktok website",
                            tint = GarTikPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// Border helper
@Composable
fun BorderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)
