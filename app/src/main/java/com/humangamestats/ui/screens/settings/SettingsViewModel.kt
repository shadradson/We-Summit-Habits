package com.humangamestats.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humangamestats.data.datastore.SettingsDataStore
import com.humangamestats.data.datastore.ThemeMode
import com.humangamestats.data.repository.StatCategoryRepository
import com.humangamestats.data.repository.StatRecordRepository
import com.humangamestats.data.repository.StatRepository
import com.humangamestats.model.DataPoint
import com.humangamestats.model.DataPointValue
import com.humangamestats.model.SortOption
import com.humangamestats.model.Stat
import com.humangamestats.model.StatCategory
import com.humangamestats.model.StatRecord
import com.humangamestats.model.StatType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * UI state for the Settings screen.
 */
data class SettingsUiState(
    val autoCaptureLocation: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val defaultSortOption: SortOption = SortOption.RECENT,
    val dateFormat: String = SettingsDataStore.DEFAULT_DATE_FORMAT,
    val useDynamicColors: Boolean = true,
    val gradientStartColor: Long = SettingsDataStore.DEFAULT_GRADIENT_START,
    val gradientEndColor: Long = SettingsDataStore.DEFAULT_GRADIENT_END,
    val iconAccentColor: Long = SettingsDataStore.DEFAULT_ICON_ACCENT,
    val isLoading: Boolean = true,
    val error: String? = null,
    val categoryCount: Int = 0,
    val statCount: Int = 0,
    val recordCount: Int = 0
)

/**
 * UI state for Export/Import screen.
 */
data class ExportImportUiState(
    val isExporting: Boolean = false,
    val isImporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val importSuccess: Boolean = false,
    val error: String? = null,
    val exportData: String? = null
)

