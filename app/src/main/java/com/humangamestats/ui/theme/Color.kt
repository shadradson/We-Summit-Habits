package com.humangamestats.ui.theme

import androidx.compose.ui.graphics.Color

// Main theme colors - Cyan/Teal theme
val primaryLight = Color(0xFF52C2EC)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFB8EAFF)
val onPrimaryContainerLight = Color(0xFF001F29)
val secondaryLight = Color(0xFF115D68)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFBCECF7)
val onSecondaryContainerLight = Color(0xFF001F24)
val tertiaryLight = Color(0xFF386472)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFB9E7F7)
val onTertiaryContainerLight = Color(0xFF00201D)
val errorLight = Color(0xFFBA1AB7)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFF6FAFD)
val onBackgroundLight = Color(0xFF171C1F)
val surfaceLight = Color(0xFFFBF8FF)
val onSurfaceLight = Color(0xFF1B1B21)
val surfaceVariantLight = Color(0xFFDCE3E9)
val onSurfaceVariantLight = Color(0xFF40484C)
val outlineLight = Color(0xFF70787D)
val outlineVariantLight = Color(0xFFC0C8CD)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF303036)
val inverseOnSurfaceLight = Color(0xFFF2EFF7)
val inversePrimaryLight = Color(0xFF67D3FD)

// Dark theme colors
val primaryDark = Color(0xFF52C2EC)
val onPrimaryDark = Color(0xFF003545)
val primaryContainerDark = Color(0xFF004D63)
val onPrimaryContainerDark = Color(0xFF52C2EC)
val secondaryDark = Color(0xFF90D1DD)
val onSecondaryDark = Color(0xFF00363D)
val secondaryContainerDark = Color(0xFF004F58)
val onSecondaryContainerDark = Color(0xFFBCECF7)
val tertiaryDark = Color(0xFF9DCBD9)
val onTertiaryDark = Color(0xFF003642)
val tertiaryContainerDark = Color(0xFF1E4D58)
val onTertiaryContainerDark = Color(0xFFB9E7F7)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690046)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF0F1416)
val onBackgroundDark = Color(0xFFDFE3E6)
val surfaceDark = Color(0xFF131318)
val onSurfaceDark = Color(0xFFE4E1E9)
val surfaceVariantDark = Color(0xFF40484C)
val onSurfaceVariantDark = Color(0xFFC0C8CD)
val outlineDark = Color(0xFF8A9297)
val outlineVariantDark = Color(0xFF40484C)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE4E1E9)
val inverseOnSurfaceDark = Color(0xFF303036)
val inversePrimaryDark = Color(0xFF006782)

// Colors for stat types - used throughout the app
val StatTypeNumber = Color(0xFF2196F3)    // Blue for numbers
val StatTypeDuration = Color(0xFF943EE1)  // Purple for duration
val StatTypeRating = Color(0xFFE5741B)    // Amber for ratings
val StatTypeCheckbox = Color(0xFF1CB7B2)  // Green for checkboxes

// Chart colors
val ChartPrimary = Color(0xFF006782)
val ChartSecondary = Color(0xFF67D3FD)
val ChartAccent = Color(0xFF9DCBD9)
val ChartLine = Color(0xFF006782)
val ChartFill = Color(0x3367D3FD)  // Semi-transparent cyan
val ChartGrid = Color(0xFFDCE3E9)

// Default data point chart colors — cycle through these when no color is specified
val defaultDataPointColors = listOf(
    Color(0xFF4FC3F7), // Light Blue
    Color(0xFFEF9A9A), // Salmon/Red
    Color(0xFFA5D6A7), // Light Green
    Color(0xFFFFCC80), // Light Orange
)

// Preset color options shown in the color picker (null = Auto)
val presetDataPointColors: List<String?> = listOf(
    null,        // Auto (uses default cycling)
    "#E53935",   // Red
    "#FB8C00",   // Orange
    "#FDD835",   // Yellow
    "#43A047",   // Green
    "#4FC3F7",   // Light Blue
    "#1E88E5",   // Blue
    "#8E24AA",   // Purple
    "#F06292",   // Pink
)

/**
 * Convert a hex color string to a Compose Color.
 * Falls back to the default cycling color at [fallbackIndex] if null or unparseable.
 */
fun String?.toChartColor(fallbackIndex: Int = 0): Color {
    if (this == null) return defaultDataPointColors[fallbackIndex % defaultDataPointColors.size]
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        defaultDataPointColors[fallbackIndex % defaultDataPointColors.size]
    }
}
