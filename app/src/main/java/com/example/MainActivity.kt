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
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.GarTikPrimary
import com.example.ui.theme.GarTikTertiary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.GarTikViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppEntry()
            }
        }
    }
}

@Composable
fun MainAppEntry() {
    val context = LocalContext.current
    val viewModel = remember { GarTikViewModel(context) }
    
    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf("HOME") } // "HOME" or "SETTINGS"

    if (showSplash) {
        SplashScreen(onTimeout = { showSplash = false })
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
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
                                style = androidx.compose.ui.text.TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GarTikPrimary,
                            selectedTextColor = GarTikPrimary,
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B),
                            indicatorColor = Color(0xFFFFF1F2)
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
                                style = androidx.compose.ui.text.TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GarTikTertiary,
                            selectedTextColor = GarTikTertiary,
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B),
                            indicatorColor = Color(0xFFF1F5F9)
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
