package com.humangamestats.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.humangamestats.data.database.entity.StatRecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for stat records.
 */
@Dao
interface StatRecordDao {
    
    /**
     * Get all records for a stat ordered by recorded time (newest first).
     */
    @Query("SELECT * FROM stat_records WHERE stat_id = :statId ORDER BY recorded_at DESC")
    fun getRecordsByStat(statId: Long): Flow<List<StatRecordEntity>>
    
    /**
     * Get all records for a stat ordered by recorded time (oldest first).
     */
    @Query("SELECT * FROM stat_records WHERE stat_id = :statId ORDER BY recorded_at ASC")
    fun getRecordsByStatOldestFirst(statId: Long): Flow<List<StatRecordEntity>>
    
    /**
     * Get all records ordered by recorded time (for export).
     */
    @Query("SELECT * FROM stat_records ORDER BY stat_id ASC, recorded_at DESC")
    suspend fun getAllRecords(): List<StatRecordEntity>
    
    /**
     * Get all records as Flow.
     */
    @Query("SELECT * FROM stat_records ORDER BY stat_id ASC, recorded_at DESC")
    fun getAllRecordsFlow(): Flow<List<StatRecordEntity>>
    
    /**
     * Get a single record by ID.
     */
    @Query("SELECT * FROM stat_records WHERE id = :recordId")
    suspend fun getRecordById(recordId: Long): StatRecordEntity?
    
    /**
     * Get a single record by ID as Flow for observation.
     */
    @Query("SELECT * FROM stat_records WHERE id = :recordId")
    fun getRecordByIdFlow(recordId: Long): Flow<StatRecordEntity?>
    
    /**
     * Get the count of records for a stat.
     */
    @Query("SELECT COUNT(*) FROM stat_records WHERE stat_id = :statId")
    suspend fun getRecordCountByStat(statId: Long): Int
    
    /**
     * Get the count of records for a stat as Flow.
     */
    @Query("SELECT COUNT(*) FROM stat_records WHERE stat_id = :statId")
    fun getRecordCountByStatFlow(statId: Long): Flow<Int>
    
    /**
     * Get total record count.
     */
    @Query("SELECT COUNT(*) FROM stat_records")
    suspend fun getTotalRecordCount(): Int
    
    /**
     * Get the latest record for a stat.
     */
    @Query("SELECT * FROM stat_records WHERE stat_id = :statId ORDER BY recorded_at DESC LIMIT 1")
    suspend fun getLatestRecordByStat(statId: Long): StatRecordEntity?
    
    /**
     * Get the latest record for a stat as Flow.
     */
    @Query("SELECT * FROM stat_records WHERE stat_id = :statId ORDER BY recorded_at DESC LIMIT 1")
    fun getLatestRecordByStatFlow(statId: Long): Flow<StatRecordEntity?>
    
    /**
     * Insert a new record.
     * @return The row ID of the newly inserted record
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: StatRecordEntity): Long
    
    /**
     * Insert multiple records.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<StatRecordEntity>): List<Long>
    
    /**
     * Update an existing record.
     */
    @Update
    suspend fun updateRecord(record: StatRecordEntity)
    
    /**
     * Delete a record.
     */
    @Delete
    suspend fun deleteRecord(record: StatRecordEntity)
    
    /**
     * Delete a record by ID.
     */
    @Query("DELETE FROM stat_records WHERE id = :recordId")
    suspend fun deleteRecordById(recordId: Long)
    
    /**
     * Delete all records for a stat.
     */
    @Query("DELETE FROM stat_records WHERE stat_id = :statId")
    suspend fun deleteRecordsByStat(statId: Long)
    
    /**
     * Delete all records.
     */
    @Query("DELETE FROM stat_records")
    suspend fun deleteAllRecords()
    
    /**
     * Get records sorted by value (highest first) - for NUMBER type stats.
     */
    @Query("SELECT * FROM stat_records WHERE stat_id = :statId ORDER BY CAST(value AS REAL) DESC")
    fun getRecordsByStatSortedByHighestValue(statId: Long): Flow<List<StatRecordEntity>>
    
    /**
     * Get records sorted by value (lowest first) - for NUMBER type stats.
     */
    @Query("SELECT * FROM stat_records WHERE stat_id = :statId ORDER BY CAST(value AS REAL) ASC")
    fun getRecordsByStatSortedByLowestValue(statId: Long): Flow<List<StatRecordEntity>>
    
    /**
     * Get records within a date range.
     */
    @Query("SELECT * FROM stat_records WHERE stat_id = :statId AND recorded_at BETWEEN :startTime AND :endTime ORDER BY recorded_at DESC")
    fun getRecordsByStatInDateRange(statId: Long, startTime: Long, endTime: Long): Flow<List<StatRecordEntity>>
    
    /**
     * Get the maximum numeric value for a stat.
     */
    @Query("SELECT MAX(CAST(value AS REAL)) FROM stat_records WHERE stat_id = :statId")
    suspend fun getMaxValueByStat(statId: Long): Double?
    
    /**
     * Get the minimum numeric value for a stat.
     */
    @Query("SELECT MIN(CAST(value AS REAL)) FROM stat_records WHERE stat_id = :statId")
    suspend fun getMinValueByStat(statId: Long): Double?
    
    /**
     * Get the average numeric value for a stat.
     */
    @Query("SELECT AVG(CAST(value AS REAL)) FROM stat_records WHERE stat_id = :statId")
    suspend fun getAverageValueByStat(statId: Long): Double?

    /**
     * Get records for chart data (limited to recent entries).
     */
    @Query("SELECT * FROM stat_records WHERE stat_id = :statId ORDER BY recorded_at ASC LIMIT :limit")
    suspend fun getRecordsForChart(statId: Long, limit: Int = 30): List<StatRecordEntity>


}
