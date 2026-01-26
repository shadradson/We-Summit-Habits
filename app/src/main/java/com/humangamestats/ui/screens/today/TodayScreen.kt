package com.humangamestats.ui.screens.today

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humangamestats.model.DataPoint
import com.humangamestats.model.StatRecord
import com.humangamestats.model.StatType
import com.humangamestats.model.StatWithSummary
import com.humangamestats.ui.theme.StatTypeCheckbox
import com.humangamestats.ui.theme.StatTypeDuration
import com.humangamestats.ui.theme.StatTypeNumber
import com.humangamestats.ui.theme.StatTypeRating
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Screen showing all stats that have record entries from today.
 */
@Composable
fun TodayScreen(
    onStatClick: (Long) -> Unit,
    viewModel: TodayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show error in snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            uiState.stats.isEmpty() -> {
                EmptyState(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                TodayStatsList(
                    stats = uiState.stats,
                    onStatClick = onStatClick
                )
            }
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TodayStatsList(
    stats: List<StatWithSummary>,
    onStatClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(stats, key = { it.stat.id }) { statWithSummary ->
            TodayStatCard(
                statWithSummary = statWithSummary,
                onClick = { onStatClick(statWithSummary.stat.id) }
            )
        }
    }
}

@Composable
private fun TodayStatCard(
    statWithSummary: StatWithSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stat = statWithSummary.stat
    // Use primary data point type for color
    val primaryType = stat.primaryDataPoint?.type ?: StatType.NUMBER
    val typeColor = when (primaryType) {
        StatType.NUMBER -> StatTypeNumber
        StatType.DURATION -> StatTypeDuration
        StatType.RATING -> StatTypeRating
        StatType.CHECKBOX -> StatTypeCheckbox
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stat type indicator
            Icon(
                imageVector = Icons.Default.ShowChart,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = typeColor
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Stat info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stat.name,
                    style = MaterialTheme.typography.titleMedium
                )
                // Show time of last entry
                statWithSummary.latestRecordedAt?.let { timestamp ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatTime(timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Latest values - show all data points separated by pipes
            val displayValue = formatAllValues(statWithSummary.latestValues, stat.dataPoints)
            if (displayValue.isNotEmpty()) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = displayValue,
                        style = MaterialTheme.typography.titleMedium,
                        color = typeColor
                    )
                    /*Text(
                        text = stat.dataPointsSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )*/
                }
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Today,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No entries today",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Stats with today's records will appear here",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Format timestamp as time (e.g., "2:30 PM").
 */
private fun formatTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

/**
 * Format stat value based on its type.
 */
private fun formatStatValue(value: String, statType: StatType): String {
    return when (statType) {
        StatType.NUMBER -> value.toDoubleOrNull()?.let {
            if (it == it.toLong().toDouble()) it.toLong().toString() else String.format("%.1f", it)
        } ?: value
        StatType.DURATION -> {
            val millis = value.toLongOrNull() ?: 0L
            StatRecord.formatDuration(millis)
        }
        StatType.RATING -> "★".repeat(value.toIntOrNull() ?: 0)
        StatType.CHECKBOX -> if (value == "true") "✓" else "✗"
    }
}

/**
 * Format all values from a record, separated by pipes.
 * Each value is formatted according to its data point type.
 */
private fun formatAllValues(values: List<String>, dataPoints: List<DataPoint>): String {
    if (values.isEmpty()) return ""
    
    return values.mapIndexed { index, value ->
        val dataPoint = dataPoints.getOrNull(index)
        if (dataPoint != null) {
            val formatted = formatStatValue(value, dataPoint.type)
            if (dataPoint.unit.isNotEmpty() && formatted.isNotEmpty()) {
                "$formatted ${dataPoint.unit}"
            } else {
                formatted
            }
        } else {
            value
        }
    }.filter { it.isNotEmpty() }.joinToString(" | ")
}
