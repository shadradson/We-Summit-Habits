package com.humangamestats.ui.screens.stat

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humangamestats.R
import com.humangamestats.model.DataPoint
import com.humangamestats.model.DataPointTemplate
import com.humangamestats.model.StatType
import com.humangamestats.ui.theme.StatTypeCheckbox
import com.humangamestats.ui.theme.StatTypeDuration
import com.humangamestats.ui.theme.StatTypeNumber
import com.humangamestats.ui.theme.StatTypeRating

/**
 * Screen for creating or editing a stat.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StatFormScreen(
    categoryId: Long,
    statId: Long?,
    onBackClick: () -> Unit,
    onSaveComplete: () -> Unit,
    onDeleteComplete: () -> Unit = {},
    viewModel: StatFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle save completion
    LaunchedEffect(uiState.saveComplete) {
        if (uiState.saveComplete) {
            onSaveComplete()
        }
    }
    
    // Handle delete completion
    LaunchedEffect(uiState.deleteComplete) {
        if (uiState.deleteComplete) {
            onDeleteComplete()
        }
    }
    
    // Show error in snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }
    
    // Data point add/edit dialog
    if (uiState.showDataPointDialog) {
        DataPointDialog(
            isEditing = uiState.editingDataPointIndex != null,
            label = uiState.dialogDataPointLabel,
            type = uiState.dialogDataPointType,
            unit = uiState.dialogDataPointUnit,
            onLabelChange = viewModel::updateDialogLabel,
            onTypeChange = viewModel::updateDialogType,
            onUnitChange = viewModel::updateDialogUnit,
            onDismiss = viewModel::dismissDataPointDialog,
            onSave = viewModel::saveDataPoint
        )
    }
    
    // Delete data point confirmation dialog
    val deleteIndex = uiState.dataPointToDeleteIndex
    if (uiState.showDeleteConfirmation && deleteIndex != null) {
        val dataPointToDelete = uiState.dataPoints.getOrNull(deleteIndex)
        AlertDialog(
            onDismissRequest = viewModel::hideDeleteDataPointConfirmation,
            title = { Text("Delete Data Point") },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to delete \"${dataPointToDelete?.label ?: "this data point"}\"?"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Warning: Existing records will no longer show data for this field.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = viewModel::confirmDeleteDataPoint
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = viewModel::hideDeleteDataPointConfirmation
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Stat delete confirmation dialog
    if (uiState.showStatDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = viewModel::hideStatDeleteConfirmation,
            title = { Text("Delete Stat") },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to delete \"${uiState.name}\"?"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Warning: All records for this stat will also be permanently deleted.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = viewModel::deleteStat
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = viewModel::hideStatDeleteConfirmation
                ) {
                    Text("Cancel")
                }
            }
        )
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
            )
            {
                TopAppBar(
                    title = {
                        Text(
                            text = if (uiState.isEditing) {
                                stringResource(R.string.edit_stat)
                            } else {
                                stringResource(R.string.add_stat)
                            }
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        //containerColor = MaterialTheme.colorScheme.primaryContainer,
                        //titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        //navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
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
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    // Stat Name
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = { viewModel.updateName(it) },
                        label = { Text(stringResource(R.string.stat_name)) },
                        placeholder = { Text("e.g., Bench Press") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Template Selection
                    if (uiState.templates.isNotEmpty()) {
                        /*Text(
                            text = "Use Template (optional)",
                            style = MaterialTheme.typography.titleMedium
                        )*/
                        
                        //Spacer(modifier = Modifier.height(8.dp))
                        
                        TemplateSelector(
                            templates = uiState.templates,
                            selectedTemplateId = uiState.selectedTemplateId,
                            onTemplateSelected = { template ->
                                if (template != null) {
                                    viewModel.applyTemplate(template)
                                } else {
                                    viewModel.clearTemplate()
                                }
                            }
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    // Data Points Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Data Points",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        TextButton(onClick = { viewModel.showAddDataPointDialog() }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Data Points List
                    uiState.dataPoints.forEachIndexed { index, dataPoint ->
                        DataPointCard(
                            dataPoint = dataPoint,
                            index = index,
                            total = uiState.dataPoints.size,
                            onEdit = { viewModel.showEditDataPointDialog(index) },
                            onDelete = { viewModel.removeDataPoint(index) },
                            onMoveUp = { viewModel.moveDataPointUp(index) },
                            onMoveDown = { viewModel.moveDataPointDown(index) },
                            canDelete = uiState.dataPoints.size > 1
                        )
                        
                        if (index < uiState.dataPoints.size - 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Save Button
                    Button(
                        onClick = { viewModel.save() },
                        enabled = !uiState.isSaving && !uiState.isDeleting,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(stringResource(R.string.save))
                    }
                    
                    // Delete Button (only shown when editing)
                    if (uiState.isEditing) {
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        TextButton(
                            onClick = viewModel::showStatDeleteConfirmation,
                            enabled = !uiState.isSaving && !uiState.isDeleting,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (uiState.isDeleting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Delete Stat",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplateSelector(
    templates: List<DataPointTemplate>,
    selectedTemplateId: Long?,
    onTemplateSelected: (DataPointTemplate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedTemplate = templates.find { it.id == selectedTemplateId }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedTemplate?.name ?: "Custom (no template)",
            onValueChange = {},
            readOnly = true,
            label = { Text("Template") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Custom (no template)") },
                onClick = {
                    onTemplateSelected(null)
                    expanded = false
                }
            )
            
            templates.forEach { template ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = template.name,
                                fontWeight = FontWeight.Medium
                            )
                            if (template.description.isNotEmpty()) {
                                Text(
                                    text = template.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = template.dataPoints.joinToString(", ") { it.label },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    onClick = {
                        onTemplateSelected(template)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DataPointCard(
    dataPoint: DataPoint,
    index: Int,
    total: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    canDelete: Boolean,
    modifier: Modifier = Modifier
) {
    val typeColor = when (dataPoint.type) {
        StatType.NUMBER -> StatTypeNumber
        StatType.DURATION -> StatTypeDuration
        StatType.RATING -> StatTypeRating
        StatType.CHECKBOX -> StatTypeCheckbox
    }
    
    val typeIcon = when (dataPoint.type) {
        StatType.NUMBER -> Icons.Default.Numbers
        StatType.DURATION -> Icons.Default.Timer
        StatType.RATING -> Icons.Default.Star
        StatType.CHECKBOX -> Icons.Default.CheckBox
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = typeIcon,
                    contentDescription = null,
                    tint = typeColor,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = dataPoint.label,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Row {
                        Text(
                            text = dataPoint.type.displayName,
                            style = MaterialTheme.typography.bodySmall,
                            color = typeColor
                        )
                        if (dataPoint.unit.isNotEmpty()) {
                            Text(
                                text = " • ${dataPoint.unit}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Row {
                // Move up/down buttons
                if (total > 1) {
                    IconButton(
                        onClick = onMoveUp,
                        enabled = index > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Move up",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    IconButton(
                        onClick = onMoveDown,
                        enabled = index < total - 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Move down",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                IconButton(
                    onClick = onDelete,
                    enabled = canDelete
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(20.dp),
                        tint = if (canDelete) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DataPointDialog(
    isEditing: Boolean,
    label: String,
    type: StatType,
    unit: String,
    onLabelChange: (String) -> Unit,
    onTypeChange: (StatType) -> Unit,
    onUnitChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEditing) "Edit Data Point" else "Add Data Point")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = label,
                    onValueChange = onLabelChange,
                    label = { Text("Label") },
                    placeholder = { Text("e.g., Weight, Reps, Duration") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Type",
                    style = MaterialTheme.typography.labelMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatType.entries.forEach { statType ->
                        StatTypeChip(
                            statType = statType,
                            isSelected = type == statType,
                            onClick = { onTypeChange(statType) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = unit,
                    onValueChange = onUnitChange,
                    label = { Text("Unit (optional)") },
                    placeholder = { Text("e.g., lbs, kg, miles") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun StatTypeChip(
    statType: StatType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typeColor = when (statType) {
        StatType.NUMBER -> StatTypeNumber
        StatType.DURATION -> StatTypeDuration
        StatType.RATING -> StatTypeRating
        StatType.CHECKBOX -> StatTypeCheckbox
    }
    
    val icon = when (statType) {
        StatType.NUMBER -> Icons.Default.Numbers
        StatType.DURATION -> Icons.Default.Timer
        StatType.RATING -> Icons.Default.Star
        StatType.CHECKBOX -> Icons.Default.CheckBox
    }
    
    Row(
        modifier = modifier
            //.clip(RoundedCornerShape(0.dp))
            .clip(RectangleShape)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = typeColor,
                        //shape = RoundedCornerShape(0.dp)
                        shape = RectangleShape
                    )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        //shape = RoundedCornerShape(0.dp)
                        shape = RectangleShape
                    )
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = if (isSelected) typeColor else MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.width(4.dp))
        
        Text(
            text = statType.displayName,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) typeColor else MaterialTheme.colorScheme.onSurface
        )
    }
}
