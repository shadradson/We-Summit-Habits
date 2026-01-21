package com.humangamestats.model

/**
 * Enum representing the different types of stats that can be tracked.
 * 
 * @property displayName User-friendly name shown in the UI
 * @property defaultLabel Default label for the stat type (can be customized by user)
 */
enum class StatType(
    val displayName: String,
    val defaultLabel: String
) {
    /**
     * Numeric value - can be integers or decimals.
     * Examples: weight, reps, calories, etc.
     */
    NUMBER("Number", "Value"),
    
    /**
     * Time duration in milliseconds.
     * Displayed as hours:minutes:seconds.
     * Examples: workout duration, sleep time, etc.
     */
    DURATION("Duration", "Time"),
    
    /**
     * Rating from 1 to 5.
     * Examples: mood rating, difficulty level, etc.
     */
    RATING("Rating (1-5)", "Rating"),
    
    /**
     * Boolean checkbox value (true/false).
     * Examples: completed task, did workout, etc.
     */
    CHECKBOX("Checkbox", "Completed");
    
    companion object {
        /**
         * Get StatType from its name string (case-insensitive).
         * Returns NUMBER as default if not found.
         */
        fun fromString(value: String): StatType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: NUMBER
        }
    }
}
