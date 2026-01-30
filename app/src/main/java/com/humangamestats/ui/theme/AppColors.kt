package com.humangamestats.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Custom app colors for gradients and accents.
 * These are separate from Material Theme colors and can be customized by the user.
 */
data class AppColors(
    val gradientStart: Color = Color(0xFF6200EE),
    val gradientEnd: Color = Color(0xFF3700B3),
    val iconAccent: Color = Color(0xFF6200EE)
) {
    companion object {
        val Default = AppColors()
        
        /**
         * Create AppColors from Long color values (ARGB format).
         */
        fun fromLongs(
            gradientStart: Long,
            gradientEnd: Long,
            iconAccent: Long
        ): AppColors = AppColors(
            gradientStart = Color(gradientStart),
            gradientEnd = Color(gradientEnd),
            iconAccent = Color(iconAccent)
        )
    }
}

/**
 * CompositionLocal for providing custom app colors throughout the app.
 */
val LocalAppColors = staticCompositionLocalOf { AppColors.Default }

/**
 * Extension property to easily access app colors.
 */
val androidx.compose.material3.MaterialTheme.appColors: AppColors
    @Composable
    get() = LocalAppColors.current
