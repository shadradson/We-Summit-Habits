package com.humangamestats.ui.screens.stat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humangamestats.data.repository.DataPointTemplateRepository
import com.humangamestats.data.repository.StatRepository
import com.humangamestats.model.DataPoint
import com.humangamestats.model.DataPointTemplate
import com.humangamestats.model.Stat
import com.humangamestats.model.StatType
import com.humangamestats.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Stat Form screen.
 */
data class StatFormUiState(
    val isEditing: Boolean = false,
    val name: String = "",
    val dataPoints: List<DataPoint> = listOf(DataPoint.number("Value")),
    val selectedTemplateId: Long? = null,
    val templates: List<DataPointTemplate> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val error: String? = null,
    val saveComplete: Boolean = false,
    val deleteComplete: Boolean = false,
    // For adding/editing a data point
    val showDataPointDialog: Boolean = false,
    val editingDataPointIndex: Int? = null,
    val dialogDataPointLabel: String = "",
    val dialogDataPointType: StatType = StatType.NUMBER,
    val dialogDataPointUnit: String = "",
    // For data point delete confirmation
    val showDeleteConfirmation: Boolean = false,
    val dataPointToDeleteIndex: Int? = null,
    // For stat delete confirmation
    val showStatDeleteConfirmation: Boolean = false
)

/**
 * ViewModel for the Stat Form screen.
 * Handles creating and editing stats with multiple data points support.
 */
