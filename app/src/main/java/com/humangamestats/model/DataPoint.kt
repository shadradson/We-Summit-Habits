package com.humangamestats.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents a single data point definition within a stat.
 * A stat can have multiple data points (e.g., Bench Press has "Weight" and "Reps").
 *
 * @property id Unique stable identifier for this data point (persists through reordering)
 * @property label The display label for this data point (e.g., "Weight", "Reps")
 * @property type The type of data this point collects
 * @property unit Optional unit label (e.g., "lbs", "kg", "minutes")
 * @property minValue Optional minimum value for NUMBER type
 * @property maxValue Optional maximum value for NUMBER type
 * @property step Optional step value for NUMBER type (increment/decrement amount)
 */
@Serializable
data class DataPoint(
    val id: String = UUID.randomUUID().toString(),
    val label: String,
    val type: StatType,
    val unit: String = "",
    val minValue: Double? = null,
    val maxValue: Double? = null,
    val step: Double? = null
) {
    companion object {
        /**
         * Create a simple number data point.
         */
        fun number(
            label: String,
            unit: String = "",
            minValue: Double? = null,
            maxValue: Double? = null,
            step: Double? = null
        ): DataPoint = DataPoint(
            label = label,
            type = StatType.NUMBER,
            unit = unit,
            minValue = minValue,
            maxValue = maxValue,
            step = step
        )
        
        /**
         * Create a duration data point.
         */
        fun duration(label: String): DataPoint =
            DataPoint(label = label, type = StatType.DURATION)
        
        /**
         * Create a rating (1-5) data point.
         */
        fun rating(label: String): DataPoint =
            DataPoint(label = label, type = StatType.RATING)
        
        /**
         * Create a checkbox (boolean) data point.
         */
        fun checkbox(label: String): DataPoint =
            DataPoint(label = label, type = StatType.CHECKBOX)
    }
}

/**
 * Represents a value entered for a data point in a record.
 * The value is stored as a string to support all types.
 *
 * @property dataPointId The unique ID of the data point this value belongs to
 * @property value The actual value as a string
 * @property dataPointIndex Legacy: Index of the data point (for backward compatibility)
 */
@Serializable
data class DataPointValue(
    val dataPointId: String = "",
    val value: String,
    @Deprecated("Use dataPointId instead. Kept for backward compatibility with existing data.")
    val dataPointIndex: Int = -1
) {
    /**
     * Get the value as a number, or null if not a valid number.
     */
    fun asNumber(): Double? = value.toDoubleOrNull()
    
    /**
     * Get the value as a boolean.
     */
    fun asBoolean(): Boolean = value.toBooleanStrictOrNull() ?: false
    
    /**
     * Get the value as a duration in seconds.
     */
    fun asDurationSeconds(): Long? = value.toLongOrNull()
    
    /**
     * Get the value as a rating (1-5).
     */
    fun asRating(): Int? = value.toIntOrNull()?.coerceIn(1, 5)
    
    /**
     * Check if this value uses the new ID-based system.
     */
    fun hasDataPointId(): Boolean = dataPointId.isNotEmpty()
    
    companion object {
        /**
         * Create a DataPointValue with a data point ID.
         */
        fun forDataPoint(dataPointId: String, value: String): DataPointValue =
            DataPointValue(dataPointId = dataPointId, value = value)
        
        fun fromNumber(dataPointId: String, value: Double): DataPointValue =
            DataPointValue(dataPointId = dataPointId, value = value.toString())
        
        fun fromBoolean(dataPointId: String, value: Boolean): DataPointValue =
            DataPointValue(dataPointId = dataPointId, value = value.toString())
        
        fun fromDuration(dataPointId: String, seconds: Long): DataPointValue =
            DataPointValue(dataPointId = dataPointId, value = seconds.toString())
        
        fun fromRating(dataPointId: String, rating: Int): DataPointValue =
            DataPointValue(dataPointId = dataPointId, value = rating.coerceIn(1, 5).toString())
        
        // Legacy methods for backward compatibility
        @Deprecated("Use forDataPoint instead")
        fun fromIndex(index: Int, value: String): DataPointValue =
            DataPointValue(dataPointId = "", value = value, dataPointIndex = index)
        
        @Deprecated("Use fromNumber with dataPointId instead")
        fun fromNumber(index: Int, value: Double): DataPointValue =
            DataPointValue(dataPointId = "", value = value.toString(), dataPointIndex = index)
        
        @Deprecated("Use fromBoolean with dataPointId instead")
        fun fromBoolean(index: Int, value: Boolean): DataPointValue =
            DataPointValue(dataPointId = "", value = value.toString(), dataPointIndex = index)
        
        @Deprecated("Use fromDuration with dataPointId instead")
        fun fromDuration(index: Int, seconds: Long): DataPointValue =
            DataPointValue(dataPointId = "", value = seconds.toString(), dataPointIndex = index)
        
        @Deprecated("Use fromRating with dataPointId instead")
        fun fromRating(index: Int, rating: Int): DataPointValue =
            DataPointValue(dataPointId = "", value = rating.coerceIn(1, 5).toString(), dataPointIndex = index)
    }
}
