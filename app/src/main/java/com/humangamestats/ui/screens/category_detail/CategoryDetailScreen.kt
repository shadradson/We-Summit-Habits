package com.humangamestats.ui.screens.category_detail

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.humangamestats.model.SortOption
import com.humangamestats.model.StatType
import com.humangamestats.model.StatWithSummary
import com.humangamestats.ui.screens.categories.CategoryDialog
import com.humangamestats.ui.theme.StatTypeCheckbox
import com.humangamestats.ui.theme.StatTypeDuration
import com.humangamestats.ui.theme.StatTypeNumber
import com.humangamestats.ui.theme.StatTypeRating

/**
 * Screen displaying all stats within a category.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    categoryId: Long,
    onBackClick: () -> Unit,
    onStatClick: (Long) -> Unit,
    onAddStatClick: () -> Unit,
    viewModel: CategoryDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSortMenu by remember { mutableStateOf(false) }
    var showCategoryEditDialog by remember { mutableStateOf(false) }
    
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
                        text = uiState.category?.title ?: stringResource(R.string.stats),
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
                    IconButton(onClick = { showCategoryEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_category)
                        )
                    }
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = stringResource(R.string.sort_by)
                            )
                        }
                        SortDropdownMenu(
                            expanded = showSortMenu,
                            currentSort = uiState.sortOption,
                            onDismiss = { showSortMenu = false },
                            onSortSelected = { sortOption ->
                                viewModel.setSortOption(sortOption)
                                showSortMenu = false
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
            FloatingActionButton(onClick = onAddStatClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_stat)
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
                uiState.stats.isEmpty() -> {
                    EmptyState(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    StatsList(
                        stats = uiState.stats,
                        onStatClick = onStatClick
                    )
                }
            }
        }
    }
    
    // Category edit dialog
    if (showCategoryEditDialog && uiState.category != null) {
        CategoryDialog(
            category = uiState.category,
            onDismiss = { showCategoryEditDialog = false },
            onSave = { title, icon ->
                viewModel.updateCategory(title, icon)
                showCategoryEditDialog = false
            },
            onDelete = {
                showCategoryEditDialog = false
                viewModel.deleteCategory(onBackClick)
            }
        )
    }
    
}

@Composable
private fun StatsList(
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
            StatCard(
                statWithSummary = statWithSummary,
                onClick = { onStatClick(statWithSummary.stat.id) }
            )
        }
    }
}

@Composable
private fun StatCard(
    statWithSummary: StatWithSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stat = statWithSummary.stat
    val typeColor = when (stat.statType) {
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stat.statType.displayName} • ${stat.typeLabel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (statWithSummary.recordCount > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${statWithSummary.recordCount} entries",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Latest value
            statWithSummary.latestValue?.let { value ->
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatStatValue(value, stat.statType),
                        style = MaterialTheme.typography.titleLarge,
                        color = typeColor
                    )
                    Text(
                        text = stat.typeLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SortDropdownMenu(
    expanded: Boolean,
    currentSort: SortOption,
    onDismiss: () -> Unit,
    onSortSelected: (SortOption) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        SortOption.entries.forEach { option ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = option.displayName,
                        color = if (option == currentSort) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                },
                onClick = { onSortSelected(option) }
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
            imageVector = Icons.Default.ShowChart,
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
            text = "Tap + to create your first stat",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
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
            com.humangamestats.model.StatRecord.formatDuration(millis)
        }
        StatType.RATING -> "★".repeat(value.toIntOrNull() ?: 0)
        StatType.CHECKBOX -> if (value == "true") "✓" else "✗"
    }
}
