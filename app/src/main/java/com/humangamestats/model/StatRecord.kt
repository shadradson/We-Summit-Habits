package com.humangamestats.model

import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Domain model representing a single record entry for a stat.
 * Records are the actual data points tracked over time.
 * 
 * @property id Unique identifier for the record
 * @property statId ID of the parent stat this record belongs to
 * @property values List of values corresponding to each data point in the stat
 * @property notes Optional user notes for this record
 * @property latitude GPS latitude coordinate (null if location not captured)
 * @property longitude GPS longitude coordinate (null if location not captured)
 * @property locationName Reverse-geocoded address or location name
 * @property recordedAt Timestamp when the record was taken (user-facing date/time)
 * @property createdAt Timestamp when the record was created in the database
 * @property updatedAt Timestamp when the record was last updated
 */
@Serializable
data class StatRecord(
    val id: Long = 0,
    val statId: Long,
    val values: List<DataPointValue> = emptyList(),
    val notes: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationName: String? = null,
    val recordedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Legacy compatibility: get the first value as a string.
     */
    @Deprecated("Use values instead")
    val value: String
        get() = values.firstOrNull()?.value ?: ""
    
    /**
     * Check if this record has location data.
     */
    val hasLocation: Boolean
        get() = latitude != null && longitude != null
    
    /**
     * Check if this record uses the new ID-based data point system.
     */
    val usesDataPointIds: Boolean
        get() = values.any { it.hasDataPointId() }
    
    /**
     * Get value for a specific data point by ID.
     */
    fun getValueForDataPoint(dataPointId: String): String? {
        return values.find { it.dataPointId == dataPointId }?.value
    }
    
    /**
     * Get numeric value for a specific data point by ID.
     */
    fun getNumericValueForDataPoint(dataPointId: String): Double? {
        return getValueForDataPoint(dataPointId)?.toDoubleOrNull()
    }
    
    /**
     * Get value for a specific data point by index (legacy fallback).
     * Tries ID first, then falls back to index for backward compatibility.
     */
    fun getValueAt(index: Int, dataPoints: List<DataPoint>? = null): String? {
        // If we have dataPoints and this record uses IDs, look up by ID
        if (dataPoints != null && usesDataPointIds) {
            val dataPoint = dataPoints.getOrNull(index)
            if (dataPoint != null) {
                return getValueForDataPoint(dataPoint.id)
            }
        }
        // Fallback to index-based lookup for legacy data
        return values.find { it.dataPointIndex == index }?.value
    }
    
    /**
     * Get numeric value for a specific data point index.
     */
    fun getNumericValueAt(index: Int, dataPoints: List<DataPoint>? = null): Double? {
        return getValueAt(index, dataPoints)?.toDoubleOrNull()
    }
    
    /**
     * Get all values as a list of strings in the order of the provided data points.
     */
    fun getOrderedValues(dataPoints: List<DataPoint>): List<String> {
        return dataPoints.map { dp ->
            getValueForDataPoint(dp.id)
                ?: values.find { it.dataPointIndex == dataPoints.indexOf(dp) }?.value
                ?: ""
        }
    }
    
    /**
     * Get all values as a list of strings in order (legacy).
     */
    @Deprecated("Use getOrderedValues(dataPoints) instead for proper ID-based lookup")
    fun getOrderedValues(): List<String> {
        return (0 until values.size).map { index ->
            values.find { it.dataPointIndex == index }?.value ?: ""
        }
    }
    
    /**
     * Get formatted date string for display.
     */
    fun getFormattedDate(pattern: String = "MMM dd, yyyy"): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(Date(recordedAt))
    }
    
    /**
     * Get formatted time string for display.
     */
    fun getFormattedTime(pattern: String = "h:mm a"): String {
        val timeFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return timeFormat.format(Date(recordedAt))
    }
    
    /**
     * Get formatted date and time string for display.
     */
    fun getFormattedDateTime(pattern: String = "MMM dd, yyyy h:mm a"): String {
        val dateTimeFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateTimeFormat.format(Date(recordedAt))
    }
    
    /**
     * Format values for display with data point info.
     * Uses ID-based lookup with fallback to index for legacy data.
     */
    fun formatValuesForDisplay(dataPoints: List<DataPoint>): String {
        return dataPoints.mapIndexed { index, dp ->
            // Try ID-based lookup first, then fallback to index
            val value = getValueForDataPoint(dp.id)
                ?: values.find { it.dataPointIndex == index }?.value
                ?: ""
            when (dp.type) {
                StatType.NUMBER -> {
                    val num = value.toDoubleOrNull()
                    if (num != null) {
                        val formatted = if (num == num.toLong().toDouble()) {
                            num.toLong().toString()
                        } else {
                            "%.2f".format(num)
                        }
                        if (dp.unit.isNotEmpty()) "$formatted ${dp.unit}" else formatted
                    } else value
                }
                StatType.DURATION -> {
                    val millis = value.toLongOrNull() ?: 0L
                    formatDuration(millis)
                }
                StatType.RATING -> {
                    val rating = value.toIntOrNull() ?: 0
                    "★".repeat(rating) + "☆".repeat(5 - rating)
                }
                StatType.CHECKBOX -> {
                    if (value.toBooleanStrictOrNull() == true) "✓" else "✗"
                }
            }
        }.joinToString(" | ")
    }
    
    companion object {
        /**
         * Create a record with values for the given data points (new ID-based).
         */
        fun create(statId: Long, dataPoints: List<DataPoint>, valueStrings: List<String>): StatRecord {
            return StatRecord(
                statId = statId,
                values = dataPoints.mapIndexed { index, dp ->
                    DataPointValue.forDataPoint(dp.id, valueStrings.getOrElse(index) { "" })
                }
            )
        }
        
        /**
         * Create a record with a single value (backward compatible).
         */
        @Deprecated("Use create() with data points for ID-based values")
        fun single(statId: Long, value: String): StatRecord {
            return StatRecord(
                statId = statId,
                values = listOf(DataPointValue.fromIndex(0, value))
            )
        }
        
        /**
         * Create a record from a list of string values (backward compatible).
         */
        @Deprecated("Use create() with data points for ID-based values")
        fun fromValues(statId: Long, valueStrings: List<String>): StatRecord {
            return StatRecord(
                statId = statId,
                values = valueStrings.mapIndexed { index, value ->
                    DataPointValue.fromIndex(index, value)
                }
            )
        }
        
        /**
         * Parse a number value from string.
         * Returns null if parsing fails.
         */
        fun parseNumberValue(value: String): Double? {
            return value.toDoubleOrNull()
        }
        
        /**
         * Parse a duration value from milliseconds string.
         * Returns null if parsing fails.
         */
        fun parseDurationValue(value: String): Long? {
            return value.toLongOrNull()
        }
        
        /**
         * Parse a rating value from string (1-5).
         * Returns null if parsing fails or out of range.
         */
        fun parseRatingValue(value: String): Int? {
            val rating = value.toIntOrNull()
            return if (rating != null && rating in 1..5) rating else null
        }
        
        /**
         * Parse a checkbox value from string.
         */
        fun parseCheckboxValue(value: String): Boolean {
            return value.equals("true", ignoreCase = true)
        }
        
        /**
         * Format duration in milliseconds to readable string.
         * Format: "Xh Ym Zs" or "Ym Zs" or "Zs"
         */
        fun formatDuration(millis: Long): String {
            val hours = TimeUnit.MILLISECONDS.toHours(millis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
            
            return when {
                hours > 0 -> "${hours}h ${minutes}m ${seconds}s"
                minutes > 0 -> "${minutes}m ${seconds}s"
                else -> "${seconds}s"
            }
        }
        
        /**
         * Parse duration string (e.g., "1h 30m 45s") to milliseconds.
         */
        fun parseDurationString(duration: String): Long {
            var totalMillis = 0L
            
            val hourMatch = Regex("(\\d+)h").find(duration)
            val minMatch = Regex("(\\d+)m").find(duration)
            val secMatch = Regex("(\\d+)s").find(duration)
            
            hourMatch?.groupValues?.get(1)?.toLongOrNull()?.let {
                totalMillis += TimeUnit.HOURS.toMillis(it)
            }
            minMatch?.groupValues?.get(1)?.toLongOrNull()?.let {
                totalMillis += TimeUnit.MINUTES.toMillis(it)
            }
            secMatch?.groupValues?.get(1)?.toLongOrNull()?.let {
                totalMillis += TimeUnit.SECONDS.toMillis(it)
            }
            
            return totalMillis
        }
    }
}

/**
 * Location data class for geo-tagging records.
 */
@Serializable
data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val locationName: String? = null
)
