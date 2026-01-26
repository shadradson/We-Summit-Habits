package com.humangamestats.ui.screens.categories

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humangamestats.R
import com.humangamestats.model.DataPoint
import com.humangamestats.model.StatCategory
import com.humangamestats.model.StatRecord
import com.humangamestats.model.StatType
import com.humangamestats.model.StatWithSummary
import com.humangamestats.ui.screens.today.TodayViewModel
import com.humangamestats.ui.theme.StatTypeCheckbox
import com.humangamestats.ui.theme.StatTypeDuration
import com.humangamestats.ui.theme.StatTypeNumber
import com.humangamestats.ui.theme.StatTypeRating
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Home screen displaying all stat categories.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onCategoryClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onStatClick: (Long) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel(),
    todayViewModel: TodayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val todayUiState by todayViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    // Show error in snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }
    
    LaunchedEffect(todayUiState.error) {
        todayUiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            todayViewModel.clearError()
        }
    }
    
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF6200EE),  // Start color (purple)
                                Color(0xFF3700B3)   // End color (darker purple)
                            )
                        )
                    )
            ) {
            Column {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        //containerColor = MaterialTheme.colorScheme.primaryContainer,
                        containerColor = Color.Transparent,
                        //titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        titleContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = onSettingsClick) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(R.string.settings),
                                tint = Color.White
                            )
                        }
                    }
                )
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    //containerColor = MaterialTheme.colorScheme.primaryContainer,
                    containerColor = Color.Transparent,
                    //contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    contentColor = Color.White
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("Categories") },
                        icon = { Icon(Icons.Default.Category, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("Today") },
                        icon = { Icon(Icons.Default.Today, contentDescription = null) }
                    )
                }
            }
            }
        },
        floatingActionButton = {
            if (selectedTabIndex == 0) {
                FloatingActionButton(
                    onClick = { viewModel.showCreateCategoryDialog() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_category)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTabIndex) {
                0 -> {
                    // Categories tab
                    when {
                        uiState.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        uiState.categories.isEmpty() -> {
                            EmptyState(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        else -> {
                            CategoriesGrid(
                                categories = uiState.categories,
                                onCategoryClick = onCategoryClick
                            )
                        }
                    }
                }
                1 -> {
                    // Today tab
                    when {
                        todayUiState.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        todayUiState.stats.isEmpty() -> {
                            TodayEmptyState(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        else -> {
                            TodayStatsList(
                                stats = todayUiState.stats,
                                onStatClick = onStatClick
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Category dialog
    if (uiState.showCategoryDialog) {
        CategoryDialog(
            category = uiState.editingCategory,
            onDismiss = { viewModel.hideCategoryDialog() },
            onSave = { title, icon -> viewModel.saveCategory(title, icon) }
        )
    }
    
    // Delete confirmation dialog
    uiState.categoryToDelete?.let { category ->
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteConfirmation() },
            title = { Text(stringResource(R.string.delete_category)) },
            text = { Text(stringResource(R.string.delete_category_confirm)) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.deleteCategory(category) }
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
private fun CategoriesGrid(
    categories: List<StatCategory>,
    onCategoryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(categories, key = { it.id }) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category.id) }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    category: StatCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = getCategoryIcon(category.icon),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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
            imageVector = Icons.Default.Category,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Tap + to create your first category",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TodayEmptyState(modifier: Modifier = Modifier) {
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
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
                        style = MaterialTheme.typography.titleLarge,
                        color = typeColor
                    )
                    Text(
                        text = stat.dataPointsSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
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
 * Format stat value based on its type for Today tab.
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

/**
 * Get the appropriate icon for a category based on its icon name.
 */
fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName) {
        "fitness_center" -> Icons.Default.FitnessCenter
        "restaurant" -> Icons.Default.Restaurant
        "bedtime" -> Icons.Default.Bedtime
        "mood" -> Icons.Default.Mood
        "water_drop" -> Icons.Default.WaterDrop
        "directions_run" -> Icons.Default.DirectionsRun
        "self_improvement" -> Icons.Default.SelfImprovement
        "medication" -> Icons.Default.Medication
        "work" -> Icons.Default.Work
        "school" -> Icons.Default.School
        "attach_money" -> Icons.Default.AttachMoney
        "favorite" -> Icons.Default.Favorite
        "star" -> Icons.Default.Star
        else -> Icons.Default.Category
    }
}
