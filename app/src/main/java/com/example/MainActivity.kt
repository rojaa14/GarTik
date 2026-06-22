package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.GarTikPrimary
import com.example.ui.theme.GarTikSecondary
import com.example.ui.theme.GarTikTertiary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.LocalThemeMode
import com.example.ui.viewmodel.GarTikViewModel
import com.example.data.ThemeMode
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val viewModel = remember { GarTikViewModel(context) }
            MyApplicationTheme(themeMode = viewModel.themeMode.value) {
                MainAppEntry(viewModel)
            }
        }
    }
}

@Composable
fun ThemeDecoratedBackdrop(themeMode: ThemeMode, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                when (themeMode) {
                    ThemeMode.LIGHT -> Color(0xFFF8FAFC)
                    ThemeMode.DARK -> Color(0xFF0F172A)
                    ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.background
                    ThemeMode.FROSTED_GLASS -> Color(0xFF090D16)
                    ThemeMode.GRADIENT_GLASS -> Color(0xFF150428)
                    ThemeMode.LIQUID_GLASS -> Color(0xFF020714)
                }
            )
    ) {
        // Custom decorative high-fidelity glowing graphics for glass modes
        if (themeMode == ThemeMode.FROSTED_GLASS) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x90FE2C55), Color.Transparent),
                        center = Offset(size.width * 0.1f, size.height * 0.2f),
                        radius = size.width * 0.8f
                    ),
                    center = Offset(size.width * 0.1f, size.height * 0.2f),
                    radius = size.width * 0.8f
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x7000ADB5), Color.Transparent),
                        center = Offset(size.width * 0.9f, size.height * 0.8f),
                        radius = size.width * 0.9f
                    ),
                    center = Offset(size.width * 0.9f, size.height * 0.8f),
                    radius = size.width * 0.9f
                )
            }
        } else if (themeMode == ThemeMode.GRADIENT_GLASS) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Diagonal multi-stop energetic background linear gradient
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF3F2B96), // Deep Royal Purple
                            Color(0xFF8B5CF6), // Cool Orchid Violet
                            Color(0xFFEC4899)  // Vibrant Bright Pink
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height)
                    )
                )
                // Center-bottom golden-orange sunburst glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x66F59E0B), Color.Transparent),
                        center = Offset(size.width * 0.5f, size.height * 0.6f),
                        radius = size.width * 0.8f
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.6f),
                    radius = size.width * 0.8f
                )
            }
        } else if (themeMode == ThemeMode.LIQUID_GLASS) {
            // Cool water cyan/emerald/aquamarine organic blob design
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x80059669), Color.Transparent), // Emerald
                        center = Offset(size.width * 0.25f, size.height * 0.35f),
                        radius = size.width * 0.65f
                    ),
                    center = Offset(size.width * 0.25f, size.height * 0.35f),
                    radius = size.width * 0.65f
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x802563EB), Color.Transparent), // Royal Blue
                        center = Offset(size.width * 0.75f, size.height * 0.75f),
                        radius = size.width * 0.75f
                    ),
                    center = Offset(size.width * 0.75f, size.height * 0.75f),
                    radius = size.width * 0.75f
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x66EC4899), Color.Transparent), // Liquid Violet-pink
                        center = Offset(size.width * 0.85f, size.height * 0.15f),
                        radius = size.width * 0.45f
                    ),
                    center = Offset(size.width * 0.85f, size.height * 0.15f),
                    radius = size.width * 0.45f
                )
            }
        }
        content()
    }
}

