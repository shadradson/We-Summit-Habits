package com.humangamestats.data.repository

import com.humangamestats.data.database.dao.StatDao
import com.humangamestats.data.database.dao.StatRecordDao
import com.humangamestats.data.database.entity.StatEntity
import com.humangamestats.di.IoDispatcher
import com.humangamestats.model.Stat
import com.humangamestats.model.SortOption
import com.humangamestats.model.StatWithSummary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing stats.
 * Provides a clean API for the UI layer to interact with stat data.
 */
@Singleton
class StatRepository @Inject constructor(
    private val statDao: StatDao,
    private val statRecordDao: StatRecordDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    
    /**
     * Get all stats for a category as a Flow of domain models.
     */
    fun getStatsByCategory(categoryId: Long): Flow<List<Stat>> {
        return statDao.getStatsByCategory(categoryId).map { entities ->
            entities.map { it.toStat() }
        }
    }
    
    /**
     * Get all stats for a category with sorting applied.
     */
    fun getStatsByCategorySorted(categoryId: Long, sortOption: SortOption): Flow<List<Stat>> {
        return when (sortOption) {
            SortOption.RECENT -> statDao.getStatsByCategorySortedByRecentRecord(categoryId)
            SortOption.OLDEST -> statDao.getStatsByCategory(categoryId)
            SortOption.ALPHABETICAL -> statDao.getStatsByCategorySortedAlphabetically(categoryId)
            SortOption.ALPHABETICAL_DESC -> statDao.getStatsByCategorySortedAlphabetically(categoryId)
                .map { it.reversed() }
            else -> statDao.getStatsByCategory(categoryId)
        }.map { entities ->
            entities.map { it.toStat() }
        }
    }
    
    /**
     * Get stats with summary data (record count, latest values).
     */
    fun getStatsWithSummaryByCategory(categoryId: Long): Flow<List<StatWithSummary>> {
        return statDao.getStatsByCategory(categoryId).map { statEntities ->
            statEntities.map { statEntity ->
                val stat = statEntity.toStat()
                val recordCount = statRecordDao.getRecordCountByStat(stat.id)
                val latestRecord = statRecordDao.getLatestRecordByStat(stat.id)
                
                // Convert the record to values list
                val latestValues = latestRecord?.toStatRecord()?.values?.map { it.value } ?: emptyList()
                
                StatWithSummary(
                    stat = stat,
                    recordCount = recordCount,
                    latestValues = latestValues,
                    latestRecordedAt = latestRecord?.recordedAt
                )
            }
        }
    }
    
    /**
     * Get all stats (for export).
     */
    suspend fun getAllStats(): List<Stat> {
        return withContext(ioDispatcher) {
            statDao.getAllStats().map { it.toStat() }
        }
    }
    
    /**
     * Get all stats as Flow.
     */
    fun getAllStatsFlow(): Flow<List<Stat>> {
        return statDao.getAllStatsFlow().map { entities ->
            entities.map { it.toStat() }
        }
    }
    
    /**
     * Get a single stat by ID.
     */
    suspend fun getStatById(statId: Long): Stat? {
        return withContext(ioDispatcher) {
            statDao.getStatById(statId)?.toStat()
        }
    }
    
    /**
     * Get a single stat by ID as Flow for observation.
     */
    fun getStatByIdFlow(statId: Long): Flow<Stat?> {
        return statDao.getStatByIdFlow(statId).map { entity ->
            entity?.toStat()
        }
    }
    
    /**
     * Get the count of stats in a category.
     */
    suspend fun getStatCountByCategory(categoryId: Long): Int {
        return withContext(ioDispatcher) {
            statDao.getStatCountByCategory(categoryId)
        }
    }
    
    /**
     * Get total stat count.
     */
    suspend fun getTotalStatCount(): Int {
        return withContext(ioDispatcher) {
            statDao.getTotalStatCount()
        }
    }
    
    /**
     * Insert or update a stat.
     * @return The ID of the inserted/updated stat
     */
    suspend fun saveStat(stat: Stat): Long {
        return withContext(ioDispatcher) {
            val sortOrder = if (stat.sortOrder == 0 && stat.id == 0L) {
                statDao.getMaxSortOrderInCategory(stat.categoryId) + 1
            } else {
                stat.sortOrder
            }
            
            val entity = StatEntity.fromStat(
                stat.copy(
                    sortOrder = sortOrder,
                    updatedAt = System.currentTimeMillis()
                )
            )
            statDao.insertStat(entity)
        }
    }
    
    /**
     * Delete a stat.
     * This will cascade delete all records for this stat.
     */
    suspend fun deleteStat(stat: Stat) {
        withContext(ioDispatcher) {
            statDao.deleteStat(StatEntity.fromStat(stat))
        }
    }
    
    /**
     * Delete a stat by ID.
     */
    suspend fun deleteStatById(statId: Long) {
        withContext(ioDispatcher) {
            statDao.deleteStatById(statId)
        }
    }
    
    /**
     * Delete all stats for a category.
     */
    suspend fun deleteStatsByCategory(categoryId: Long) {
        withContext(ioDispatcher) {
            statDao.deleteStatsByCategory(categoryId)
        }
    }
    
    /**
     * Delete all stats.
     */
    suspend fun deleteAllStats() {
        withContext(ioDispatcher) {
            statDao.deleteAllStats()
        }
    }
    
    /**
     * Search stats by name within a category.
     */
    fun searchStatsInCategory(categoryId: Long, query: String): Flow<List<Stat>> {
        return statDao.searchStatsInCategory(categoryId, query).map { entities ->
            entities.map { it.toStat() }
        }
    }
    
    /**
     * Insert multiple stats (for import).
     */
    suspend fun insertStats(stats: List<Stat>): List<Long> {
        return withContext(ioDispatcher) {
            val entities = stats.map { StatEntity.fromStat(it) }
            statDao.insertStats(entities)
        }
    }
}
