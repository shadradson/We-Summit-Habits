package com.humangamestats.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.humangamestats.data.database.entity.StatEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for stats.
 */
@Dao
interface StatDao {
    
    /**
     * Get all stats for a category ordered by sort order.
     */
    @Query("SELECT * FROM stats WHERE category_id = :categoryId ORDER BY sort_order ASC, created_at DESC")
    fun getStatsByCategory(categoryId: Long): Flow<List<StatEntity>>
    
    /**
     * Get all stats ordered by sort order (for export).
     */
    @Query("SELECT * FROM stats ORDER BY category_id ASC, sort_order ASC")
    suspend fun getAllStats(): List<StatEntity>
    
    /**
     * Get all stats as Flow.
     */
    @Query("SELECT * FROM stats ORDER BY category_id ASC, sort_order ASC")
    fun getAllStatsFlow(): Flow<List<StatEntity>>
    
    /**
     * Get a single stat by ID.
     */
    @Query("SELECT * FROM stats WHERE id = :statId")
    suspend fun getStatById(statId: Long): StatEntity?
    
    /**
     * Get a single stat by ID as Flow for observation.
     */
    @Query("SELECT * FROM stats WHERE id = :statId")
    fun getStatByIdFlow(statId: Long): Flow<StatEntity?>
    
    /**
     * Get the count of stats in a category.
     */
    @Query("SELECT COUNT(*) FROM stats WHERE category_id = :categoryId")
    suspend fun getStatCountByCategory(categoryId: Long): Int
    
    /**
     * Get total stat count.
     */
    @Query("SELECT COUNT(*) FROM stats")
    suspend fun getTotalStatCount(): Int
    
    /**
     * Get the maximum sort order value within a category.
     */
    @Query("SELECT COALESCE(MAX(sort_order), 0) FROM stats WHERE category_id = :categoryId")
    suspend fun getMaxSortOrderInCategory(categoryId: Long): Int
    
    /**
     * Insert a new stat (only if it doesn't exist).
     * @return The row ID of the newly inserted stat
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStat(stat: StatEntity): Long
    
    /**
     * Insert or update a stat without triggering cascade deletes.
     * Uses UPDATE for existing stats, INSERT for new ones.
     * @return The row ID of the inserted/updated stat
     */
    @Upsert
    suspend fun upsertStat(stat: StatEntity): Long
    
    /**
     * Insert multiple stats (for import, replaces existing).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: List<StatEntity>): List<Long>
    
    /**
     * Insert or update multiple stats without triggering cascade deletes.
     */
    @Upsert
    suspend fun upsertStats(stats: List<StatEntity>): List<Long>
    
    /**
     * Update an existing stat.
     */
    @Update
    suspend fun updateStat(stat: StatEntity)
    
    /**
     * Delete a stat.
     * This will also cascade delete all records for this stat.
     */
    @Delete
    suspend fun deleteStat(stat: StatEntity)
    
    /**
     * Delete a stat by ID.
     */
    @Query("DELETE FROM stats WHERE id = :statId")
    suspend fun deleteStatById(statId: Long)
    
    /**
     * Delete all stats for a category.
     */
    @Query("DELETE FROM stats WHERE category_id = :categoryId")
    suspend fun deleteStatsByCategory(categoryId: Long)
    
    /**
     * Delete all stats.
     */
    @Query("DELETE FROM stats")
    suspend fun deleteAllStats()
    
    /**
     * Search stats by name within a category.
     */
    @Query("SELECT * FROM stats WHERE category_id = :categoryId AND name LIKE '%' || :query || '%' ORDER BY sort_order ASC")
    fun searchStatsInCategory(categoryId: Long, query: String): Flow<List<StatEntity>>
    
    /**
     * Get stats sorted by most recent record.
     */
    @Query("""
        SELECT s.* FROM stats s
        LEFT JOIN (
            SELECT stat_id, MAX(recorded_at) as latestRecord
            FROM stat_records
            GROUP BY stat_id
        ) r ON s.id = r.stat_id
        WHERE s.category_id = :categoryId
        ORDER BY CASE WHEN r.latestRecord IS NULL THEN 1 ELSE 0 END, r.latestRecord DESC, s.created_at DESC
    """)
    fun getStatsByCategorySortedByRecentRecord(categoryId: Long): Flow<List<StatEntity>>
    
    /**
     * Get stats sorted alphabetically.
     */
    @Query("SELECT * FROM stats WHERE category_id = :categoryId ORDER BY name ASC")
    fun getStatsByCategorySortedAlphabetically(categoryId: Long): Flow<List<StatEntity>>
}
