package com.humangamestats.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.humangamestats.model.SortOption
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Extension property to create DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * DataStore for storing app settings and preferences.
 * Uses Jetpack DataStore Preferences for type-safe, asynchronous storage.
 */
@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore
    
    /**
     * Get the auto-capture location setting.
     * Default: true
     */
    val autoCaptureLocation: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTO_CAPTURE_LOCATION] ?: true
    }
    
    /**
     * Set the auto-capture location setting.
     */
    suspend fun setAutoCaptureLocation(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_CAPTURE_LOCATION] = enabled
        }
    }
    
    /**
     * Get the theme mode setting.
     * Default: SYSTEM
     */
    val themeMode: Flow<ThemeMode> = dataStore.data.map { preferences ->
        val mode = preferences[THEME_MODE] ?: ThemeMode.SYSTEM.name
        ThemeMode.fromString(mode)
    }
    
    /**
     * Set the theme mode setting.
     */
    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode.name
        }
    }
    
    /**
     * Get the default sort option setting.
     * Default: RECENT
     */
    val defaultSortOption: Flow<SortOption> = dataStore.data.map { preferences ->
        val sort = preferences[DEFAULT_SORT_OPTION] ?: SortOption.RECENT.name
        SortOption.fromString(sort)
    }
    
    /**
     * Set the default sort option setting.
     */
    suspend fun setDefaultSortOption(sortOption: SortOption) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_SORT_OPTION] = sortOption.name
        }
    }
    
    /**
     * Get the date format setting.
     * Default: "MMM dd, yyyy"
     */
    val dateFormat: Flow<String> = dataStore.data.map { preferences ->
        preferences[DATE_FORMAT] ?: DEFAULT_DATE_FORMAT
    }
    
    /**
     * Set the date format setting.
     */
    suspend fun setDateFormat(format: String) {
        dataStore.edit { preferences ->
            preferences[DATE_FORMAT] = format
        }
    }
    
    /**
     * Get the use dynamic colors setting (Material You).
     * Default: true
     */
    val useDynamicColors: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[USE_DYNAMIC_COLORS] ?: true
    }
    
    /**
     * Set the use dynamic colors setting.
     */
    suspend fun setUseDynamicColors(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[USE_DYNAMIC_COLORS] = enabled
        }
    }
    
    /**
     * Clear all settings (reset to defaults).
     */
    suspend fun clearAllSettings() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    companion object {
        // Preference keys
        private val AUTO_CAPTURE_LOCATION = booleanPreferencesKey("auto_capture_location")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val DEFAULT_SORT_OPTION = stringPreferencesKey("default_sort_option")
        private val DATE_FORMAT = stringPreferencesKey("date_format")
        private val USE_DYNAMIC_COLORS = booleanPreferencesKey("use_dynamic_colors")
        
        // Default values
        const val DEFAULT_DATE_FORMAT = "MMM dd, yyyy"
        
        // Available date formats
        val DATE_FORMATS = listOf(
            "MMM dd, yyyy",      // Jan 19, 2026
            "dd MMM yyyy",       // 19 Jan 2026
            "yyyy-MM-dd",        // 2026-01-19
            "MM/dd/yyyy",        // 01/19/2026
            "dd/MM/yyyy"         // 19/01/2026
        )
    }
}

/**
 * Theme mode options.
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM;
    
    companion object {
        fun fromString(value: String): ThemeMode {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: SYSTEM
        }
    }
}
