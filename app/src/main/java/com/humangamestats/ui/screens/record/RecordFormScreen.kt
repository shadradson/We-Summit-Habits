package com.humangamestats.ui.screens.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humangamestats.R
import com.humangamestats.model.DataPoint
import com.humangamestats.model.StatType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Screen for creating or editing a stat record.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordFormScreen(
    statId: Long,
    recordId: Long?,
    onBackClick: () -> Unit,
    onSaveComplete: () -> Unit,
    viewModel: RecordFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle save completion
    LaunchedEffect(uiState.saveComplete) {
        if (uiState.saveComplete) {
            onSaveComplete()
        }
    }
    
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
                        text = if (uiState.isEditing) {
                            stringResource(R.string.edit_record)
                        } else {
                            stringResource(R.string.add_record)
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
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
                        .padding(16.dp)
                ) {
                    // Stat info header
                    uiState.stat?.let { stat ->
                        Text(
                            text = stat.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        /*Text(
                            text = "${stat.dataPoints.size} data point${if (stat.dataPoints.size != 1) "s" else ""}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )*/
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Value inputs for each data point
                        stat.dataPoints.forEachIndexed { index, dataPoint ->
                            DataPointInput(
                                dataPoint = dataPoint,
                                value = uiState.values.getOrElse(index) { "" },
                                onValueChange = { viewModel.updateValue(index, it) }
                            )
                            
                            if (index < stat.dataPoints.size - 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Date and Time - Single button showing date & time
                    OutlinedButton(
                        onClick = { viewModel.showDateTimeDialog() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(formatDateTime(uiState.recordedAt))
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Notes
                    OutlinedTextField(
                        value = uiState.notes,
                        onValueChange = { viewModel.updateNotes(it) },
                        label = { Text(stringResource(R.string.record_notes)) },
                        placeholder = { Text("Add notes about this record...") },
                        minLines = 1,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Location
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (uiState.location != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = uiState.location?.locationName 
                                        ?: stringResource(R.string.location_captured),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${uiState.location?.latitude}, ${uiState.location?.longitude}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            TextButton(onClick = { viewModel.clearLocation() }) {
                                Text("Clear")
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = { viewModel.captureLocation() },
                            enabled = !uiState.isCapturingLocation,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (uiState.isCapturingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            } else {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                if (uiState.isCapturingLocation) {
                                    "Capturing..."
                                } else if (!viewModel.hasLocationPermission()) {
                                    stringResource(R.string.location_permission_required)
                                } else {
                                    "Capture Location"
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Save Button
                    Button(
                        onClick = { viewModel.save() },
                        enabled = !uiState.isSaving,
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
                }
            }
        }
    }
    
    // Date/Time picker dialog
    if (uiState.showDateTimeDialog) {
        DateTimePickerDialog(
            currentTimestamp = uiState.recordedAt,
            onDismiss = { viewModel.hideDateTimeDialog() },
            onDateTimeSelected = { timestamp ->
                viewModel.updateRecordedAt(timestamp)
            }
        )
    }
}

/**
 * Input component for a single data point.
 */
@Composable
private fun DataPointInput(
    dataPoint: DataPoint,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = dataPoint.label,
            style = MaterialTheme.typography.titleMedium
        )
        
        //Spacer(modifier = Modifier.height(2.dp))
        
        when (dataPoint.type) {
            StatType.NUMBER -> {
                NumberInputWithArrows(
                    value = value,
                    onValueChange = onValueChange,
                    //label = stringResource(R.string.record_value),
                    label = "",
                    minValue = dataPoint.minValue,
                    maxValue = dataPoint.maxValue,
                    step = dataPoint.step ?: 1.0
                )
            }
            
            StatType.DURATION -> {
                DurationInputWithArrows(
                    millis = value.toLongOrNull() ?: 0L,
                    onMillisChange = { onValueChange(it.toString()) }
                )
            }
            
            StatType.RATING -> {
                val rating = value.toIntOrNull() ?: 3
                Column {
                    Text(
                        text = "★".repeat(rating) + "☆".repeat(5 - rating),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Slider(
                        value = rating.toFloat(),
                        onValueChange = { onValueChange(it.toInt().toString()) },
                        valueRange = 1f..5f,
                        steps = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            StatType.CHECKBOX -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = value == "true",
                        onCheckedChange = { onValueChange(it.toString()) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (value == "true") "Checked!" else "Check this to enter.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

/**
 * Number input with up/down arrow buttons for incrementing/decrementing.
 */
@Composable
private fun NumberInputWithArrows(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    minValue: Double?,
    maxValue: Double?,
    step: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text("Enter value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = {
                    val currentValue = value.toDoubleOrNull() ?: 0.0
                    val newValue = currentValue + step
                    val constrainedValue = if (maxValue != null) {
                        newValue.coerceAtMost(maxValue)
                    } else {
                        newValue
                    }
                    // Format value (remove unnecessary decimals if whole number)
                    val formatted = if (constrainedValue == constrainedValue.toLong().toDouble()) {
                        constrainedValue.toLong().toString()
                    } else {
                        constrainedValue.toString()
                    }
                    onValueChange(formatted)
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Increase",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            IconButton(
                onClick = {
                    val currentValue = value.toDoubleOrNull() ?: 0.0
                    val newValue = currentValue - step
                    val constrainedValue = if (minValue != null) {
                        newValue.coerceAtLeast(minValue)
                    } else {
                        newValue
                    }
                    // Format value (remove unnecessary decimals if whole number)
                    val formatted = if (constrainedValue == constrainedValue.toLong().toDouble()) {
                        constrainedValue.toLong().toString()
                    } else {
                        constrainedValue.toString()
                    }
                    onValueChange(formatted)
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Decrease",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Duration input with up/down arrows for hours, minutes, and seconds.
 */
@Composable
private fun DurationInputWithArrows(
    millis: Long,
    onMillisChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val hours = (millis / 3600000).toInt()
    val minutes = ((millis % 3600000) / 60000).toInt()
    val seconds = ((millis % 60000) / 1000).toInt()
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Hours
        DurationUnit(
            value = hours,
            label = "Hours",
            onValueChange = { newHours ->
                val h = newHours.coerceAtLeast(0)
                onMillisChange((h * 3600000L) + (minutes * 60000L) + (seconds * 1000L))
            },
            modifier = Modifier.weight(1f)
        )
        
        // Minutes
        DurationUnit(
            value = minutes,
            label = "Min",
            onValueChange = { newMinutes ->
                val m = newMinutes.coerceIn(0, 59)
                onMillisChange((hours * 3600000L) + (m * 60000L) + (seconds * 1000L))
            },
            modifier = Modifier.weight(1f)
        )
        
        // Seconds
        DurationUnit(
            value = seconds,
            label = "Sec",
            onValueChange = { newSeconds ->
                val s = newSeconds.coerceIn(0, 59)
                onMillisChange((hours * 3600000L) + (minutes * 60000L) + (s * 1000L))
            },
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * A single duration unit field with up/down arrows.
 */
@Composable
private fun DurationUnit(
    value: Int,
    label: String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { onValueChange(value + 1) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase $label",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        OutlinedTextField(
            value = if (value > 0) value.toString() else "",
            onValueChange = { newValue ->
                val parsed = newValue.toIntOrNull() ?: 0
                onValueChange(parsed)
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        
        IconButton(
            onClick = { onValueChange(value - 1) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease $label",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return format.format(Date(timestamp))
}

private fun formatTime(timestamp: Long): String {
    val format = SimpleDateFormat("h:mm a", Locale.getDefault())
    return format.format(Date(timestamp))
}

private fun formatDateTime(timestamp: Long): String {
    val format = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
    return format.format(Date(timestamp))
}

/**
 * Dialog for selecting both date and time in one place.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimePickerDialog(
    currentTimestamp: Long,
    onDismiss: () -> Unit,
    onDateTimeSelected: (Long) -> Unit
) {
    // Track whether we're showing date or time picker
    var showingTimePicker by remember { mutableStateOf(false) }
    
    // Initialize calendar from current timestamp
    val calendar = remember { Calendar.getInstance().apply { timeInMillis = currentTimestamp } }
    
    // Date picker state
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentTimestamp
    )
    
    // Time picker state
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE)
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (showingTimePicker) "Select Time" else "Select Date",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showingTimePicker) {
                    TimePicker(state = timePickerState)
                } else {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        },
        confirmButton = {
            if (showingTimePicker) {
                TextButton(
                    onClick = {
                        // Combine date and time into final timestamp
                        val selectedDate = datePickerState.selectedDateMillis ?: currentTimestamp
                        val selectedCalendar = Calendar.getInstance().apply {
                            timeInMillis = selectedDate
                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(Calendar.MINUTE, timePickerState.minute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        onDateTimeSelected(selectedCalendar.timeInMillis)
                        onDismiss()
                    }
                ) {
                    Text("Confirm")
                }
            } else {
                TextButton(
                    onClick = { showingTimePicker = true }
                ) {
                    Text("Next")
                }
            }
        },
        dismissButton = {
            if (showingTimePicker) {
                TextButton(onClick = { showingTimePicker = false }) {
                    Text("Back")
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}
