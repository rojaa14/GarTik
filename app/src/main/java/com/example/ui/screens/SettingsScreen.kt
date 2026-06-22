package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GarTikPrimary
import com.example.ui.theme.GarTikSecondary
import com.example.ui.theme.GarTikTertiary
import com.example.ui.theme.LocalThemeMode
import com.example.ui.viewmodel.GarTikViewModel
import com.example.data.ThemeMode

@Composable
fun SettingsScreen(viewModel: GarTikViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Read current theme mode
    val themeMode = LocalThemeMode.current

    // Dynamic style definitions
    val bgModifier = when(themeMode) {
        ThemeMode.LIGHT -> Modifier.background(Color(0xFFF8FAFC))
        ThemeMode.DARK -> Modifier.background(Color(0xFF0F172A))
        ThemeMode.MATERIAL_YOU -> Modifier.background(MaterialTheme.colorScheme.background)
        else -> Modifier // Let backdrop canvas shine through
    }

    val headerBg = when(themeMode) {
        ThemeMode.LIGHT -> Color.White
        ThemeMode.DARK -> Color(0xFF1E293B)
        ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.surface
        else -> Color(0xC21E293B) // Frosted glass dark tint
    }

    val headerBorder = if (themeMode != ThemeMode.LIGHT && themeMode != ThemeMode.DARK && themeMode != ThemeMode.MATERIAL_YOU) {
        Modifier.border(0.5.dp, Color(0x2BFFFFFF))
    } else Modifier

    val textPrimaryColor = when(themeMode) {
        ThemeMode.LIGHT -> Color(0xFF0F172A)
        ThemeMode.DARK -> Color.White
        ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.onBackground
        else -> Color.White
    }

    val textSecondaryColor = when(themeMode) {
        ThemeMode.LIGHT -> Color(0xFF475569)
        ThemeMode.DARK -> Color(0xFF94A3B8)
        ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> Color(0xFFCBD5E1)
    }

    val cardBgColor = when(themeMode) {
        ThemeMode.LIGHT -> Color.White
        ThemeMode.DARK -> Color(0xFF1E293B)
        ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.surfaceVariant
        ThemeMode.FROSTED_GLASS -> Color(0x13FFFFFF)
        ThemeMode.GRADIENT_GLASS -> Color(0x0EFFFFFF)
        ThemeMode.LIQUID_GLASS -> Color(0x1500ADB5)
    }

    val cardBorderModifier = when(themeMode) {
        ThemeMode.LIGHT -> Modifier.border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
        ThemeMode.DARK -> Modifier.border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
        ThemeMode.MATERIAL_YOU -> Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
        ThemeMode.FROSTED_GLASS -> Modifier.border(0.8.dp, Color(0x22FFFFFF), RoundedCornerShape(16.dp))
        ThemeMode.GRADIENT_GLASS -> Modifier.border(
            width = 1.dp,
            brush = Brush.linearGradient(listOf(Color(0x52EC4899), Color(0x228B5CF6))),
            shape = RoundedCornerShape(16.dp)
        )
        ThemeMode.LIQUID_GLASS -> Modifier.border(
            width = 1.2.dp,
            brush = Brush.linearGradient(listOf(Color(0x6600ADB5), Color(0x1100ADB5))),
            shape = RoundedCornerShape(24.dp)
        )
    }

    val dividerLineColor = when(themeMode) {
        ThemeMode.LIGHT, ThemeMode.MATERIAL_YOU -> Color(0xFFE2E8F0)
        else -> Color(0x1BFFFFFF)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(bgModifier)
    ) {
        // Dynamic App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBg)
                .then(headerBorder)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = "settings header icon",
                tint = if (themeMode == ThemeMode.LIGHT) GarTikPrimary else if (themeMode == ThemeMode.MATERIAL_YOU) MaterialTheme.colorScheme.primary else Color(0xFF38BDF8),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "System Settings & Theme Configuration",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimaryColor
            )
        }

        if (themeMode == ThemeMode.LIGHT || themeMode == ThemeMode.MATERIAL_YOU) {
            Divider(color = dividerLineColor, thickness = 1.dp)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            
            // THEME CONFIGURATION SECTION (Requested user feature!)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
            ) {
                Icon(
                    Icons.Default.Palette,
                    contentDescription = "Theme Palette Icon",
                    tint = if (themeMode == ThemeMode.LIGHT) GarTikPrimary else if (themeMode == ThemeMode.MATERIAL_YOU) MaterialTheme.colorScheme.primary else Color(0xFFEC4899),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Application Visual Theme",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = textPrimaryColor
                )
            }

            // Theme Choices Cards Grid
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(cardBorderModifier)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = cardBgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Tailor your user experience. Select from classic interfaces or gorgeous experimental glass backplates:",
                        fontSize = 11.sp,
                        color = textSecondaryColor,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Helper list of themes to layout
                    val themes = listOf(
                        ThemeChoiceItem(ThemeMode.LIGHT, "Light Mode", "Standard bright, cold-slate workspace layout.", Color(0xFFF1F5F9)),
                        ThemeChoiceItem(ThemeMode.DARK, "Dark Mode", "Sleek immersive high-contrast dark-slate view.", Color(0xFF0F172A)),
                        ThemeChoiceItem(ThemeMode.MATERIAL_YOU, "Material You", "Dynamic scheme adapting to system pastel colors.", Color(0xFFEDE9FE)),
                        ThemeChoiceItem(ThemeMode.FROSTED_GLASS, "Frosted Glass", "Translucent overlay with floating warm orbs.", Color(0x3DFFFFFF)),
                        ThemeChoiceItem(ThemeMode.GRADIENT_GLASS, "Gradient Glass", "Energetic plum and magenta glass layers.", Color(0x228B5CF6)),
                        ThemeChoiceItem(ThemeMode.LIQUID_GLASS, "Liquid Glass", "Aquamarine and organic fluid blue neon bubbles.", Color(0x2B00ADB5))
                    )

                    // Display themes in columns of 2
                    for (i in themes.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ThemeCardCell(
                                item = themes[i],
                                isSelected = (viewModel.themeMode.value == themes[i].mode),
                                onClick = { viewModel.changeThemeMode(themes[i].mode) },
                                modifier = Modifier.weight(1f),
                                textPrimaryColor = textPrimaryColor,
                                textSecondaryColor = textSecondaryColor
                            )
                            if (i + 1 < themes.size) {
                                ThemeCardCell(
                                    item = themes[i + 1],
                                    isSelected = (viewModel.themeMode.value == themes[i + 1].mode),
                                    onClick = { viewModel.changeThemeMode(themes[i + 1].mode) },
                                    modifier = Modifier.weight(1f),
                                    textPrimaryColor = textPrimaryColor,
                                    textSecondaryColor = textSecondaryColor
                                )
                            }
                        }
                        if (i + 2 < themes.size) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Automation presets section
            Text(
                "Automation Scraper Presets",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = textPrimaryColor,
                modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(cardBorderModifier)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = cardBgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Switch 1: Decouple Watermark
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "HD Original Video (No Watermark)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = textPrimaryColor
                            )
                            Text(
                                "Bypasses the standard TikTok compression overlays using native Chromium scraping.",
                                fontSize = 11.sp,
                                color = textSecondaryColor
                            )
                        }
                        Switch(
                            checked = viewModel.isWatermarkDisabled.value,
                            onCheckedChange = { viewModel.toggleWatermark(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = if (themeMode == ThemeMode.LIGHT) GarTikPrimary else if (themeMode == ThemeMode.MATERIAL_YOU) MaterialTheme.colorScheme.primary else Color(0xFF38BDF8),
                                uncheckedThumbColor = Color(0xFF94A3B8),
                                uncheckedTrackColor = if (themeMode == ThemeMode.LIGHT || themeMode == ThemeMode.MATERIAL_YOU) Color(0xFFE2E8F0) else Color(0xFF1E293B)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = dividerLineColor)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Switch 2: Custom proxy nodes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Use Dedicated Scraper Proxy",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = textPrimaryColor
                            )
                            Text(
                                "Redirect headless browser requests via routing nodes to avoid captcha flags.",
                                fontSize = 11.sp,
                                color = textSecondaryColor
                            )
                        }
                        Switch(
                            checked = viewModel.isProxyEnabled.value,
                            onCheckedChange = { viewModel.toggleProxy(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = if (themeMode == ThemeMode.LIGHT) GarTikPrimary else if (themeMode == ThemeMode.MATERIAL_YOU) MaterialTheme.colorScheme.primary else Color(0xFF38BDF8),
                                uncheckedThumbColor = Color(0xFF94A3B8),
                                uncheckedTrackColor = if (themeMode == ThemeMode.LIGHT || themeMode == ThemeMode.MATERIAL_YOU) Color(0xFFE2E8F0) else Color(0xFF1E293B)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // JS Scraper Integration Block
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Code,
                        contentDescription = "Code icon scraper editor",
                        tint = if (themeMode == ThemeMode.LIGHT) GarTikSecondary else if (themeMode == ThemeMode.MATERIAL_YOU) MaterialTheme.colorScheme.secondary else Color(0xFF38BDF8),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Script: tiktok-scraper.js",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = textPrimaryColor
                    )
                }

                Text(
                    text = "Reset Default",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (themeMode == ThemeMode.LIGHT) GarTikPrimary else if (themeMode == ThemeMode.MATERIAL_YOU) MaterialTheme.colorScheme.primary else Color(0xFFFE2C55),
                    modifier = Modifier
                        .clickable {
                            viewModel.resetScriptToDefault()
                            Toast.makeText(context, "Scraper script reset to default!", Toast.LENGTH_SHORT).show()
                        }
                        .padding(4.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(cardBorderModifier)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = cardBgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "You can edit or paste your custom JavaScript file scraping source below. This script will be run dynamically in the download pipelines:",
                        fontSize = 11.sp,
                        color = textSecondaryColor,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = viewModel.scraperScriptCode.value,
                        onValueChange = { viewModel.scraperScriptCode.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = Color(0xFFE2E8F0)
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0F172A),
                            unfocusedContainerColor = Color(0xFF0F172A),
                            focusedBorderColor = if (themeMode == ThemeMode.LIGHT) GarTikSecondary else Color(0xFF00ADB5),
                            unfocusedBorderColor = Color(0xFF1E293B)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.saveScript(viewModel.scraperScriptCode.value)
                            Toast.makeText(context, "Scraper modifications saved!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (themeMode == ThemeMode.LIGHT) GarTikSecondary else if (themeMode == ThemeMode.MATERIAL_YOU) MaterialTheme.colorScheme.secondary else Color(0xFF38BDF8),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = "Save scraper scripts element config icon",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Scraper Script", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Architectural info card (.dll specifications)
            val infoBgColor = when(themeMode) {
                ThemeMode.LIGHT, ThemeMode.MATERIAL_YOU -> Color(0xFFEFF6FF)
                else -> Color(0x113B82F6)
            }
            val infoBorderColor = when(themeMode) {
                ThemeMode.LIGHT, ThemeMode.MATERIAL_YOU -> Color(0xFFDBEAFE)
                else -> Color(0x333B82F6)
            }
            val infoTextColor = when(themeMode) {
                ThemeMode.LIGHT, ThemeMode.MATERIAL_YOU -> Color(0xFF1E40AF)
                else -> Color(0xFF60A5FA)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = infoBgColor),
                border = BorderStroke(1.dp, infoBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Architecture documentation element info",
                        tint = infoTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Architecture & DLL Extensions",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = infoTextColor
                        )
                        Text(
                            "GarTik utilizes external scripts as core extraction layers. This lets you import and run javascript scenarios on client devices dynamically. DLL extension parameters act as bridges for re-encoding scales and multi-thread downloads on high-bandwidth channels. Ensure your customized scrapers return JSON payload metadata to maintain UI synchronization.",
                            fontSize = 10.sp,
                            color = infoTextColor,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

// Data holder helper class
data class ThemeChoiceItem(
    val mode: ThemeMode,
    val title: String,
    val desc: String,
    val previewAccent: Color
)

@Composable
fun ThemeCardCell(
    item: ThemeChoiceItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textPrimaryColor: Color,
    textSecondaryColor: Color
) {
    val activeBorderColor = when (item.mode) {
        ThemeMode.LIGHT, ThemeMode.DARK -> GarTikPrimary
        ThemeMode.MATERIAL_YOU -> Color(0xFF8B5CF6)
        ThemeMode.FROSTED_GLASS -> Color(0xFFFE2C55)
        ThemeMode.GRADIENT_GLASS -> Color(0xFFEC4899)
        ThemeMode.LIQUID_GLASS -> Color(0xFF00ADB5)
    }

    val cellBg = if (isSelected) {
        when(LocalThemeMode.current) {
            ThemeMode.LIGHT, ThemeMode.MATERIAL_YOU -> Color(0xFFF1F5F9)
            else -> Color(0x3DFFFFFF)
        }
    } else Color.Transparent

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(115.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(cellBg)
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, activeBorderColor, RoundedCornerShape(12.dp))
                } else {
                    Modifier.border(1.dp, textSecondaryColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                }
            )
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Small color circle accent
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(item.previewAccent)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = textPrimaryColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.desc,
                    fontSize = 9.sp,
                    color = textSecondaryColor.copy(alpha = 0.9f),
                    lineHeight = 12.sp
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Selected selection indicator icon",
                    tint = activeBorderColor,
                    modifier = Modifier.size(16.dp).padding(start = 2.dp)
                )
            }
        }
    }
}