@Composable
fun MainAppEntry(viewModel: GarTikViewModel) {
    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf("HOME") } // "HOME" or "SETTINGS"

    if (showSplash) {
        SplashScreen(onTimeout = { showSplash = false })
    } else {
        val themeMode = LocalThemeMode.current
        
        // Define dynamic styles based on theme
        val scaffoldBg = when (themeMode) {
            ThemeMode.LIGHT -> Color(0xFFF8FAFC)
            ThemeMode.DARK -> Color(0xFF0F172A)
            ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.background
            else -> Color.Transparent // Show glowing canvas behind glass modes
        }

        val navBg = when (themeMode) {
            ThemeMode.LIGHT -> Color.White
            ThemeMode.DARK -> Color(0xFF1E293B)
            ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.surface
            ThemeMode.FROSTED_GLASS -> Color(0x3D111827)
            ThemeMode.GRADIENT_GLASS -> Color(0x3D0F051D)
            ThemeMode.LIQUID_GLASS -> Color(0x3D020714)
        }

        val indColor = when (themeMode) {
            ThemeMode.LIGHT -> Color(0xFFFFF1F2)
            ThemeMode.DARK -> Color(0xFF334155)
            ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.primaryContainer
            ThemeMode.FROSTED_GLASS -> Color(0x4DFFFFFF)
            ThemeMode.GRADIENT_GLASS -> Color(0x52E0F2FE)
            ThemeMode.LIQUID_GLASS -> Color(0x4D00ADB5)
        }

        val actIconText = when (themeMode) {
            ThemeMode.LIGHT -> GarTikPrimary
            ThemeMode.DARK -> GarTikPrimary
            ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.primary
            ThemeMode.FROSTED_GLASS -> Color(0xFFFE2C55)
            ThemeMode.GRADIENT_GLASS -> Color(0xFFEC4899)
            ThemeMode.LIQUID_GLASS -> Color(0xFF00ADB5)
        }

        val unselectedIconText = when (themeMode) {
            ThemeMode.LIGHT -> Color(0xFF64748B)
            ThemeMode.DARK -> Color(0xFF94A3B8)
            ThemeMode.MATERIAL_YOU -> MaterialTheme.colorScheme.onSurfaceVariant
            else -> Color(0xFF94A3B8)
        }

        ThemeDecoratedBackdrop(themeMode = themeMode) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = scaffoldBg,
                bottomBar = {
                    NavigationBar(
                        containerColor = navBg,
                        tonalElevation = if (themeMode == ThemeMode.LIGHT) 8.dp else 0.dp,
                        modifier = if (themeMode != ThemeMode.LIGHT && themeMode != ThemeMode.DARK && themeMode != ThemeMode.MATERIAL_YOU) {
                            Modifier.border(0.5.dp, Color(0x2BFFFFFF), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        } else Modifier
                    ) {
                        NavigationBarItem(
                            selected = (currentScreen == "HOME"),
                            onClick = { currentScreen = "HOME" },
                            icon = {
                                Icon(
                                    Icons.Default.Download,
                                    contentDescription = "Downloader app main dashboard"
                                )
                            },
                            label = {
                                Text(
                                    "Downloader",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = actIconText,
                                selectedTextColor = actIconText,
                                unselectedIconColor = unselectedIconText,
                                unselectedTextColor = unselectedIconText,
                                indicatorColor = indColor
                            )
                        )

                        NavigationBarItem(
                            selected = (currentScreen == "SETTINGS"),
                            onClick = { currentScreen = "SETTINGS" },
                            icon = {
                                Icon(
                                    Icons.Default.Code,
                                    contentDescription = "Scraper js visual code editor setup"
                                )
                            },
                            label = {
                                Text(
                                    "JS Developer",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = if (themeMode == ThemeMode.LIGHT) GarTikTertiary else actIconText,
                                selectedTextColor = if (themeMode == ThemeMode.LIGHT) GarTikTertiary else actIconText,
                                unselectedIconColor = unselectedIconText,
                                unselectedTextColor = unselectedIconText,
                                indicatorColor = indColor
                            )
                        )
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (currentScreen) {
                        "HOME" -> HomeScreen(viewModel)
                        "SETTINGS" -> SettingsScreen(viewModel)
                    }
                }
            }
        }
    }
}
