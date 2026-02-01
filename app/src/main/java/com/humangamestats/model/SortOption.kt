package com.humangamestats.model

/**
 * Enum representing sorting options for stats and records.
 *
 * @property displayName User-friendly name shown in the UI
 * @property ascending Whether this sort should be ascending by default
 */
enum class SortOption(
    val displayName: String,
    val ascending: Boolean = false
) {
    /**
     * Sort by most recent record entry (newest first).
     */
    RECENT("Most Recent Entries", ascending = false),
    
    /**
     * Sort by oldest record entry (oldest first).
     */
    OLDEST("Least Recent Entries", ascending = true),
    
    /**
     * Sort by highest numeric value (highest first).
     * Only applicable to NUMBER stat type records.
     */
    HIGHEST("Highest Value", ascending = false),
    
    /**
     * Sort by lowest numeric value (lowest first).
     * Only applicable to NUMBER stat type records.
     */
    LOWEST("Lowest Value", ascending = true),
    
    /**
     * Sort alphabetically by name (A-Z).
     */
    ALPHABETICAL("Alphabetical (A-Z)", ascending = true),
    
    /**
     * Sort alphabetically reversed (Z-A).
     */
    ALPHABETICAL_DESC("Alphabetical (Z-A)", ascending = false);
    
    companion object {
        /**
         * Get SortOption from its name string (case-insensitive).
         * Returns RECENT as default if not found.
         */
        fun fromString(value: String): SortOption {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: RECENT
        }
        
        /**
         * Get sorting options applicable for a stat list (within a category).
         * Does not include value-based sorting since that's for records.
         */
        fun forStatList(): List<SortOption> {
            return listOf(RECENT, OLDEST, ALPHABETICAL, ALPHABETICAL_DESC)
        }
        
        /**
         * Get sorting options applicable to records of a specific stat type.
         */
        fun forRecords(statType: StatType): List<SortOption> {
            return when (statType) {
                StatType.NUMBER -> listOf(RECENT, OLDEST, HIGHEST, LOWEST)
                StatType.DURATION -> listOf(RECENT, OLDEST, HIGHEST, LOWEST)
                StatType.RATING -> listOf(RECENT, OLDEST, HIGHEST, LOWEST)
                StatType.CHECKBOX -> listOf(RECENT, OLDEST)
            }
        }
        
        /**
         * Get sorting options applicable to a specific stat type.
         * @deprecated Use forRecords() instead
         */
        @Deprecated("Use forRecords() instead", ReplaceWith("forRecords(statType)"))
        fun forStatType(statType: StatType): List<SortOption> = forRecords(statType)
    }
}
