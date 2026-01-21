package com.humangamestats.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.humangamestats.data.database.entity.DataPointTemplateEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for DataPointTemplate operations.
 * Provides methods to create, read, update, and delete templates.
 */
@Dao
interface DataPointTemplateDao {
    
    /**
     * Get all templates ordered by sort order, then by name.
     */
    @Query("SELECT * FROM data_point_templates ORDER BY sort_order ASC, name ASC")
    fun getAllTemplates(): Flow<List<DataPointTemplateEntity>>
    
    /**
     * Get all templates as a list (non-Flow).
     */
    @Query("SELECT * FROM data_point_templates ORDER BY sort_order ASC, name ASC")
    suspend fun getAllTemplatesList(): List<DataPointTemplateEntity>
    
    /**
     * Get only user-created templates (non-system).
     */
    @Query("SELECT * FROM data_point_templates WHERE is_system = 0 ORDER BY sort_order ASC, name ASC")
    fun getUserTemplates(): Flow<List<DataPointTemplateEntity>>
    
    /**
     * Get only system templates.
     */
    @Query("SELECT * FROM data_point_templates WHERE is_system = 1 ORDER BY sort_order ASC, name ASC")
    fun getSystemTemplates(): Flow<List<DataPointTemplateEntity>>
    
    /**
     * Get a specific template by ID.
     */
    @Query("SELECT * FROM data_point_templates WHERE id = :templateId")
    suspend fun getTemplateById(templateId: Long): DataPointTemplateEntity?
    
    /**
     * Get a specific template by ID as a Flow.
     */
    @Query("SELECT * FROM data_point_templates WHERE id = :templateId")
    fun getTemplateByIdFlow(templateId: Long): Flow<DataPointTemplateEntity?>
    
    /**
     * Search templates by name.
     */
    @Query("SELECT * FROM data_point_templates WHERE name LIKE '%' || :query || '%' ORDER BY sort_order ASC, name ASC")
    fun searchTemplates(query: String): Flow<List<DataPointTemplateEntity>>
    
    /**
     * Insert a new template.
     * @return The ID of the inserted template
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: DataPointTemplateEntity): Long
    
    /**
     * Insert multiple templates.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplates(templates: List<DataPointTemplateEntity>): List<Long>
    
    /**
     * Update an existing template.
     */
    @Update
    suspend fun updateTemplate(template: DataPointTemplateEntity)
    
    /**
     * Delete a template.
     */
    @Delete
    suspend fun deleteTemplate(template: DataPointTemplateEntity)
    
    /**
     * Delete a template by ID.
     */
    @Query("DELETE FROM data_point_templates WHERE id = :templateId")
    suspend fun deleteTemplateById(templateId: Long)
    
    /**
     * Delete all user-created templates (not system templates).
     */
    @Query("DELETE FROM data_point_templates WHERE is_system = 0")
    suspend fun deleteAllUserTemplates()
    
    /**
     * Get the count of all templates.
     */
    @Query("SELECT COUNT(*) FROM data_point_templates")
    suspend fun getTemplateCount(): Int
    
    /**
     * Get the maximum sort order value.
     */
    @Query("SELECT MAX(sort_order) FROM data_point_templates")
    suspend fun getMaxSortOrder(): Int?
    
    /**
     * Check if a template name already exists.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM data_point_templates WHERE name = :name AND id != :excludeId)")
    suspend fun templateNameExists(name: String, excludeId: Long = 0): Boolean
}
