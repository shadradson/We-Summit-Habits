package com.humangamestats.model

import kotlinx.serialization.Serializable

/**
 * Represents a single data point definition within a stat.
 * A stat can have multiple data points (e.g., Bench Press has "Weight" and "Reps").
 * 
 * @property label The display label for this data point (e.g., "Weight", "Reps")
 * @property type The type of data this point collects
 * @property unit Optional unit label (e.g., "lbs", "kg", "minutes")
 * @property minValue Optional minimum value for NUMBER type
 * @property maxValue Optional maximum value for NUMBER type
 * @property step Optional step value for NUMBER type (increment/decrement amount)
 */
@Serializable
data class DataPoint(
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
 * @property dataPointIndex Index of the data point this value belongs to
 * @property value The actual value as a string
 */
@Serializable
data class DataPointValue(
    val dataPointIndex: Int,
    val value: String
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
    
    companion object {
        fun fromNumber(index: Int, value: Double): DataPointValue =
            DataPointValue(index, value.toString())
        
        fun fromBoolean(index: Int, value: Boolean): DataPointValue =
            DataPointValue(index, value.toString())
        
        fun fromDuration(index: Int, seconds: Long): DataPointValue =
            DataPointValue(index, seconds.toString())
        
        fun fromRating(index: Int, rating: Int): DataPointValue =
            DataPointValue(index, rating.coerceIn(1, 5).toString())
    }
}