@HiltViewModel
class StatFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val statRepository: StatRepository,
    private val templateRepository: DataPointTemplateRepository
) : ViewModel() {
    
    private val categoryId: Long = savedStateHandle.get<Long>(Screen.StatForm.ARG_CATEGORY_ID) ?: 0L
    private val statId: Long? = savedStateHandle.get<Long>(Screen.StatForm.ARG_STAT_ID)?.takeIf { it != -1L }
    
    private val _uiState = MutableStateFlow(StatFormUiState())
    val uiState: StateFlow<StatFormUiState> = _uiState.asStateFlow()
    
    private var existingStat: Stat? = null
    
    init {
        loadTemplates()
        if (statId != null) {
            loadExistingStat()
        }
    }
    
    /**
     * Load available templates.
     */
    private fun loadTemplates() {
        viewModelScope.launch {
            templateRepository.getAllTemplates().collect { templates ->
                _uiState.update { it.copy(templates = templates) }
            }
        }
    }
    
    /**
     * Load existing stat for editing.
     */
    private fun loadExistingStat() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val stat = statRepository.getStatById(statId!!)
                if (stat != null) {
                    existingStat = stat
                    _uiState.update { state ->
                        state.copy(
                            isEditing = true,
                            name = stat.name,
                            dataPoints = stat.dataPoints,
                            selectedTemplateId = stat.templateId,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = "Stat not found"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load stat"
                    )
                }
            }
        }
    }
    
    /**
     * Update the stat name.
     */
    fun updateName(name: String) {
        _uiState.update { state ->
            state.copy(name = name)
        }
    }
    
    /**
     * Apply a template's data points.
     */
    fun applyTemplate(template: DataPointTemplate) {
        _uiState.update { state ->
            state.copy(
                dataPoints = template.dataPoints,
                selectedTemplateId = template.id
            )
        }
    }
    
    /**
     * Clear template selection and reset to single data point.
     */
    fun clearTemplate() {
        _uiState.update { state ->
            state.copy(
                dataPoints = listOf(DataPoint.number("Value")),
                selectedTemplateId = null
            )
        }
    }
    
    /**
     * Show dialog to add a new data point.
     */
    fun showAddDataPointDialog() {
        _uiState.update { state ->
            state.copy(
                showDataPointDialog = true,
                editingDataPointIndex = null,
                dialogDataPointLabel = "",
                dialogDataPointType = StatType.NUMBER,
                dialogDataPointUnit = ""
            )
        }
    }
    
    /**
     * Show dialog to edit an existing data point.
     */
    fun showEditDataPointDialog(index: Int) {
        val dataPoint = _uiState.value.dataPoints.getOrNull(index) ?: return
        _uiState.update { state ->
            state.copy(
                showDataPointDialog = true,
                editingDataPointIndex = index,
                dialogDataPointLabel = dataPoint.label,
                dialogDataPointType = dataPoint.type,
                dialogDataPointUnit = dataPoint.unit
            )
        }
    }
    
    /**
     * Dismiss the data point dialog.
     */
    fun dismissDataPointDialog() {
        _uiState.update { state ->
            state.copy(showDataPointDialog = false)
        }
    }
    
    /**
     * Update the dialog data point label.
     */
    fun updateDialogLabel(label: String) {
        _uiState.update { state ->
            state.copy(dialogDataPointLabel = label)
        }
    }
    
    /**
     * Update the dialog data point type.
     */
    fun updateDialogType(type: StatType) {
        _uiState.update { state ->
            state.copy(dialogDataPointType = type)
        }
    }
    
    /**
     * Update the dialog data point unit.
     */
    fun updateDialogUnit(unit: String) {
        _uiState.update { state ->
            state.copy(dialogDataPointUnit = unit)
        }
    }
    
    /**
     * Save the data point from the dialog.
     */
    fun saveDataPoint() {
        val currentState = _uiState.value
        
        if (currentState.dialogDataPointLabel.isBlank()) {
            _uiState.update { it.copy(error = "Data point label is required") }
            return
        }
        
        val newDataPoint = DataPoint(
            label = currentState.dialogDataPointLabel.trim(),
            type = currentState.dialogDataPointType,
            unit = currentState.dialogDataPointUnit.trim()
        )
        
        val newDataPoints = if (currentState.editingDataPointIndex != null) {
            // Editing existing data point
            currentState.dataPoints.toMutableList().apply {
                this[currentState.editingDataPointIndex] = newDataPoint
            }
        } else {
            // Adding new data point
            currentState.dataPoints + newDataPoint
        }
        
        _uiState.update { state ->
            state.copy(
                dataPoints = newDataPoints,
                showDataPointDialog = false,
                selectedTemplateId = null // Clear template when manually editing
            )
        }
    }
    
    /**
     * Show delete confirmation dialog for a data point.
     */
    fun showDeleteDataPointConfirmation(index: Int) {
        val currentState = _uiState.value
        if (currentState.dataPoints.size <= 1) {
            _uiState.update { it.copy(error = "Stat must have at least one data point") }
            return
        }
        
        _uiState.update { state ->
            state.copy(
                showDeleteConfirmation = true,
                dataPointToDeleteIndex = index
            )
        }
    }
    
    /**
     * Hide delete confirmation dialog.
     */
    fun hideDeleteDataPointConfirmation() {
        _uiState.update { state ->
            state.copy(
                showDeleteConfirmation = false,
                dataPointToDeleteIndex = null
            )
        }
    }
    
    /**
     * Confirm and remove a data point by index.
     */
    fun confirmDeleteDataPoint() {
        val currentState = _uiState.value
        val index = currentState.dataPointToDeleteIndex ?: return
        
        if (currentState.dataPoints.size <= 1) {
            _uiState.update { it.copy(
                error = "Stat must have at least one data point",
                showDeleteConfirmation = false,
                dataPointToDeleteIndex = null
            ) }
            return
        }
        
        _uiState.update { state ->
            state.copy(
                dataPoints = state.dataPoints.filterIndexed { i, _ -> i != index },
                selectedTemplateId = null, // Clear template when manually editing
                showDeleteConfirmation = false,
                dataPointToDeleteIndex = null
            )
        }
    }
    
    /**
     * Remove a data point by index (direct, for internal use).
     */
    fun removeDataPoint(index: Int) {
        showDeleteDataPointConfirmation(index)
    }
    
    /**
     * Move a data point up in the list.
     */
    fun moveDataPointUp(index: Int) {
        if (index <= 0) return
        
        _uiState.update { state ->
            val mutableList = state.dataPoints.toMutableList()
            val temp = mutableList[index]
            mutableList[index] = mutableList[index - 1]
            mutableList[index - 1] = temp
            state.copy(
                dataPoints = mutableList,
                selectedTemplateId = null // Clear template when manually editing
            )
        }
    }
    
    /**
     * Move a data point down in the list.
     */
    fun moveDataPointDown(index: Int) {
        val currentState = _uiState.value
        if (index >= currentState.dataPoints.size - 1) return
        
        _uiState.update { state ->
            val mutableList = state.dataPoints.toMutableList()
            val temp = mutableList[index]
            mutableList[index] = mutableList[index + 1]
            mutableList[index + 1] = temp
            state.copy(
                dataPoints = mutableList,
                selectedTemplateId = null // Clear template when manually editing
            )
        }
    }
    
    /**
     * Validate and save the stat.
     */
    fun save() {
        val currentState = _uiState.value
        
        // Validation
        if (currentState.name.isBlank()) {
            _uiState.update { it.copy(error = "Name is required") }
            return
        }
        
        if (currentState.dataPoints.isEmpty()) {
            _uiState.update { it.copy(error = "At least one data point is required") }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                val stat = if (existingStat != null) {
                    existingStat!!.copy(
                        name = currentState.name.trim(),
                        dataPoints = currentState.dataPoints,
                        templateId = currentState.selectedTemplateId,
                        updatedAt = System.currentTimeMillis()
                    )
                } else {
                    Stat(
                        categoryId = categoryId,
                        name = currentState.name.trim(),
                        dataPoints = currentState.dataPoints,
                        templateId = currentState.selectedTemplateId
                    )
                }
                
                statRepository.saveStat(stat)
                _uiState.update { it.copy(isSaving = false, saveComplete = true) }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isSaving = false,
                        error = e.message ?: "Failed to save stat"
                    )
                }
            }
        }
    }
    
    /**
     * Show confirmation dialog for deleting the stat.
     */
    fun showStatDeleteConfirmation() {
        if (!_uiState.value.isEditing) return
        _uiState.update { state ->
            state.copy(showStatDeleteConfirmation = true)
        }
    }
    
    /**
     * Hide stat delete confirmation dialog.
     */
    fun hideStatDeleteConfirmation() {
        _uiState.update { state ->
            state.copy(showStatDeleteConfirmation = false)
        }
    }
    
    /**
     * Delete the current stat.
     */
    fun deleteStat() {
        val stat = existingStat ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, error = null) }
            try {
                statRepository.deleteStat(stat)
                _uiState.update { it.copy(isDeleting = false, deleteComplete = true) }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isDeleting = false,
                        showStatDeleteConfirmation = false,
                        error = e.message ?: "Failed to delete stat"
                    )
                }
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
