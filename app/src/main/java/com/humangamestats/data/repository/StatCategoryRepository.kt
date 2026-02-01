package com.humangamestats.data.repository

import com.humangamestats.data.database.dao.StatCategoryDao
import com.humangamestats.data.database.entity.StatCategoryEntity
import com.humangamestats.di.IoDispatcher
import com.humangamestats.model.StatCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing stat categories.
 * Provides a clean API for the UI layer to interact with category data.
 */
@Singleton
class StatCategoryRepository @Inject constructor(
    private val statCategoryDao: StatCategoryDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    
    /**
     * Get all categories as a Flow of domain models.
     */
    fun getAllCategories(): Flow<List<StatCategory>> {
        return statCategoryDao.getAllCategories().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    /**
     * Get a single category by ID.
     */
    suspend fun getCategoryById(categoryId: Long): StatCategory? {
        return withContext(ioDispatcher) {
            statCategoryDao.getCategoryById(categoryId)?.toDomainModel()
        }
    }
    
    /**
     * Get a single category by ID as Flow for observation.
     */
    fun getCategoryByIdFlow(categoryId: Long): Flow<StatCategory?> {
        return statCategoryDao.getCategoryByIdFlow(categoryId).map { entity ->
            entity?.toDomainModel()
        }
    }
    
    /**
     * Get the count of categories.
     */
    suspend fun getCategoryCount(): Int {
        return withContext(ioDispatcher) {
            statCategoryDao.getCategoryCount()
        }
    }
    
    /**
     * Insert or update a category.
     * @return The ID of the inserted/updated category
     */
    suspend fun saveCategory(category: StatCategory): Long {
        return withContext(ioDispatcher) {
            val sortOrder = if (category.sortOrder == 0 && category.id == 0L) {
                statCategoryDao.getMaxSortOrder() + 1
            } else {
                category.sortOrder
            }
            
            val entity = StatCategoryEntity.fromDomainModel(
                category.copy(
                    sortOrder = sortOrder,
                    updatedAt = System.currentTimeMillis()
                )
            )
            
            // Use update for existing categories to avoid cascade delete from REPLACE strategy
            if (category.id > 0) {
                statCategoryDao.updateCategory(entity)
                category.id
            } else {
                statCategoryDao.insertCategory(entity)
            }
        }
    }
    
    /**
     * Delete a category.
     * This will cascade delete all stats and records within this category.
     */
    suspend fun deleteCategory(category: StatCategory) {
        withContext(ioDispatcher) {
            statCategoryDao.deleteCategory(StatCategoryEntity.fromDomainModel(category))
        }
    }
    
    /**
     * Delete a category by ID.
     */
    suspend fun deleteCategoryById(categoryId: Long) {
        withContext(ioDispatcher) {
            statCategoryDao.deleteCategoryById(categoryId)
        }
    }
    
    /**
     * Delete all categories.
     */
    suspend fun deleteAllCategories() {
        withContext(ioDispatcher) {
            statCategoryDao.deleteAllCategories()
        }
    }
    
    /**
     * Search categories by title.
     */
    fun searchCategories(query: String): Flow<List<StatCategory>> {
        return statCategoryDao.searchCategories(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    /**
     * Insert multiple categories (for import).
     */
    suspend fun insertCategories(categories: List<StatCategory>): List<Long> {
        return withContext(ioDispatcher) {
            val entities = categories.map { StatCategoryEntity.fromDomainModel(it) }
            statCategoryDao.insertCategories(entities)
        }
    }
    
    /**
     * Update the default sort option for a category.
     * @param categoryId The ID of the category to update
     * @param sortOption The sort option name (e.g., "RECENT", "ALPHABETICAL"), or null to clear
     */
    suspend fun updateDefaultSortOption(categoryId: Long, sortOption: String?) {
        withContext(ioDispatcher) {
            statCategoryDao.updateDefaultSortOption(categoryId, sortOption)
        }
    }
}
