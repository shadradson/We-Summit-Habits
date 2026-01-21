package com.humangamestats.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.humangamestats.model.DataPointValue
import com.humangamestats.model.StatRecord
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room entity representing a stat record in the database.
 * Records are individual entries for a stat containing the tracked data.
 * Supports multiple values stored as JSON.
 */
@Entity(
    tableName = "stat_records",
    foreignKeys = [
        ForeignKey(
            entity = StatEntity::class,
            parentColumns = ["id"],
            childColumns = ["stat_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["stat_id"]),
        Index(value = ["recorded_at"])
    ]
)
data class StatRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "stat_id")
    val statId: Long,
    
    /**
     * JSON-serialized list of DataPointValue objects.
     * Stores all values for this record's data points.
     */
    @ColumnInfo(name = "values_json")
    val valuesJson: String = "[]",
    
    /**
     * Legacy field for backward compatibility.
     * New records should use valuesJson instead.
     */
    @ColumnInfo(name = "value", defaultValue = "")
    val value: String = "",
    
    val notes: String? = null,
    
    val latitude: Double? = null,
    
    val longitude: Double? = null,
    
    @ColumnInfo(name = "location_name")
    val locationName: String? = null,
    
    @ColumnInfo(name = "recorded_at")
    val recordedAt: Long = System.currentTimeMillis(),
    
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
         * Create a StatRecordEntity from a domain model StatRecord.
         */
        fun fromStatRecord(record: StatRecord): StatRecordEntity {
            return StatRecordEntity(
                id = record.id,
                statId = record.statId,
                valuesJson = json.encodeToString(record.values),
                // Legacy field - store first value for backward compatibility
                value = record.values.firstOrNull()?.value ?: "",
                notes = record.notes,
                latitude = record.latitude,
                longitude = record.longitude,
                locationName = record.locationName,
                recordedAt = record.recordedAt,
                createdAt = record.createdAt,
                updatedAt = record.updatedAt
            )
        }
    }
    
    /**
     * Convert this entity to a domain model StatRecord.
     */
    fun toStatRecord(): StatRecord {
        val values = try {
            if (valuesJson.isNotEmpty() && valuesJson != "[]") {
                json.decodeFromString<List<DataPointValue>>(valuesJson)
            } else if (value.isNotEmpty()) {
                // Fall back to legacy single value
                listOf(DataPointValue(0, value))
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            // Fall back to legacy single value if JSON parsing fails
            if (value.isNotEmpty()) {
                listOf(DataPointValue(0, value))
            } else {
                emptyList()
            }
        }
        
        return StatRecord(
            id = id,
            statId = statId,
            values = values,
            notes = notes,
            latitude = latitude,
            longitude = longitude,
            locationName = locationName,
            recordedAt = recordedAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
