package com.humangamestats.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.humangamestats.model.StatCategory

/**
 * Room entity representing a stat category in the database.
 */
@Entity(tableName = "stat_categories")
data class StatCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val icon: String = "category",
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Convert entity to domain model.
     */
    fun toDomainModel(): StatCategory = StatCategory(
        id = id,
        title = title,
        icon = icon,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    
    companion object {
        /**
         * Convert domain model to entity.
         */
        fun fromDomainModel(category: StatCategory): StatCategoryEntity = StatCategoryEntity(
            id = category.id,
            title = category.title,
            icon = category.icon,
            sortOrder = category.sortOrder,
            createdAt = category.createdAt,
            updatedAt = category.updatedAt
        )
    }
}
