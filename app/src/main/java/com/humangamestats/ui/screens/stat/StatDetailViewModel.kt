package com.humangamestats.ui.screens.stat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humangamestats.data.repository.StatRecordRepository
import com.humangamestats.data.repository.StatRepository
import com.humangamestats.data.repository.StatStatistics
import com.humangamestats.model.Stat
import com.humangamestats.model.StatRecord
import com.humangamestats.model.SortOption
import com.humangamestats.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Represents a group of records for a single day.
 */
data class DayGroup(
    val date: Long, // Start of day timestamp
    val displayDate: String, // Formatted display string (Today, Yesterday, or abbreviated date)
    val records: List<StatRecord>
)

/**
 * UI state for the Stat Detail screen.
 */
data class StatDetailUiState(
    val stat: Stat? = null,
    val records: List<StatRecord> = emptyList(),
    val groupedRecords: List<DayGroup> = emptyList(),
    val chartData: List<StatRecord> = emptyList(),
    val statistics: StatStatistics? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val sortOption: SortOption = SortOption.RECENT,
    val recordToDelete: StatRecord? = null,
    val showChart: Boolean = true
)

/**
 * ViewModel for the Stat Detail screen.
 * Displays all records for a stat with chart visualization.
 */
@HiltViewModel
class StatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val statRepository: StatRepository,
    private val recordRepository: StatRecordRepository
) : ViewModel() {
    
    private val statId: Long = savedStateHandle.get<Long>(Screen.StatDetail.ARG_STAT_ID) ?: 0L
    
    private val _uiState = MutableStateFlow(StatDetailUiState())
    val uiState: StateFlow<StatDetailUiState> = _uiState.asStateFlow()
    
    private val _sortOption = MutableStateFlow(SortOption.RECENT)
    
    init {
        loadStat()
        loadRecords()
        loadChartData()
        loadStatistics()
    }
    
    /**
     * Load the stat details.
     */
    private fun loadStat() {
        statRepository.getStatByIdFlow(statId)
            .onEach { stat ->
                _uiState.update { state ->
                    state.copy(stat = stat)
                }
            }
            .catch { e ->
                _uiState.update { state ->
                    state.copy(error = e.message ?: "Failed to load stat")
                }
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * Load records for the stat with current sort option.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadRecords() {
        _sortOption
            .flatMapLatest { sortOption ->
                _uiState.update { it.copy(sortOption = sortOption) }
                recordRepository.getRecordsByStatSorted(statId, sortOption)
            }
            .onEach { records ->
                _uiState.update { state ->
                    state.copy(
                        records = records,
                        groupedRecords = groupRecordsByDay(records),
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { e ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load records"
                    )
                }
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * Load chart data (recent records in chronological order).
     */
    private fun loadChartData() {
        viewModelScope.launch {
            try {
                val chartData = recordRepository.getRecordsForChart(statId, 30)
                _uiState.update { state ->
                    state.copy(chartData = chartData)
                }
            } catch (e: Exception) {
                // Chart data is optional, don't show error
            }
        }
    }
    
    /**
     * Load statistics for the stat.
     */
    private fun loadStatistics() {
        viewModelScope.launch {
            try {
                val statistics = recordRepository.getStatStatistics(statId)
                _uiState.update { state ->
                    state.copy(statistics = statistics)
                }
            } catch (e: Exception) {
                // Statistics are optional, don't show error
            }
        }
    }
    
    /**
     * Refresh chart data and statistics.
     */
    fun refreshData() {
        loadChartData()
        loadStatistics()
    }
    
    /**
     * Set the sort option.
     */
    fun setSortOption(sortOption: SortOption) {
        _sortOption.value = sortOption
    }
    
    /**
     * Toggle chart visibility.
     */
    fun toggleChart() {
        _uiState.update { state ->
            state.copy(showChart = !state.showChart)
        }
    }
    
    /**
     * Show delete confirmation for a record.
     */
    fun showDeleteConfirmation(record: StatRecord) {
        _uiState.update { state ->
            state.copy(recordToDelete = record)
        }
    }
    
    /**
     * Hide delete confirmation.
     */
    fun hideDeleteConfirmation() {
        _uiState.update { state ->
            state.copy(recordToDelete = null)
        }
    }
    
    /**
     * Delete a record.
     */
    fun deleteRecord(record: StatRecord) {
        viewModelScope.launch {
            try {
                recordRepository.deleteRecord(record)
                hideDeleteConfirmation()
                refreshData()
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(error = e.message ?: "Failed to delete record")
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
    
    /**
     * Group records by day and format with relative dates.
     * Returns records grouped by the day they were recorded.
     */
    private fun groupRecordsByDay(records: List<StatRecord>): List<DayGroup> {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        val yesterday = today - 86400000L // 24 hours in millis
        
        return records
            .groupBy { record ->
                Calendar.getInstance().apply {
                    timeInMillis = record.recordedAt
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
            }
            .map { (dayStart, dayRecords) ->
                val displayDate = when {
                    dayStart >= today -> "Today"
                    dayStart >= yesterday -> "Yesterday"
                    else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                        .format(Date(dayStart))
                }
                DayGroup(dayStart, displayDate, dayRecords)
            }
            .sortedByDescending { it.date }
    }
}
