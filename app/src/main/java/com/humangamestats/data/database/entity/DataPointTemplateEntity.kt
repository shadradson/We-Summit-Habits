package com.humangamestats.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.humangamestats.model.DataPoint
import com.humangamestats.model.DataPointTemplate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room entity representing a reusable data point template.
 * Templates allow users to define common data point configurations
 * that can be applied to multiple stats.
 * 
 * Example templates:
 * - "Workout Set" with data points: Weight (NUMBER), Reps (NUMBER)
 * - "Sleep" with data points: Hours (DURATION), Quality (RATING)
 * - "Habit" with data points: Completed (CHECKBOX)
 */
@Entity(tableName = "data_point_templates")
data class DataPointTemplateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    
    val description: String = "",
    
    /**
     * JSON-serialized list of DataPoint objects.
     * Stores the template's data point configuration.
     */
    @ColumnInfo(name = "data_points_json")
    val dataPointsJson: String = "[]",
    
    /**
     * Whether this is a system-provided template (can't be deleted).
     */
    @ColumnInfo(name = "is_system")
    val isSystem: Boolean = false,
    
    @ColumnInfo(name = "sort_order")
    val sortOrder: Int = 0,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        private val json = Json { 
            ignoreUnknownKeys = true 
            encodeDefaults = true
        }
        
        /**
         * Create a DataPointTemplateEntity from a domain model DataPointTemplate.
         */
        fun fromTemplate(template: DataPointTemplate): DataPointTemplateEntity {
            return DataPointTemplateEntity(
                id = template.id,
                name = template.name,
                description = template.description,
                dataPointsJson = json.encodeToString(template.dataPoints),
                isSystem = template.isSystem,
                sortOrder = template.sortOrder,
                createdAt = template.createdAt,
                updatedAt = template.updatedAt
            )
        }
    }
    
    /**
     * Convert this entity to a domain model DataPointTemplate.
     */
    fun toTemplate(): DataPointTemplate {
        val dataPoints = try {
            if (dataPointsJson.isNotEmpty() && dataPointsJson != "[]") {
                json.decodeFromString<List<DataPoint>>(dataPointsJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
        
        return DataPointTemplate(
            id = id,
            name = name,
            description = description,
            dataPoints = dataPoints,
            isSystem = isSystem,
            sortOrder = sortOrder,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
