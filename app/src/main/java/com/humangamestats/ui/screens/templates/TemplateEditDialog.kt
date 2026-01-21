package com.humangamestats.ui.screens.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.humangamestats.model.DataPoint
import com.humangamestats.model.DataPointTemplate
import com.humangamestats.model.StatType

/**
 * Dialog for creating or editing a data point template.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateEditDialog(
    template: DataPointTemplate,
    onDismiss: () -> Unit,
    onSave: (name: String, description: String, dataPoints: List<DataPoint>) -> Unit
) {
    var name by remember { mutableStateOf(template.name) }
    var description by remember { mutableStateOf(template.description) }
    val dataPoints = remember { mutableStateListOf(*template.dataPoints.toTypedArray()) }
    
    val isNew = template.id == 0L
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNew) "Create Template" else "Edit Template") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Template Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Template Name") },
                    placeholder = { Text("e.g., Workout Set") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    placeholder = { Text("e.g., Track weight and reps") },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Data Points Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Data Points",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    
                    TextButton(
                        onClick = {
                            dataPoints.add(DataPoint.number("Value ${dataPoints.size + 1}"))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Add")
                    }
                }
                
                // Data Points List
                dataPoints.forEachIndexed { index, dataPoint ->
                    DataPointEditor(
                        dataPoint = dataPoint,
                        index = index,
                        canMoveUp = index > 0,
                        canMoveDown = index < dataPoints.lastIndex,
                        canDelete = dataPoints.size > 1,
                        onUpdate = { updatedDataPoint ->
                            dataPoints[index] = updatedDataPoint
                        },
                        onMoveUp = {
                            if (index > 0) {
                                val temp = dataPoints[index - 1]
                                dataPoints[index - 1] = dataPoints[index]
                                dataPoints[index] = temp
                            }
                        },
                        onMoveDown = {
                            if (index < dataPoints.lastIndex) {
                                val temp = dataPoints[index + 1]
                                dataPoints[index + 1] = dataPoints[index]
                                dataPoints[index] = temp
                            }
                        },
                        onDelete = {
                            if (dataPoints.size > 1) {
                                dataPoints.removeAt(index)
                            }
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, description, dataPoints.toList()) },
                enabled = name.isNotBlank() && dataPoints.isNotEmpty()
            ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DataPointEditor(
    dataPoint: DataPoint,
    index: Int,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    canDelete: Boolean,
    onUpdate: (DataPoint) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    var typeExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header with index and action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${index + 1}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row {
                    IconButton(
                        onClick = onMoveUp,
                        enabled = canMoveUp
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Move up",
                            tint = if (canMoveUp) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            }
                        )
                    }
                    
                    IconButton(
                        onClick = onMoveDown,
                        enabled = canMoveDown
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Move down",
                            tint = if (canMoveDown) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            }
                        )
                    }
                    
                    IconButton(
                        onClick = onDelete,
                        enabled = canDelete
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = if (canDelete) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
                            }
                        )
                    }
                }
            }
            
            // Label field
            OutlinedTextField(
                value = dataPoint.label,
                onValueChange = { onUpdate(dataPoint.copy(label = it)) },
                label = { Text("Label") },
                placeholder = { Text("e.g., Weight") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Type dropdown
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = dataPoint.type.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        StatType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    onUpdate(dataPoint.copy(type = type))
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }
                
                // Unit field (only for numeric types)
                if (dataPoint.type == StatType.NUMBER || dataPoint.type == StatType.DURATION) {
                    OutlinedTextField(
                        value = dataPoint.unit,
                        onValueChange = { onUpdate(dataPoint.copy(unit = it)) },
                        label = { Text("Unit") },
                        placeholder = { Text("e.g., lbs") },
                        singleLine = true,
                        modifier = Modifier.weight(0.7f)
                    )
                }
            }
            
            // Number-specific options
            if (dataPoint.type == StatType.NUMBER) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = dataPoint.minValue?.toString() ?: "",
                        onValueChange = { 
                            onUpdate(dataPoint.copy(minValue = it.toDoubleOrNull()))
                        },
                        label = { Text("Min") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = dataPoint.maxValue?.toString() ?: "",
                        onValueChange = { 
                            onUpdate(dataPoint.copy(maxValue = it.toDoubleOrNull()))
                        },
                        label = { Text("Max") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = dataPoint.step?.toString() ?: "",
                        onValueChange = { 
                            onUpdate(dataPoint.copy(step = it.toDoubleOrNull()))
                        },
                        label = { Text("Step") },
                        placeholder = { Text("5") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
