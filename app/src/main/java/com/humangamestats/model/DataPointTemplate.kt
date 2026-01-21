package com.humangamestats.model

import kotlinx.serialization.Serializable

/**
 * Domain model for a data point template.
 * Templates define reusable sets of data points that can be applied to multiple stats.
 * 
 * @property id Unique identifier
 * @property name Display name for the template (e.g., "Weight Training")
 * @property description Optional description of what this template is for
 * @property dataPoints The list of data point definitions in this template
 * @property isSystem Whether this is a system-provided template that can't be deleted
 * @property sortOrder Order in which templates appear in lists
 * @property createdAt Timestamp when this template was created
 * @property updatedAt Timestamp when this template was last modified
 */
@Serializable
data class DataPointTemplate(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val dataPoints: List<DataPoint>,
    val isSystem: Boolean = false,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if this template has the same structure as another template.
     */
    fun hasSameStructure(other: DataPointTemplate): Boolean {
        if (dataPoints.size != other.dataPoints.size) return false
        return dataPoints.zip(other.dataPoints).all { (a, b) ->
            a.label == b.label && a.type == b.type
        }
    }
    
    /**
     * Create a copy of this template with a new name (for cloning).
     */
    fun cloneWithName(newName: String): DataPointTemplate {
        return copy(
            id = 0,
            name = newName,
            isSystem = false,
            sortOrder = 0,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
    
    companion object {
        /**
         * Create a simple template with a single data point.
         */
        fun simple(name: String, dataPoint: DataPoint): DataPointTemplate {
            return DataPointTemplate(
                name = name,
                dataPoints = listOf(dataPoint)
            )
        }
        
        /**
         * Create a weight training template (weight + reps).
         */
        fun weightTraining(): DataPointTemplate {
            return DataPointTemplate(
                name = "Weight Training",
                description = "Track weight lifted and repetitions",
                dataPoints = listOf(
                    DataPoint.number("Weight", "lbs"),
                    DataPoint.number("Reps")
                )
            )
        }
        
        /**
         * Create a cardio template (duration + distance).
         */
        fun cardio(): DataPointTemplate {
            return DataPointTemplate(
                name = "Cardio",
                description = "Track duration and distance for cardio exercises",
                dataPoints = listOf(
                    DataPoint.duration("Duration"),
                    DataPoint.number("Distance", "miles")
                )
            )
        }
    }
}
