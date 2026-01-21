package com.humangamestats.data.repository

import com.humangamestats.data.database.dao.StatRecordDao
import com.humangamestats.data.database.entity.StatRecordEntity
import com.humangamestats.di.IoDispatcher
import com.humangamestats.model.SortOption
import com.humangamestats.model.StatRecord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing stat records.
 * Provides a clean API for the UI layer to interact with record data.
 */
@Singleton
class StatRecordRepository @Inject constructor(
    private val statRecordDao: StatRecordDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    
    /**
     * Get all records for a stat as a Flow of domain models.
     */
    fun getRecordsByStat(statId: Long): Flow<List<StatRecord>> {
        return statRecordDao.getRecordsByStat(statId).map { entities ->
            entities.map { it.toStatRecord() }
        }
    }
    
    /**
     * Get records for a stat with sorting applied.
     */
    fun getRecordsByStatSorted(statId: Long, sortOption: SortOption): Flow<List<StatRecord>> {
        return when (sortOption) {
            SortOption.RECENT -> statRecordDao.getRecordsByStat(statId)
            SortOption.OLDEST -> statRecordDao.getRecordsByStatOldestFirst(statId)
            SortOption.HIGHEST -> statRecordDao.getRecordsByStatSortedByHighestValue(statId)
            SortOption.LOWEST -> statRecordDao.getRecordsByStatSortedByLowestValue(statId)
            else -> statRecordDao.getRecordsByStat(statId)
        }.map { entities ->
            entities.map { it.toStatRecord() }
        }
    }
    
    /**
     * Get all records (for export).
     */
    suspend fun getAllRecords(): List<StatRecord> {
        return withContext(ioDispatcher) {
            statRecordDao.getAllRecords().map { it.toStatRecord() }
        }
    }
    
    /**
     * Get all records as Flow.
     */
    fun getAllRecordsFlow(): Flow<List<StatRecord>> {
        return statRecordDao.getAllRecordsFlow().map { entities ->
            entities.map { it.toStatRecord() }
        }
    }
    
    /**
     * Get a single record by ID.
     */
    suspend fun getRecordById(recordId: Long): StatRecord? {
        return withContext(ioDispatcher) {
            statRecordDao.getRecordById(recordId)?.toStatRecord()
        }
    }
    
    /**
     * Get a single record by ID as Flow for observation.
     */
    fun getRecordByIdFlow(recordId: Long): Flow<StatRecord?> {
        return statRecordDao.getRecordByIdFlow(recordId).map { entity ->
            entity?.toStatRecord()
        }
    }
    
    /**
     * Get the count of records for a stat.
     */
    suspend fun getRecordCountByStat(statId: Long): Int {
        return withContext(ioDispatcher) {
            statRecordDao.getRecordCountByStat(statId)
        }
    }
    
    /**
     * Get the count of records for a stat as Flow.
     */
    fun getRecordCountByStatFlow(statId: Long): Flow<Int> {
        return statRecordDao.getRecordCountByStatFlow(statId)
    }
    
    /**
     * Get total record count.
     */
    suspend fun getTotalRecordCount(): Int {
        return withContext(ioDispatcher) {
            statRecordDao.getTotalRecordCount()
        }
    }
    
    /**
     * Get the latest record for a stat.
     */
    suspend fun getLatestRecordByStat(statId: Long): StatRecord? {
        return withContext(ioDispatcher) {
            statRecordDao.getLatestRecordByStat(statId)?.toStatRecord()
        }
    }


    /**
     * Get the latest record for a stat as Flow.
     */
    fun getLatestRecordByStatFlow(statId: Long): Flow<StatRecord?> {
        return statRecordDao.getLatestRecordByStatFlow(statId).map { entity ->
            entity?.toStatRecord()
        }
    }
    
    /**
     * Insert or update a record.
     * @return The ID of the inserted/updated record
     */
    suspend fun saveRecord(record: StatRecord): Long {
        return withContext(ioDispatcher) {
            val entity = StatRecordEntity.fromStatRecord(
                record.copy(updatedAt = System.currentTimeMillis())
            )
            statRecordDao.insertRecord(entity)
        }
    }
    
    /**
     * Delete a record.
     */
    suspend fun deleteRecord(record: StatRecord) {
        withContext(ioDispatcher) {
            statRecordDao.deleteRecord(StatRecordEntity.fromStatRecord(record))
        }
    }
    
    /**
     * Delete a record by ID.
     */
    suspend fun deleteRecordById(recordId: Long) {
        withContext(ioDispatcher) {
            statRecordDao.deleteRecordById(recordId)
        }
    }
    
    /**
     * Delete all records for a stat.
     */
    suspend fun deleteRecordsByStat(statId: Long) {
        withContext(ioDispatcher) {
            statRecordDao.deleteRecordsByStat(statId)
        }
    }
    
    /**
     * Delete all records.
     */
    suspend fun deleteAllRecords() {
        withContext(ioDispatcher) {
            statRecordDao.deleteAllRecords()
        }
    }
    
    /**
     * Get records within a date range.
     */
    fun getRecordsByStatInDateRange(
        statId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<StatRecord>> {
        return statRecordDao.getRecordsByStatInDateRange(statId, startTime, endTime)
            .map { entities -> entities.map { it.toStatRecord() } }
    }
    
    /**
     * Get statistics for a stat (max, min, average).
     */
    suspend fun getStatStatistics(statId: Long): StatStatistics {
        return withContext(ioDispatcher) {
            StatStatistics(
                maxValue = statRecordDao.getMaxValueByStat(statId),
                minValue = statRecordDao.getMinValueByStat(statId),
                averageValue = statRecordDao.getAverageValueByStat(statId),
                totalCount = statRecordDao.getRecordCountByStat(statId)
            )
        }
    }
    
    /**
     * Get records for chart data.
     */
    suspend fun getRecordsForChart(statId: Long, limit: Int = 30): List<StatRecord> {
        return withContext(ioDispatcher) {
            statRecordDao.getRecordsForChart(statId, limit).map { it.toStatRecord() }
        }
    }
    
    /**
     * Insert multiple records (for import).
     */
    suspend fun insertRecords(records: List<StatRecord>): List<Long> {
        return withContext(ioDispatcher) {
            val entities = records.map { record -> StatRecordEntity.fromStatRecord(record) }
            statRecordDao.insertRecords(entities)
        }
    }
}

/**
 * Data class for stat statistics.
 */
data class StatStatistics(
    val maxValue: Double?,
    val minValue: Double?,
    val averageValue: Double?,
    val totalCount: Int
)
