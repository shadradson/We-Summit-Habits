package com.humangamestats.ui.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humangamestats.data.repository.StatRepository
import com.humangamestats.model.StatWithSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * UI state for the Today screen.
 */
data class TodayUiState(
    val stats: List<StatWithSummary> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * ViewModel for the Today screen.
 * Shows all stats that have record entries from today.
 */
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val statRepository: StatRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()
    
    init {
        loadTodayStats()
    }
    
    /**
     * Load stats that have records from today.
     */
    private fun loadTodayStats() {
        statRepository.getStatsWithTodayRecords()
            .onEach { stats ->
                _uiState.update { state ->
                    state.copy(
                        stats = stats,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { e ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load today's stats"
                    )
                }
            }
            .launchIn(viewModelScope)
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
