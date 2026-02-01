package com.humangamestats.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a stat within a category.
 * Stats define what is being tracked and can have multiple data points.
 * 
 * Examples: 
 * - "Bench Press" with data points: Weight (NUMBER), Reps (NUMBER)
 * - "Sleep" with data points: Duration (DURATION), Quality (RATING)
 * - "Did Workout" with data points: Completed (CHECKBOX)
 * 
 * @property id Unique identifier for the stat
 * @property categoryId ID of the parent category this stat belongs to
 * @property name Display name of the stat
 * @property dataPoints List of data points this stat tracks
 * @property templateId Optional reference to the template this stat was created from
 * @property sortOrder Order in which stats appear in the list within a category
 * @property createdAt Timestamp when the stat was created
 * @property updatedAt Timestamp when the stat was last updated
 */
@Serializable
data class Stat(
    val id: Long = 0,
    val categoryId: Long,
    val name: String,
    val dataPoints: List<DataPoint> = listOf(DataPoint.number("Value")),
    val templateId: Long? = null,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Get a formatted display string summarizing the data points.
     * Example: "Weight (lbs), Reps"
     */
    val dataPointsSummary: String
        get() = dataPoints.joinToString(", ") { dp ->
            if (dp.unit.isNotEmpty()) "${dp.label} (${dp.unit})" else dp.label
        }
    
    /**
     * Get a short display string for the stat type.
     * Shows the first data point's info for backwards compatibility.
     */
    val typeDisplayString: String
        get() = dataPoints.firstOrNull()?.let { dp ->
            "${dp.type.displayName} (${dp.label})"
        } ?: "No data points"
    
    /**
     * Check if this stat supports numeric sorting (highest/lowest).
     * True if the first data point is numeric.
     */
    val supportsNumericSorting: Boolean
        get() = dataPoints.firstOrNull()?.type in listOf(
            StatType.NUMBER, StatType.DURATION, StatType.RATING
        )
    
    /**
     * Check if this stat has multiple data points.
     */
    val hasMultipleDataPoints: Boolean
        get() = dataPoints.size > 1
    
    /**
     * Get the primary data point (first one).
     */
    val primaryDataPoint: DataPoint?
        get() = dataPoints.firstOrNull()
    
    /**
     * Legacy compatibility: get the stat type from the first data point.
     */
    @Deprecated("Use dataPoints instead")
    val statType: StatType
        get() = dataPoints.firstOrNull()?.type ?: StatType.NUMBER
    
    /**
     * Legacy compatibility: get the type label from the first data point.
     */
    @Deprecated("Use dataPoints instead")
    val typeLabel: String
        get() = dataPoints.firstOrNull()?.label ?: ""
    
    companion object {
        /**
         * Create a stat with a single data point (backward compatible).
         */
        fun simple(
            categoryId: Long,
            name: String,
            type: StatType,
            label: String = type.defaultLabel,
            unit: String = ""
        ): Stat = Stat(
            categoryId = categoryId,
            name = name,
            dataPoints = listOf(DataPoint(label = label, type = type, unit = unit))
        )
        
        /**
         * Create a stat from a template.
         */
        fun fromTemplate(
            categoryId: Long,
            name: String,
            template: DataPointTemplate
        ): Stat = Stat(
            categoryId = categoryId,
            name = name,
            dataPoints = template.dataPoints,
            templateId = template.id
        )
    }
}

/**
 * Stat with additional computed data like record count and latest value.
 * Used for displaying stats in lists with summary information.
 */
@Serializable
data class StatWithSummary(
    val stat: Stat,
    val recordCount: Int = 0,
    val todayRecordCount: Int = 0,
    val latestValues: List<String> = emptyList(),
    val latestRecordedAt: Long? = null
) {
    /**
     * Get the latest value formatted as a string.
     * For multiple data points, joins them with " | ".
     */
    val latestValueDisplay: String?
        get() = if (latestValues.isNotEmpty()) {
            stat.dataPoints.mapIndexed { index, dp ->
                val value = latestValues.getOrNull(index) ?: ""
                if (dp.unit.isNotEmpty() && value.isNotEmpty()) {
                    "$value ${dp.unit}"
                } else {
                    value
                }
            }.filter { it.isNotEmpty() }.joinToString(" | ")
        } else null
    
    /**
     * Legacy compatibility: get the latest value as a single string.
     */
    @Deprecated("Use latestValues instead")
    val latestValue: String?
        get() = latestValues.firstOrNull()
}
