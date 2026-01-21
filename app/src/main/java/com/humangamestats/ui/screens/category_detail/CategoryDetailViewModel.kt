package com.humangamestats.ui.screens.category_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humangamestats.data.repository.StatCategoryRepository
import com.humangamestats.data.repository.StatRepository
import com.humangamestats.model.Stat
import com.humangamestats.model.StatCategory
import com.humangamestats.model.SortOption
import com.humangamestats.model.StatWithSummary
import com.humangamestats.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Category Detail screen.
 */
data class CategoryDetailUiState(
    val category: StatCategory? = null,
    val stats: List<StatWithSummary> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val sortOption: SortOption = SortOption.RECENT,
    val statToDelete: Stat? = null
)

/**
 * ViewModel for the Category Detail screen.
 * Displays all stats within a category with sorting options.
 */
@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: StatCategoryRepository,
    private val statRepository: StatRepository
) : ViewModel() {
    
    private val categoryId: Long = savedStateHandle.get<Long>(Screen.CategoryDetail.ARG_CATEGORY_ID) ?: 0L
    
    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    val uiState: StateFlow<CategoryDetailUiState> = _uiState.asStateFlow()
    
    private val _sortOption = MutableStateFlow(SortOption.RECENT)
    
    init {
        loadCategory()
        loadStats()
    }
    
    /**
     * Load the category details.
     */
    private fun loadCategory() {
        categoryRepository.getCategoryByIdFlow(categoryId)
            .onEach { category ->
                _uiState.update { state ->
                    state.copy(category = category)
                }
            }
            .catch { e ->
                _uiState.update { state ->
                    state.copy(error = e.message ?: "Failed to load category")
                }
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * Load stats for the category with current sort option.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadStats() {
        _sortOption
            .flatMapLatest { sortOption ->
                _uiState.update { it.copy(sortOption = sortOption) }
                statRepository.getStatsWithSummaryByCategory(categoryId)
            }
            .onEach { stats ->
                val sortedStats = sortStats(stats, _sortOption.value)
                _uiState.update { state ->
                    state.copy(
                        stats = sortedStats,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { e ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load stats"
                    )
                }
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * Sort stats based on the selected sort option.
     */
    private fun sortStats(stats: List<StatWithSummary>, sortOption: SortOption): List<StatWithSummary> {
        return when (sortOption) {
            SortOption.RECENT -> stats.sortedByDescending { it.latestRecordedAt ?: 0L }
            SortOption.OLDEST -> stats.sortedBy { it.latestRecordedAt ?: Long.MAX_VALUE }
            SortOption.ALPHABETICAL -> stats.sortedBy { it.stat.name.lowercase() }
            SortOption.ALPHABETICAL_DESC -> stats.sortedByDescending { it.stat.name.lowercase() }
            SortOption.HIGHEST -> stats.sortedByDescending { 
                it.latestValue?.toDoubleOrNull() ?: Double.MIN_VALUE 
            }
            SortOption.LOWEST -> stats.sortedBy { 
                it.latestValue?.toDoubleOrNull() ?: Double.MAX_VALUE 
            }
        }
    }
    
    /**
     * Set the sort option.
     */
    fun setSortOption(sortOption: SortOption) {
        _sortOption.value = sortOption
    }
    
    /**
     * Show delete confirmation for a stat.
     */
    fun showDeleteConfirmation(stat: Stat) {
        _uiState.update { state ->
            state.copy(statToDelete = stat)
        }
    }
    
    /**
     * Hide delete confirmation.
     */
    fun hideDeleteConfirmation() {
        _uiState.update { state ->
            state.copy(statToDelete = null)
        }
    }
    
    /**
     * Delete a stat.
     */
    fun deleteStat(stat: Stat) {
        viewModelScope.launch {
            try {
                statRepository.deleteStat(stat)
                hideDeleteConfirmation()
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(error = e.message ?: "Failed to delete stat")
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
