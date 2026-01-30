package com.humangamestats.ui.screens.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humangamestats.data.datastore.SettingsDataStore
import com.humangamestats.data.repository.StatRecordRepository
import com.humangamestats.data.repository.StatRepository
import com.humangamestats.data.service.LocationService
import com.humangamestats.model.DataPoint
import com.humangamestats.model.DataPointValue
import com.humangamestats.model.LocationData
import com.humangamestats.model.Stat
import com.humangamestats.model.StatRecord
import com.humangamestats.model.StatType
import com.humangamestats.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Record Form screen.
 */
data class RecordFormUiState(
    val stat: Stat? = null,
    val isEditing: Boolean = false,
    val values: List<String> = emptyList(), // One value per data point
    val notes: String = "",
    val recordedAt: Long = System.currentTimeMillis(),
    val location: LocationData? = null,
    val isCapturingLocation: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveComplete: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDateTimeDialog: Boolean = false
)

/**
 * ViewModel for the Record Form screen.
 * Handles creating and editing stat records with auto-capture features.
 */
@HiltViewModel
class RecordFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val statRepository: StatRepository,
    private val recordRepository: StatRecordRepository,
    private val locationService: LocationService,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {
    
    private val statId: Long = savedStateHandle.get<Long>(Screen.RecordForm.ARG_STAT_ID) ?: 0L
    private val recordId: Long? = savedStateHandle.get<Long>(Screen.RecordForm.ARG_RECORD_ID)?.takeIf { it != -1L }
    
    private val _uiState = MutableStateFlow(RecordFormUiState())
    val uiState: StateFlow<RecordFormUiState> = _uiState.asStateFlow()
    
    private var existingRecord: StatRecord? = null
    
    init {
        loadStat()
        if (recordId != null) {
            loadExistingRecord()
        } else {
            // Auto-capture location for new records
            captureLocationIfEnabled()
        }
    }
    
    /**
     * Load the stat for this record.
     */
    private fun loadStat() {
        viewModelScope.launch {
            try {
                val stat = statRepository.getStatById(statId)
                if (stat != null) {
                    // Get the most recent record to pre-fill values
                    val lastRecord = recordRepository.getLatestRecordByStat(statId)

                    val defaultValues = if (lastRecord != null) {
                        // Use values from the last record, using ID-based lookup with index fallback
                        stat.dataPoints.mapIndexed { index, dp ->
                            lastRecord.getValueForDataPoint(dp.id)
                                ?: lastRecord.values.find { it.dataPointIndex == index }?.value
                                ?: getDefaultValueForType(dp.type)
                        }
                    } else {
                        // Fresh defaults
                        stat.dataPoints.map { dp ->
                            getDefaultValueForType(dp.type)
                        }
                    }

                    _uiState.update { state ->
                        state.copy(stat = stat, values = defaultValues)
                    }
                }
            } catch (e: Exception) { /* ... */ }
        }
    }
    
    /**
     * Get default value for a data point type.
     */
    private fun getDefaultValueForType(type: StatType): String {
        return when (type) {
            StatType.CHECKBOX -> "false"
            StatType.RATING -> "3"
            StatType.DURATION -> "0"
            StatType.NUMBER -> ""
        }
    }
    
    /**
     * Load existing record for editing.
     * Ensures stat is loaded first to properly map values to data points.
     */
    private fun loadExistingRecord() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Ensure we have the stat first - fetch it if not already loaded
                var stat = _uiState.value.stat
                if (stat == null) {
                    stat = statRepository.getStatById(statId)
                    if (stat != null) {
                        _uiState.update { it.copy(stat = stat) }
                    }
                }
                
                val record = recordRepository.getRecordById(recordId!!)
                if (record != null) {
                    existingRecord = record
                    
                    // Extract values from record, ensuring we have values for all data points
                    // Using ID-based lookup with index fallback for legacy data
                    val valuesList = if (stat != null) {
                        stat.dataPoints.mapIndexed { index, dp ->
                            record.getValueForDataPoint(dp.id)
                                ?: record.values.find { it.dataPointIndex == index }?.value
                                ?: getDefaultValueForType(dp.type)
                        }
                    } else {
                        // Fallback: ensure we have enough values for the record
                        record.values.map { it.value }
                    }
                    
                    _uiState.update { state ->
                        state.copy(
                            isEditing = true,
                            values = valuesList,
                            notes = record.notes ?: "",
                            recordedAt = record.recordedAt,
                            location = if (record.latitude != null && record.longitude != null) {
                                LocationData(
                                    latitude = record.latitude,
                                    longitude = record.longitude,
                                    locationName = record.locationName
                                )
                            } else null,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = "Record not found"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load record"
                    )
                }
            }
        }
    }
    
    /**
     * Capture location if auto-capture is enabled.
     */
    private fun captureLocationIfEnabled() {
        viewModelScope.launch {
            val autoCaptureEnabled = settingsDataStore.autoCaptureLocation.first()
            if (autoCaptureEnabled && locationService.hasLocationPermission()) {
                captureLocation()
            }
        }
    }
    
    /**
     * Manually capture current location.
     */
    fun captureLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCapturingLocation = true) }
            try {
                val location = locationService.getCurrentLocation()
                _uiState.update { state ->
                    state.copy(
                        location = location,
                        isCapturingLocation = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isCapturingLocation = false,
                        error = "Failed to capture location"
                    )
                }
            }
        }
    }
    
    /**
     * Clear captured location.
     */
    fun clearLocation() {
        _uiState.update { state ->
            state.copy(location = null)
        }
    }
    
    /**
     * Check if location permission is available.
     */
    fun hasLocationPermission(): Boolean = locationService.hasLocationPermission()
    
    /**
     * Update a specific data point value.
     * Expands the values list if needed to accommodate the index.
     */
    fun updateValue(index: Int, value: String) {
        _uiState.update { state ->
            val newValues = state.values.toMutableList()
            // Expand the list if needed to accommodate the index
            while (newValues.size <= index) {
                newValues.add("")
            }
            newValues[index] = value
            state.copy(values = newValues)
        }
    }
    
    /**
     * Update the notes.
     */
    fun updateNotes(notes: String) {
        _uiState.update { state ->
            state.copy(notes = notes)
        }
    }
    
    /**
     * Update the recorded at timestamp.
     */
    fun updateRecordedAt(timestamp: Long) {
        _uiState.update { state ->
            state.copy(recordedAt = timestamp)
        }
    }
    
    /**
     * Show date picker.
     */
    fun showDatePicker() {
        _uiState.update { state ->
            state.copy(showDatePicker = true)
        }
    }
    
    /**
     * Hide date picker.
     */
    fun hideDatePicker() {
        _uiState.update { state ->
            state.copy(showDatePicker = false)
        }
    }
    
    /**
     * Show time picker.
     */
    fun showTimePicker() {
        _uiState.update { state ->
            state.copy(showTimePicker = true)
        }
    }
    
    /**
     * Hide time picker.
     */
    fun hideTimePicker() {
        _uiState.update { state ->
            state.copy(showTimePicker = false)
        }
    }
    
    /**
     * Show combined date/time selection dialog.
     */
    fun showDateTimeDialog() {
        _uiState.update { state ->
            state.copy(showDateTimeDialog = true)
        }
    }
    
    /**
     * Hide combined date/time selection dialog.
     */
    fun hideDateTimeDialog() {
        _uiState.update { state ->
            state.copy(showDateTimeDialog = false)
        }
    }
    
    /**
     * Validate and save the record.
     */
    fun save() {
        val currentState = _uiState.value
        val stat = currentState.stat
        
        if (stat == null) {
            _uiState.update { it.copy(error = "Stat not found") }
            return
        }
        
        // Validation for each data point
        stat.dataPoints.forEachIndexed { index, dp ->
            val value = currentState.values.getOrNull(index) ?: ""
            val validationError = validateValue(value, dp.type, dp.label)
            if (validationError != null) {
                _uiState.update { it.copy(error = validationError) }
                return
            }
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                // Create values list using data point IDs (not indices)
                // This ensures values remain linked to their data points even after reordering
                val dataPointValues = stat.dataPoints.mapIndexed { index, dp ->
                    DataPointValue.forDataPoint(dp.id, currentState.values.getOrElse(index) { "" }.trim())
                }
                
                val record = if (existingRecord != null) {
                    existingRecord!!.copy(
                        values = dataPointValues,
                        notes = currentState.notes.trim().takeIf { it.isNotBlank() },
                        latitude = currentState.location?.latitude,
                        longitude = currentState.location?.longitude,
                        locationName = currentState.location?.locationName,
                        recordedAt = currentState.recordedAt,
                        updatedAt = System.currentTimeMillis()
                    )
                } else {
                    StatRecord(
                        statId = statId,
                        values = dataPointValues,
                        notes = currentState.notes.trim().takeIf { it.isNotBlank() },
                        latitude = currentState.location?.latitude,
                        longitude = currentState.location?.longitude,
                        locationName = currentState.location?.locationName,
                        recordedAt = currentState.recordedAt
                    )
                }
                
                recordRepository.saveRecord(record)
                _uiState.update { it.copy(isSaving = false, saveComplete = true) }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isSaving = false,
                        error = e.message ?: "Failed to save record"
                    )
                }
            }
        }
    }
    
    /**
     * Validate value based on stat type.
     */
    private fun validateValue(value: String, statType: StatType, label: String): String? {
        return when (statType) {
            StatType.NUMBER -> {
                if (value.isBlank()) "$label is required"
                else if (value.toDoubleOrNull() == null) "Invalid number for $label"
                else null
            }
            StatType.DURATION -> {
                if (value.toLongOrNull() == null) "Invalid duration for $label"
                else null
            }
            StatType.RATING -> {
                val rating = value.toIntOrNull()
                if (rating == null || rating !in 1..5) "$label must be between 1 and 5"
                else null
            }
            StatType.CHECKBOX -> {
                if (value != "true" && value != "false") "Invalid value for $label"
                else null
            }
        }
    }
    
    /**
     * Clear error message.
     */
    fun clearError() {
        _uiState.update { state ->
            state.copy(error = null)
        }
    }
}
