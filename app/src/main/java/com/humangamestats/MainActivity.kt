package com.humangamestats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.humangamestats.ui.navigation.NavGraph
import com.humangamestats.ui.theme.HumanGameStatsTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity that hosts the Compose UI.
 * Single activity architecture with Navigation Compose for screen navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Check for deep link stat ID from widget
        val initialStatId = intent?.getLongExtra("stat_id", -1L)?.takeIf { it != -1L }
        
        setContent {
            HumanGameStatsTheme {
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
