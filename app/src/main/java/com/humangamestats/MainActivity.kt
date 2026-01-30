package com.humangamestats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.humangamestats.data.datastore.SettingsDataStore
import com.humangamestats.data.datastore.ThemeMode
import com.humangamestats.ui.navigation.NavGraph
import com.humangamestats.ui.theme.AppColors
import com.humangamestats.ui.theme.HumanGameStatsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main activity that hosts the Compose UI.
 * Single activity architecture with Navigation Compose for screen navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var settingsDataStore: SettingsDataStore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Check for deep link stat ID from widget
        val initialStatId = intent?.getLongExtra("stat_id", -1L)?.takeIf { it != -1L }
        
        setContent {
            // Collect theme settings
            val themeMode by settingsDataStore.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val useDynamicColors by settingsDataStore.useDynamicColors.collectAsState(initial = true)
            val gradientStartColor by settingsDataStore.gradientStartColor.collectAsState(initial = SettingsDataStore.DEFAULT_GRADIENT_START)
            val gradientEndColor by settingsDataStore.gradientEndColor.collectAsState(initial = SettingsDataStore.DEFAULT_GRADIENT_END)
            val iconAccentColor by settingsDataStore.iconAccentColor.collectAsState(initial = SettingsDataStore.DEFAULT_ICON_ACCENT)
            
            // Determine dark theme based on mode
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            
            // Create AppColors from settings
            val appColors = AppColors.fromLongs(
                gradientStart = gradientStartColor,
                gradientEnd = gradientEndColor,
                iconAccent = iconAccentColor
            )
            
            HumanGameStatsTheme(
                darkTheme = darkTheme,
                dynamicColor = useDynamicColors,
                appColors = appColors
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(initialStatId = initialStatId)
                }
            }
        }
    }
}
