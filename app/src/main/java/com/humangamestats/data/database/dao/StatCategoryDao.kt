package com.humangamestats.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.humangamestats.data.database.entity.StatCategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for stat categories.
 */
@Dao
interface StatCategoryDao {
    
    /**
     * Get all categories ordered by sort order.
     */
    @Query("SELECT * FROM stat_categories ORDER BY sortOrder ASC, createdAt DESC")
    fun getAllCategories(): Flow<List<StatCategoryEntity>>
    
    /**
     * Get a single category by ID.
     */
    @Query("SELECT * FROM stat_categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Long): StatCategoryEntity?
    
    /**
     * Get a single category by ID as Flow for observation.
     */
    @Query("SELECT * FROM stat_categories WHERE id = :categoryId")
    fun getCategoryByIdFlow(categoryId: Long): Flow<StatCategoryEntity?>
    
    /**
     * Get the count of categories.
     */
    @Query("SELECT COUNT(*) FROM stat_categories")
    suspend fun getCategoryCount(): Int
    
    /**
     * Get the maximum sort order value.
     */
    @Query("SELECT COALESCE(MAX(sortOrder), 0) FROM stat_categories")
    suspend fun getMaxSortOrder(): Int
    
    /**
     * Insert a new category.
     * @return The row ID of the newly inserted category
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: StatCategoryEntity): Long
    
    /**
     * Insert multiple categories.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<StatCategoryEntity>): List<Long>
    
    /**
     * Update an existing category.
     */
    @Update
    suspend fun updateCategory(category: StatCategoryEntity)
    
    /**
     * Delete a category.
     * This will also cascade delete all stats and records within this category.
     */
    @Delete
    suspend fun deleteCategory(category: StatCategoryEntity)
    
    /**
     * Delete a category by ID.
     */
    @Query("DELETE FROM stat_categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Long)
    
    /**
     * Delete all categories.
     */
    @Query("DELETE FROM stat_categories")
    suspend fun deleteAllCategories()
    
    /**
     * Search categories by title.
     */
    @Query("SELECT * FROM stat_categories WHERE title LIKE '%' || :query || '%' ORDER BY sortOrder ASC")
    fun searchCategories(query: String): Flow<List<StatCategoryEntity>>
    
    /**
     * Update the default sort option for a category.
     */
    @Query("UPDATE stat_categories SET default_sort_option = :sortOption, updatedAt = :updatedAt WHERE id = :categoryId")
    suspend fun updateDefaultSortOption(categoryId: Long, sortOption: String?, updatedAt: Long = System.currentTimeMillis())
}
