package com.humangamestats.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a stat category.
 * Categories are top-level containers that group related stats together.
 *
 * Examples: "Workout", "Food", "Sleep", "Mood"
 *
 * @property id Unique identifier for the category
 * @property title Display name of the category
 * @property icon Icon identifier for visual representation
 * @property sortOrder Order in which categories appear in the list
 * @property defaultSortOption Default sort option when opening this category (null = RECENT)
 * @property createdAt Timestamp when the category was created
 * @property updatedAt Timestamp when the category was last updated
 */
@Serializable
data class StatCategory(
    val id: Long = 0,
    val title: String,
    val icon: String = "category",
    val sortOrder: Int = 0,
    val defaultSortOption: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        /**
         * Available icons for categories.
         * Using Material Icons names.
         */
        val availableIcons = listOf(
            "fitness_center",    // Workout
            "restaurant",        // Food
            "bedtime",           // Sleep
            "mood",              // Mood
            "water_drop",        // Hydration
            "directions_run",    // Cardio
            "self_improvement",  // Mindfulness
            "medication",        // Health/Medicine
            "work",              // Work
            "school",            // Learning
            "attach_money",      // Finance
            "favorite",          // General tracking
            "star",              // Goals
            "category"           // Default
        )
    }
}
