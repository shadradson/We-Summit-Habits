package com.humangamestats.data.repository

import com.humangamestats.data.database.dao.DataPointTemplateDao
import com.humangamestats.data.database.entity.DataPointTemplateEntity
import com.humangamestats.model.DataPoint
import com.humangamestats.model.DataPointTemplate
import com.humangamestats.model.StatType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for DataPointTemplate operations.
 * Provides a clean API for template CRUD operations and manages
 * the conversion between domain models and database entities.
 */
@Singleton
class DataPointTemplateRepository @Inject constructor(
    private val templateDao: DataPointTemplateDao
) {
    /**
     * Get all templates as a Flow.
     */
    fun getAllTemplates(): Flow<List<DataPointTemplate>> {
        return templateDao.getAllTemplates().map { entities ->
            entities.map { it.toTemplate() }
        }
    }
    
    /**
     * Get all templates as a list (non-Flow).
     */
    suspend fun getAllTemplatesList(): List<DataPointTemplate> {
        return templateDao.getAllTemplatesList().map { it.toTemplate() }
    }
    
    /**
     * Get only user-created templates.
     */
    fun getUserTemplates(): Flow<List<DataPointTemplate>> {
        return templateDao.getUserTemplates().map { entities ->
            entities.map { it.toTemplate() }
        }
    }
    
    /**
     * Get only system templates.
     */
    fun getSystemTemplates(): Flow<List<DataPointTemplate>> {
        return templateDao.getSystemTemplates().map { entities ->
            entities.map { it.toTemplate() }
        }
    }
    
    /**
     * Get a specific template by ID.
     */
    suspend fun getTemplateById(templateId: Long): DataPointTemplate? {
        return templateDao.getTemplateById(templateId)?.toTemplate()
    }
    
    /**
     * Get a specific template by ID as a Flow.
     */
    fun getTemplateByIdFlow(templateId: Long): Flow<DataPointTemplate?> {
        return templateDao.getTemplateByIdFlow(templateId).map { it?.toTemplate() }
    }
    
    /**
     * Search templates by name.
     */
    fun searchTemplates(query: String): Flow<List<DataPointTemplate>> {
        return templateDao.searchTemplates(query).map { entities ->
            entities.map { it.toTemplate() }
        }
    }
    
    /**
     * Create a new template.
     * @return The ID of the created template
     */
    suspend fun createTemplate(template: DataPointTemplate): Long {
        val maxSortOrder = templateDao.getMaxSortOrder() ?: 0
        val entity = DataPointTemplateEntity.fromTemplate(
            template.copy(sortOrder = maxSortOrder + 1)
        )
        return templateDao.insertTemplate(entity)
    }
    
    /**
     * Update an existing template.
     */
    suspend fun updateTemplate(template: DataPointTemplate) {
        val entity = DataPointTemplateEntity.fromTemplate(
            template.copy(updatedAt = System.currentTimeMillis())
        )
        templateDao.updateTemplate(entity)
    }
    
    /**
     * Delete a template.
     */
    suspend fun deleteTemplate(template: DataPointTemplate) {
        templateDao.deleteTemplateById(template.id)
    }
    
    /**
     * Delete a template by ID.
     */
    suspend fun deleteTemplateById(templateId: Long) {
        templateDao.deleteTemplateById(templateId)
    }
    
    /**
     * Check if a template name already exists.
     */
    suspend fun templateNameExists(name: String, excludeId: Long = 0): Boolean {
        return templateDao.templateNameExists(name, excludeId)
    }
    
    /**
     * Get the count of all templates.
     */
    suspend fun getTemplateCount(): Int {
        return templateDao.getTemplateCount()
    }
    
    /**
     * Initialize default system templates if they don't exist.
     */
    suspend fun initializeDefaultTemplates() {
        val count = templateDao.getTemplateCount()
        if (count == 0) {
            val defaultTemplates = createDefaultTemplates()
            templateDao.insertTemplates(defaultTemplates.map { 
                DataPointTemplateEntity.fromTemplate(it) 
            })
        }
    }
    
    /**
     * Create the default system templates.
     */
    private fun createDefaultTemplates(): List<DataPointTemplate> {
        return listOf(
            // Simple templates
            DataPointTemplate(
                name = "Simple Number",
                description = "Track a single numeric value",
                dataPoints = listOf(
                    DataPoint.number("Value")
                ),
                isSystem = true,
                sortOrder = 1
            ),
            DataPointTemplate(
                name = "Simple Checkbox",
                description = "Track yes/no completion",
                dataPoints = listOf(
                    DataPoint.checkbox("Completed")
                ),
                isSystem = true,
                sortOrder = 2
            ),
            DataPointTemplate(
                name = "Simple Rating",
                description = "Track a 1-5 star rating",
                dataPoints = listOf(
                    DataPoint.rating("Rating")
                ),
                isSystem = true,
                sortOrder = 3
            ),
            DataPointTemplate(
                name = "Simple Duration",
                description = "Track time duration",
                dataPoints = listOf(
                    DataPoint.duration("Duration")
                ),
                isSystem = true,
                sortOrder = 4
            ),
            
            // Workout templates
            DataPointTemplate(
                name = "Workout Set",
                description = "Track weight and reps for exercises",
                dataPoints = listOf(
                    DataPoint.number("Weight", "lbs"),
                    DataPoint.number("Reps")
                ),
                isSystem = true,
                sortOrder = 10
            ),
            DataPointTemplate(
                name = "Cardio Session",
                description = "Track distance, duration, and intensity",
                dataPoints = listOf(
                    DataPoint.number("Distance", "miles"),
                    DataPoint.duration("Duration"),
                    DataPoint.rating("Intensity")
                ),
                isSystem = true,
                sortOrder = 11
            ),
            
            // Health templates
            DataPointTemplate(
                name = "Sleep Log",
                description = "Track sleep duration and quality",
                dataPoints = listOf(
                    DataPoint.duration("Duration"),
                    DataPoint.rating("Quality")
                ),
                isSystem = true,
                sortOrder = 20
            ),
            DataPointTemplate(
                name = "Water Intake",
                description = "Track glasses of water",
                dataPoints = listOf(
                    DataPoint.number("Glasses")
                ),
                isSystem = true,
                sortOrder = 21
            ),
            DataPointTemplate(
                name = "Medication Taken",
                description = "Track medication compliance",
                dataPoints = listOf(
                    DataPoint.checkbox("Taken"),
                    DataPoint.number("Dose", "mg")
                ),
                isSystem = true,
                sortOrder = 22
            ),
            
            // Productivity templates
            DataPointTemplate(
                name = "Habit Tracker",
                description = "Track daily habit completion",
                dataPoints = listOf(
                    DataPoint.checkbox("Completed")
                ),
                isSystem = true,
                sortOrder = 30
            ),
            DataPointTemplate(
                name = "Focus Session",
                description = "Track focused work sessions",
                dataPoints = listOf(
                    DataPoint.duration("Duration"),
                    DataPoint.rating("Productivity")
                ),
                isSystem = true,
                sortOrder = 31
            ),
            
            // Mood & Wellness
            DataPointTemplate(
                name = "Mood Log",
                description = "Track your mood and energy",
                dataPoints = listOf(
                    DataPoint.rating("Mood"),
                    DataPoint.rating("Energy")
                ),
                isSystem = true,
                sortOrder = 40
            ),
            
            // Food & Nutrition
            DataPointTemplate(
                name = "Meal Log",
                description = "Track meals with calories and rating",
                dataPoints = listOf(
                    DataPoint.number("Calories", "cal"),
                    DataPoint.rating("Satisfaction")
                ),
                isSystem = true,
                sortOrder = 50
            )
        )
    }
}
