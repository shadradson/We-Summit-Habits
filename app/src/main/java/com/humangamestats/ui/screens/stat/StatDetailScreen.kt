package com.humangamestats.ui.screens.stat

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humangamestats.R
import com.humangamestats.model.DataPoint
import com.humangamestats.model.Stat
import com.humangamestats.model.StatRecord
import com.humangamestats.model.StatType
import com.humangamestats.ui.components.StatChart
import com.humangamestats.ui.theme.StatTypeCheckbox
import com.humangamestats.ui.theme.StatTypeDuration
import com.humangamestats.ui.theme.StatTypeNumber
import com.humangamestats.ui.theme.StatTypeRating

/**
 * Screen displaying all records for a stat with chart visualization.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatDetailScreen(
    statId: Long,
    onBackClick: () -> Unit,
    onAddRecordClick: () -> Unit,
    onEditRecordClick: (Long) -> Unit,
    onEditStatClick: (categoryId: Long) -> Unit,
    viewModel: StatDetailViewModel = hiltViewModel()
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
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.stat?.name ?: stringResource(R.string.records),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { uiState.stat?.categoryId?.let { onEditStatClick(it) } }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_stat)
                        )
                    }
                    IconButton(onClick = { viewModel.toggleChart() }) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = stringResource(R.string.view_chart),
                            tint = if (uiState.showChart) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddRecordClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_record)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Statistics summary
                        uiState.statistics?.let { stats ->
                            item {
                                StatisticsSummary(
                                    stat = uiState.stat,
                                    statistics = stats,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        
                        // Chart
                        if (uiState.showChart && uiState.chartData.size >= 2) {
                            item {
                                StatChart(
                                    records = uiState.chartData,
                                    statType = uiState.stat?.statType ?: StatType.NUMBER,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            }
                        }
                        
                        // Empty state
                        if (uiState.records.isEmpty()) {
                            item {
                                EmptyState(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp)
                                )
                            }
                        }
                        
                        // Records list grouped by day
                        uiState.groupedRecords.forEach { dayGroup ->
                            item(key = "header_${dayGroup.date}") {
                                DayHeader(
                                    displayDate = dayGroup.displayDate,
                                    recordCount = dayGroup.records.size
                                )
                            }
                            
                            items(
                                items = dayGroup.records,
                                key = { it.id }
                            ) { record ->
                                RecordCard(
                                    record = record,
                                    stat = uiState.stat,
                                    onEditClick = { onEditRecordClick(record.id) },
                                    onDeleteClick = { viewModel.showDeleteConfirmation(record) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Delete confirmation dialog
    uiState.recordToDelete?.let { record ->
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteConfirmation() },
            title = { Text(stringResource(R.string.delete_record)) },
            text = { Text(stringResource(R.string.delete_record_confirm)) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.deleteRecord(record) }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.hideDeleteConfirmation() }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun StatisticsSummary(
    stat: Stat?,
    statistics: com.humangamestats.data.repository.StatStatistics,
    modifier: Modifier = Modifier
) {
    if (stat == null || statistics.totalCount == 0) return
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticItem(
                label = "Total",
                value = statistics.totalCount.toString()
            )
            
            if (stat.statType in listOf(StatType.NUMBER, StatType.DURATION)) {
                statistics.averageValue?.let { avg ->
                    StatisticItem(
                        label = "Avg",
                        value = formatValue(avg, stat.statType)
                    )
                }
                
                statistics.maxValue?.let { max ->
                    StatisticItem(
                        label = "Max",
                        value = formatValue(max, stat.statType)
                    )
                }
                
                statistics.minValue?.let { min ->
                    StatisticItem(
                        label = "Min",
                        value = formatValue(min, stat.statType)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
    }
}

/**
 * Day header showing the date and record count for a group of records.
 */