/**
 * ViewModel for the Settings screen.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val categoryRepository: StatCategoryRepository,
    private val statRepository: StatRepository,
    private val recordRepository: StatRecordRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    private val _exportImportState = MutableStateFlow(ExportImportUiState())
    val exportImportState: StateFlow<ExportImportUiState> = _exportImportState.asStateFlow()
    
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    init {
        loadSettings()
        loadDataCounts()
    }
    
    /**
     * Load settings from DataStore.
     */
    private fun loadSettings() {
        // Load main settings
        combine(
            settingsDataStore.autoCaptureLocation,
            settingsDataStore.themeMode,
            settingsDataStore.defaultSortOption,
            settingsDataStore.dateFormat,
            settingsDataStore.useDynamicColors
        ) { autoCaptureLocation, themeMode, defaultSortOption, dateFormat, useDynamicColors ->
            _uiState.update { state ->
                state.copy(
                    autoCaptureLocation = autoCaptureLocation,
                    themeMode = themeMode,
                    defaultSortOption = defaultSortOption,
                    dateFormat = dateFormat,
                    useDynamicColors = useDynamicColors,
                    isLoading = false
                )
            }
        }
            .launchIn(viewModelScope)
        
        // Load color settings separately (combine only supports 5 flows)
        combine(
            settingsDataStore.gradientStartColor,
            settingsDataStore.gradientEndColor,
            settingsDataStore.iconAccentColor
        ) { gradientStart, gradientEnd, iconAccent ->
            _uiState.update { state ->
                state.copy(
                    gradientStartColor = gradientStart,
                    gradientEndColor = gradientEnd,
                    iconAccentColor = iconAccent
                )
            }
        }
            .launchIn(viewModelScope)
    }
    
    /**
     * Load data counts for statistics display.
     */
    private fun loadDataCounts() {
        viewModelScope.launch {
            try {
                val categoryCount = categoryRepository.getCategoryCount()
                val statCount = statRepository.getTotalStatCount()
                val recordCount = recordRepository.getTotalRecordCount()
                
                _uiState.update { state ->
                    state.copy(
                        categoryCount = categoryCount,
                        statCount = statCount,
                        recordCount = recordCount
                    )
                }
            } catch (e: Exception) {
                // Non-critical, don't show error
            }
        }
    }
    
    /**
     * Set auto-capture location setting.
     */
    fun setAutoCaptureLocation(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setAutoCaptureLocation(enabled)
        }
    }
    
    /**
     * Set theme mode.
     */
    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsDataStore.setThemeMode(mode)
        }
    }
    
    /**
     * Set default sort option.
     */
    fun setDefaultSortOption(sortOption: SortOption) {
        viewModelScope.launch {
            settingsDataStore.setDefaultSortOption(sortOption)
        }
    }
    
    /**
     * Set date format.
     */
    fun setDateFormat(format: String) {
        viewModelScope.launch {
            settingsDataStore.setDateFormat(format)
        }
    }
    
    /**
     * Set use dynamic colors.
     */
    fun setUseDynamicColors(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setUseDynamicColors(enabled)
        }
    }
    
    /**
     * Set gradient start color (left side).
     */
    fun setGradientStartColor(color: Long) {
        viewModelScope.launch {
            settingsDataStore.setGradientStartColor(color)
        }
    }
    
    /**
     * Set gradient end color (right side).
     */
    fun setGradientEndColor(color: Long) {
        viewModelScope.launch {
            settingsDataStore.setGradientEndColor(color)
        }
    }
    
    /**
     * Set icon/accent color.
     */
    fun setIconAccentColor(color: Long) {
        viewModelScope.launch {
            settingsDataStore.setIconAccentColor(color)
        }
    }
    
    /**
     * Export all data to JSON.
     */
    fun exportData() {
        viewModelScope.launch {
            _exportImportState.update { it.copy(isExporting = true, error = null) }
            try {
                // Get categories synchronously for export
                val allCategories = mutableListOf<StatCategory>()
                categoryRepository.getAllCategories().collect {
                    allCategories.clear()
                    allCategories.addAll(it)
                }
                
                val allStats = statRepository.getAllStats()
                val allRecords = recordRepository.getAllRecords()
                
                val exportData = ExportData(
                    version = 3, // Version 3 supports data point IDs for stable ordering
                    exportedAt = System.currentTimeMillis(),
                    categories = allCategories.map { category ->
                        ExportCategory(
                            id = category.id,
                            title = category.title,
                            icon = category.icon,
                            sortOrder = category.sortOrder,
                            stats = allStats.filter { it.categoryId == category.id }.map { stat ->
                                ExportStat(
                                    id = stat.id,
                                    name = stat.name,
                                    dataPoints = stat.dataPoints.map { dp ->
                                        ExportDataPoint(
                                            id = dp.id,
                                            label = dp.label,
                                            type = dp.type.name,
                                            unit = dp.unit,
                                            minValue = dp.minValue,
                                            maxValue = dp.maxValue,
                                            step = dp.step
                                        )
                                    },
                                    sortOrder = stat.sortOrder,
                                    records = allRecords.filter { it.statId == stat.id }.map { record ->
                                        ExportRecord(
                                            id = record.id,
                                            values = record.values.map { v ->
                                                ExportDataPointValue(
                                                    dataPointId = v.dataPointId,
                                                    dataPointIndex = v.dataPointIndex,
                                                    value = v.value
                                                )
                                            },
                                            notes = record.notes,
                                            latitude = record.latitude,
                                            longitude = record.longitude,
                                            locationName = record.locationName,
                                            recordedAt = record.recordedAt
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                
                val jsonString = json.encodeToString(exportData)
                _exportImportState.update { state ->
                    state.copy(
                        isExporting = false,
                        exportSuccess = true,
                        exportData = jsonString
                    )
                }
            } catch (e: Exception) {
                _exportImportState.update { state ->
                    state.copy(
                        isExporting = false,
                        error = e.message ?: "Export failed"
                    )
                }
            }
        }
    }
    
    /**
     * Import data from JSON.
     */
    fun importData(jsonString: String) {
        viewModelScope.launch {
            _exportImportState.update { it.copy(isImporting = true, error = null) }
            try {
                val exportData = json.decodeFromString<ExportData>(jsonString)
                
                // Clear existing data
                categoryRepository.deleteAllCategories()
                
                // Import categories, stats, and records
                for (exportCategory in exportData.categories) {
                    val category = StatCategory(
                        title = exportCategory.title,
                        icon = exportCategory.icon,
                        sortOrder = exportCategory.sortOrder
                    )
                    val categoryId = categoryRepository.saveCategory(category)
                    
                    for (exportStat in exportCategory.stats) {
                        // Create data points with IDs if available, otherwise generate new ones
                        val dataPoints = exportStat.dataPoints.map { dp ->
                            DataPoint(
                                id = dp.id.takeIf { it.isNotEmpty() } ?: java.util.UUID.randomUUID().toString(),
                                label = dp.label,
                                type = StatType.fromString(dp.type),
                                unit = dp.unit,
                                minValue = dp.minValue,
                                maxValue = dp.maxValue,
                                step = dp.step
                            )
                        }
                        
                        val stat = Stat(
                            categoryId = categoryId,
                            name = exportStat.name,
                            dataPoints = dataPoints,
                            sortOrder = exportStat.sortOrder
                        )
                        val statId = statRepository.saveStat(stat)
                        
                        // Reload stat to get the saved data points with their IDs
                        val savedStat = statRepository.getStatById(statId)
                        
                        for (exportRecord in exportStat.records) {
                            // Create values using dataPointId if available, otherwise fall back to index
                            val values = exportRecord.values.mapIndexed { index, v ->
                                if (v.dataPointId.isNotEmpty()) {
                                    // Map old dataPointId to new dataPointId based on position
                                    // Find which export data point had this ID
                                    val exportDpIndex = exportStat.dataPoints.indexOfFirst { it.id == v.dataPointId }
                                    val newDataPointId = if (exportDpIndex >= 0 && savedStat != null) {
                                        savedStat.dataPoints.getOrNull(exportDpIndex)?.id ?: v.dataPointId
                                    } else {
                                        v.dataPointId
                                    }
                                    DataPointValue.forDataPoint(newDataPointId, v.value)
                                } else if (v.dataPointIndex >= 0) {
                                    // Legacy: use index to find new data point ID
                                    val newDataPointId = savedStat?.dataPoints?.getOrNull(v.dataPointIndex)?.id ?: ""
                                    if (newDataPointId.isNotEmpty()) {
                                        DataPointValue.forDataPoint(newDataPointId, v.value)
                                    } else {
                                        DataPointValue.fromIndex(v.dataPointIndex, v.value)
                                    }
                                } else {
                                    // Fallback: use index from position
                                    val newDataPointId = savedStat?.dataPoints?.getOrNull(index)?.id ?: ""
                                    if (newDataPointId.isNotEmpty()) {
                                        DataPointValue.forDataPoint(newDataPointId, v.value)
                                    } else {
                                        DataPointValue.fromIndex(index, v.value)
                                    }
                                }
                            }
                            
                            val record = StatRecord(
                                statId = statId,
                                values = values,
                                notes = exportRecord.notes,
                                latitude = exportRecord.latitude,
                                longitude = exportRecord.longitude,
                                locationName = exportRecord.locationName,
                                recordedAt = exportRecord.recordedAt
                            )
                            recordRepository.saveRecord(record)
                        }
                    }
                }
                
                loadDataCounts()
                _exportImportState.update { state ->
                    state.copy(isImporting = false, importSuccess = true)
                }
            } catch (e: Exception) {
                _exportImportState.update { state ->
                    state.copy(
                        isImporting = false,
                        error = e.message ?: "Import failed"
                    )
                }
            }
        }
    }
    
    /**
     * Clear export/import state.
     */
    fun clearExportImportState() {
        _exportImportState.value = ExportImportUiState()
    }
    
    /**
     * Clear error message.
     */
    fun clearError() {
        _uiState.update { state ->
            state.copy(error = null)
        }
        _exportImportState.update { state ->
            state.copy(error = null)
        }
    }
}

// Export data classes
@Serializable
data class ExportData(
    val version: Int,
    val exportedAt: Long,
    val categories: List<ExportCategory>
)

@Serializable
data class ExportCategory(
    val id: Long,
    val title: String,
    val icon: String,
    val sortOrder: Int,
    val stats: List<ExportStat>
)

@Serializable
data class ExportStat(
    val id: Long,
    val name: String,
    val dataPoints: List<ExportDataPoint>,
    val sortOrder: Int,
    val records: List<ExportRecord>
)

@Serializable
data class ExportDataPoint(
    val id: String = "",  // Unique stable ID for the data point
    val label: String,
    val type: String,
    val unit: String = "",
    val minValue: Double? = null,
    val maxValue: Double? = null,
    val step: Double? = null
)

@Serializable
data class ExportRecord(
    val id: Long,
    val values: List<ExportDataPointValue>,
    val notes: String?,
    val latitude: Double?,
    val longitude: Double?,
    val locationName: String?,
    val recordedAt: Long
)

@Serializable
data class ExportDataPointValue(
    val dataPointId: String = "",  // New ID-based field
    val dataPointIndex: Int = -1,  // Legacy index field for backward compatibility
    val value: String
)
