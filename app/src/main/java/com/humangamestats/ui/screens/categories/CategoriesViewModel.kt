package com.humangamestats.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humangamestats.data.repository.StatCategoryRepository
import com.humangamestats.model.StatCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Categories screen.
 */
data class CategoriesUiState(
    val categories: List<StatCategory> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showCategoryDialog: Boolean = false,
    val editingCategory: StatCategory? = null,
    val categoryToDelete: StatCategory? = null
)

/**
 * ViewModel for the Categories screen.
 * Manages the list of stat categories.
 */
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: StatCategoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()
    
    init {
        loadCategories()
    }
    
    /**
     * Load all categories from the repository.
     */
    private fun loadCategories() {
        categoryRepository.getAllCategories()
            .onEach { categories ->
                _uiState.update { state ->
                    state.copy(
                        categories = categories,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { e ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load categories"
                    )
                }
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * Show the dialog to create a new category.
     */
    fun showCreateCategoryDialog() {
        _uiState.update { state ->
            state.copy(
                showCategoryDialog = true,
                editingCategory = null
            )
        }
    }
    
    /**
     * Show the dialog to edit an existing category.
     */
    fun showEditCategoryDialog(category: StatCategory) {
        _uiState.update { state ->
            state.copy(
                showCategoryDialog = true,
                editingCategory = category
            )
        }
    }
    
    /**
     * Hide the category dialog.
     */
    fun hideCategoryDialog() {
        _uiState.update { state ->
            state.copy(
                showCategoryDialog = false,
                editingCategory = null
            )
        }
    }
    
    /**
     * Save a category (create or update).
     */
    fun saveCategory(title: String, icon: String) {
        viewModelScope.launch {
            try {
                val existingCategory = _uiState.value.editingCategory
                val category = if (existingCategory != null) {
                    existingCategory.copy(
                        title = title,
                        icon = icon,
                        updatedAt = System.currentTimeMillis()
                    )
                } else {
                    StatCategory(
                        title = title,
                        icon = icon
                    )
                }
                
                categoryRepository.saveCategory(category)
                hideCategoryDialog()
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(error = e.message ?: "Failed to save category")
                }
            }
        }
    }
    
    /**
     * Show delete confirmation for a category.
     */
    fun showDeleteConfirmation(category: StatCategory) {
        _uiState.update { state ->
            state.copy(categoryToDelete = category)
        }
    }
    
    /**
     * Hide delete confirmation.
     */
    fun hideDeleteConfirmation() {
        _uiState.update { state ->
            state.copy(categoryToDelete = null)
        }
    }
    
    /**
     * Delete a category.
     */
    fun deleteCategory(category: StatCategory) {
        viewModelScope.launch {
            try {
                categoryRepository.deleteCategory(category)
                hideDeleteConfirmation()
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(error = e.message ?: "Failed to delete category")
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