@Composable
private fun DayHeader(
    displayDate: String,
    recordCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayDate,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "($recordCount)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RecordCard(
    record: StatRecord,
    stat: Stat?,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typeColor = when (stat?.statType) {
        StatType.NUMBER -> StatTypeNumber
        StatType.DURATION -> StatTypeDuration
        StatType.RATING -> StatTypeRating
        StatType.CHECKBOX -> StatTypeCheckbox
        null -> MaterialTheme.colorScheme.primary
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Values - show up to 3 data points
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = formatRecordDataPoints(
                            record = record,
                            dataPoints = stat?.dataPoints ?: emptyList(),
                            maxPoints = 3
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = typeColor
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Action buttons
                Row {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit),
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            // Notes
            record.notes?.let { notes ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Location
            if (record.hasLocation) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = record.locationName ?: "${record.latitude}, ${record.longitude}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Tap + to add your first record",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Format up to [maxPoints] data points from a record for display.
 * Returns values separated by " | " (e.g., "135 lbs | 10 reps | 3 sets").
 * Uses ID-based lookup with fallback to index for legacy data.
 */
private fun formatRecordDataPoints(
    record: StatRecord,
    dataPoints: List<DataPoint>,
    maxPoints: Int = 3
): String {
    // If there's only one data point or no data points defined, use legacy format
    if (dataPoints.size <= 1) {
        val dp = dataPoints.firstOrNull()
        // Use ID-based lookup with index fallback
        val value = if (dp != null) {
            record.getValueForDataPoint(dp.id)
                ?: record.values.find { it.dataPointIndex == 0 }?.value
                ?: record.value
        } else {
            record.value
        }
        return formatSingleDataPoint(value, dp)
    }
    
    // Show up to maxPoints data points using ID-based lookup
    val pointsToShow = dataPoints.take(maxPoints)
    return pointsToShow.mapIndexed { index, dp ->
        // Use ID-based lookup with index fallback for legacy data
        val value = record.getValueForDataPoint(dp.id)
            ?: record.values.find { it.dataPointIndex == index }?.value
            ?: ""
        formatSingleDataPoint(value, dp)
    }.filter { it.isNotEmpty() }.joinToString(" | ")
}

/**
 * Format a single data point value for display.
 */
private fun formatSingleDataPoint(value: String, dataPoint: DataPoint?): String {
    if (value.isEmpty()) return ""
    
    val type = dataPoint?.type ?: StatType.NUMBER
    val unit = dataPoint?.unit ?: ""
    
    return when (type) {
        StatType.NUMBER -> {
            val num = value.toDoubleOrNull()
            val formatted = if (num != null) {
                if (num == num.toLong().toDouble()) num.toLong().toString()
                else String.format("%.2f", num)
            } else value
            if (unit.isNotEmpty()) "$formatted $unit" else formatted
        }
        StatType.DURATION -> StatRecord.formatDuration(value.toLongOrNull() ?: 0L)
        StatType.RATING -> {
            val rating = value.toIntOrNull() ?: 0
            "★".repeat(rating) + "☆".repeat(5 - rating)
        }
        StatType.CHECKBOX -> if (value == "true") "✓" else "✗"
    }
}

private fun formatRecordValue(value: String, statType: StatType): String {
    return when (statType) {
        StatType.NUMBER -> value.toDoubleOrNull()?.let {
            if (it == it.toLong().toDouble()) it.toLong().toString() else String.format("%.2f", it)
        } ?: value
        StatType.DURATION -> StatRecord.formatDuration(value.toLongOrNull() ?: 0L)
        StatType.RATING -> "★".repeat(value.toIntOrNull() ?: 0)
        StatType.CHECKBOX -> if (value == "true") "✓ Yes" else "✗ No"
    }
}

private fun formatValue(value: Double, statType: StatType): String {
    return when (statType) {
        StatType.NUMBER -> if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            String.format("%.1f", value)
        }
        StatType.DURATION -> StatRecord.formatDuration(value.toLong())
        else -> value.toString()
    }
}
