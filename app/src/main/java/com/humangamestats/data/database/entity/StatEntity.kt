package com.humangamestats.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.humangamestats.model.DataPoint
import com.humangamestats.model.Stat
import com.humangamestats.model.StatType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room entity representing a stat in the database.
 * Stats belong to a category and define what is being tracked.
 * Can have multiple data points stored as JSON.
 */
@Entity(
    tableName = "stats",
    foreignKeys = [
        ForeignKey(
            entity = StatCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["category_id"]),
        Index(value = ["template_id"])
    ]
)
data class StatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    
    val name: String,
    
    /**
     * JSON-serialized list of DataPoint objects.
     * Stores all data points for this stat.
     */
    @ColumnInfo(name = "data_points_json")
    val dataPointsJson: String = "[]",
    
    /**
     * Optional reference to the template this stat was created from.
     */
    @ColumnInfo(name = "template_id")
    val templateId: Long? = null,
    
    /**
     * Legacy field for backward compatibility.
     * New stats should use dataPointsJson instead.
     */
    @ColumnInfo(name = "stat_type", defaultValue = "NUMBER")
    val statType: StatType = StatType.NUMBER,
    
    /**
     * Legacy field for backward compatibility.
     * New stats should use dataPointsJson instead.
     */
    @ColumnInfo(name = "type_label", defaultValue = "Value")
    val typeLabel: String = "Value",
    
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
         * Create a StatEntity from a domain model Stat.
         */
        fun fromStat(stat: Stat): StatEntity {
            return StatEntity(
                id = stat.id,
                categoryId = stat.categoryId,
                name = stat.name,
                dataPointsJson = json.encodeToString(stat.dataPoints),
                templateId = stat.templateId,
                // Legacy fields for backward compatibility
                statType = stat.dataPoints.firstOrNull()?.type ?: StatType.NUMBER,
                typeLabel = stat.dataPoints.firstOrNull()?.label ?: "Value",
                sortOrder = stat.sortOrder,
                createdAt = stat.createdAt,
                updatedAt = stat.updatedAt
            )
        }
    }
    
    /**
     * Convert this entity to a domain model Stat.
     */
    fun toStat(): Stat {
        val dataPoints = try {
            if (dataPointsJson.isNotEmpty() && dataPointsJson != "[]") {
                json.decodeFromString<List<DataPoint>>(dataPointsJson)
            } else {
                // Fall back to legacy fields
                listOf(DataPoint(label = typeLabel, type = statType))
            }
        } catch (e: Exception) {
            // Fall back to legacy fields if JSON parsing fails
            listOf(DataPoint(label = typeLabel, type = statType))
        }
        
        return Stat(
            id = id,
            categoryId = categoryId,
            name = name,
            dataPoints = dataPoints.ifEmpty { 
                listOf(DataPoint(label = typeLabel, type = statType)) 
            },
            templateId = templateId,
            sortOrder = sortOrder,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
