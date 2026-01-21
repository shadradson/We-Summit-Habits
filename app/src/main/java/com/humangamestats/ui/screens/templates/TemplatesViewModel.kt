package com.humangamestats.ui.screens.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humangamestats.data.repository.DataPointTemplateRepository
import com.humangamestats.model.DataPoint
import com.humangamestats.model.DataPointTemplate
import com.humangamestats.model.StatType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Templates screen.
 */
data class TemplatesUiState(
    val isLoading: Boolean = true,
    val templates: List<DataPointTemplate> = emptyList(),
    val editingTemplate: DataPointTemplate? = null,
    val showEditDialog: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val templateToDelete: DataPointTemplate? = null,
    val errorMessage: String? = null
)

/**
 * ViewModel for managing Data Point Templates.
 */
@HiltViewModel
class TemplatesViewModel @Inject constructor(
    private val templateRepository: DataPointTemplateRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TemplatesUiState())
    val uiState: StateFlow<TemplatesUiState> = _uiState.asStateFlow()
    
    init {
        loadTemplates()
        initializeDefaultTemplates()
    }
    
    private fun initializeDefaultTemplates() {
        viewModelScope.launch {
            templateRepository.initializeDefaultTemplates()
        }
    }
    
    private fun loadTemplates() {
        viewModelScope.launch {
            templateRepository.getAllTemplates()
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5000),
                    emptyList()
                )
                .collect { templates ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        templates = templates
                    )
                }
        }
    }
    
    /**
     * Show dialog to create a new template.
     */
    fun showCreateDialog() {
        _uiState.value = _uiState.value.copy(
            editingTemplate = DataPointTemplate(
                name = "",
                description = "",
                dataPoints = listOf(DataPoint.number("Value"))
            ),
            showEditDialog = true
        )
    }
    
    /**
     * Show dialog to edit an existing template.
     */
    fun showEditDialog(template: DataPointTemplate) {
        // Don't allow editing system templates
        if (template.isSystem) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "System templates cannot be edited. You can duplicate it instead."
            )
            return
        }
        
        _uiState.value = _uiState.value.copy(
            editingTemplate = template,
            showEditDialog = true
        )
    }
    
    /**
     * Duplicate a template (useful for creating variants of system templates).
     */
    fun duplicateTemplate(template: DataPointTemplate) {
        _uiState.value = _uiState.value.copy(
            editingTemplate = template.copy(
                id = 0,
                name = "${template.name} (Copy)",
                isSystem = false
            ),
            showEditDialog = true
        )
    }
    
    /**
     * Dismiss the edit dialog.
     */
    fun dismissEditDialog() {
        _uiState.value = _uiState.value.copy(
            editingTemplate = null,
            showEditDialog = false
        )
    }
    
    /**
     * Save a template (create or update).
     */
    fun saveTemplate(
        name: String,
        description: String,
        dataPoints: List<DataPoint>
    ) {
        val template = _uiState.value.editingTemplate ?: return
        
        viewModelScope.launch {
            try {
                // Validate name
                if (name.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Template name cannot be empty"
                    )
                    return@launch
                }
                
                // Validate data points
                if (dataPoints.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Template must have at least one data point"
                    )
                    return@launch
                }
                
                // Check for duplicate name
                val nameExists = templateRepository.templateNameExists(name, template.id)
                if (nameExists) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "A template with this name already exists"
                    )
                    return@launch
                }
                
                val updatedTemplate = template.copy(
                    name = name,
                    description = description,
                    dataPoints = dataPoints
                )
                
                if (template.id == 0L) {
                    // Creating new template
                    templateRepository.createTemplate(updatedTemplate)
                } else {
                    // Updating existing template
                    templateRepository.updateTemplate(updatedTemplate)
                }
                
                _uiState.value = _uiState.value.copy(
                    editingTemplate = null,
                    showEditDialog = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to save template: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Show delete confirmation dialog.
     */
    fun showDeleteConfirmation(template: DataPointTemplate) {
        if (template.isSystem) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "System templates cannot be deleted"
            )
            return
        }
        
        _uiState.value = _uiState.value.copy(
            templateToDelete = template,
            showDeleteConfirmation = true
        )
    }
    
    /**
     * Dismiss delete confirmation dialog.
     */
    fun dismissDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(
            templateToDelete = null,
            showDeleteConfirmation = false
        )
    }
    
    /**
     * Confirm and delete template.
     */
    fun confirmDelete() {
        val template = _uiState.value.templateToDelete ?: return
        
        viewModelScope.launch {
            try {
                templateRepository.deleteTemplate(template)
                _uiState.value = _uiState.value.copy(
                    templateToDelete = null,
                    showDeleteConfirmation = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete template: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Clear error message.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
