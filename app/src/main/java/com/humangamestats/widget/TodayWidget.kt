package com.humangamestats.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.room.Room
import androidx.room.RoomDatabase
import com.humangamestats.MainActivity
import com.humangamestats.R
import com.humangamestats.data.database.AppDatabase
import com.humangamestats.model.DataPoint
import com.humangamestats.model.StatRecord
import com.humangamestats.model.StatType
import com.humangamestats.model.StatWithSummary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Glance App Widget that displays today's stats on the home screen.
 * Shows all stats that have record entries from today.
 */
class TodayWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Get today's stats from the database
        val stats = try {
            getTodayStats(context)
        } catch (e: Exception) {
            android.util.Log.e("TodayWidget", "Error loading stats", e)
            emptyList()
        }
        
        provideContent {
            GlanceTheme {
                TodayWidgetContent(
                    stats = stats,
                    context = context
                )
            }
        }
    }
    
    /**
     * Query the database for stats with records from today.
     */
    private suspend fun getTodayStats(context: Context): List<StatWithSummary> {
        // Clear the cached database to ensure fresh connection
        synchronized(this) {
            database?.close()
            database = null
        }
        
        val db = getDatabase(context)
        val statDao = db.statDao()
        val recordDao = db.statRecordDao()
        
        // Get today's timestamp range
        val (startOfDay, endOfDay) = getTodayTimestamps()
        
        android.util.Log.d("TodayWidget", "Today range: $startOfDay to $endOfDay (current: ${System.currentTimeMillis()})")
        
        // Get all records and filter to today
        val allRecords = recordDao.getAllRecords()
        android.util.Log.d("TodayWidget", "Total records in DB: ${allRecords.size}")
        
        // Log each record's timestamp for debugging
        allRecords.take(5).forEach { record ->
            android.util.Log.d("TodayWidget", "Record ${record.id}: statId=${record.statId}, recordedAt=${record.recordedAt}")
        }
        
        val todayRecords = allRecords.filter { it.recordedAt in startOfDay until endOfDay }
        android.util.Log.d("TodayWidget", "Today's records: ${todayRecords.size}")
        
        val todayStatIds = todayRecords.map { it.statId }.distinct()
        android.util.Log.d("TodayWidget", "Today's stat IDs: $todayStatIds")
        
        if (todayStatIds.isEmpty()) {
            android.util.Log.d("TodayWidget", "No records found for today")
            return emptyList()
        }
        
        // Get all stats and filter to those with today's records
        val allStats = statDao.getAllStats()
        android.util.Log.d("TodayWidget", "Total stats in DB: ${allStats.size}")
        
        return allStats
            .filter { it.id in todayStatIds }
            .map { statEntity ->
                val stat = statEntity.toStat()
                val recordCount = recordDao.getRecordCountByStat(stat.id)
                val todayRecordCount = todayRecords.count { it.statId == stat.id }
                val latestRecord = recordDao.getLatestRecordByStat(stat.id)
                
                val latestValues = latestRecord?.toStatRecord()?.values?.map { it.value } ?: emptyList()
                
                StatWithSummary(
                    stat = stat,
                    recordCount = recordCount,
                    todayRecordCount = todayRecordCount,
                    latestValues = latestValues,
                    latestRecordedAt = latestRecord?.recordedAt
                )
            }
            .sortedByDescending { it.latestRecordedAt ?: 0L }
    }
    
    /**
     * Get the start and end timestamps for today.
     */
    private fun getTodayTimestamps(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfDay = calendar.timeInMillis
        
        return Pair(startOfDay, endOfDay)
    }
    
    companion object {
        private var database: AppDatabase? = null
        
        /**
         * Get or create the database instance.
         * Uses TRUNCATE journal mode instead of WAL to ensure the widget
         * can immediately see changes made by the main app.
         * WAL mode can cause stale reads when multiple processes access the db.
         */
        fun getDatabase(context: Context): AppDatabase {
            return database ?: synchronized(this) {
                database ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    AppDatabase.DATABASE_NAME
                )
                    .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                    .build()
                    .also { database = it }
            }
        }
        
        /**
         * Parameter key for passing stat ID to the main activity.
         */
        val StatIdKey = ActionParameters.Key<Long>("stat_id")
    }
}

/**
 * Main widget content composable.
 */
@Composable
private fun TodayWidgetContent(
    stats: List<StatWithSummary>,
    context: Context
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(8.dp)
            .cornerRadius(4.dp)
    ) {
        // Header with title and refresh button
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                //text = context.getString(R.string.widget_title),
                text = "We Summit Habits - Today",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = GlanceTheme.colors.onSurface
                ),
                modifier = GlanceModifier.defaultWeight()
            )

            Image(
                provider = ImageProvider(R.drawable.ic_refresh),
                contentDescription = context.getString(R.string.widget_refresh),
                modifier = GlanceModifier
                    .size(32.dp)
                    .padding(4.dp)
                    .clickable(actionRunCallback<RefreshAction>())
            )
        }
        
        Spacer(modifier = GlanceModifier.height(8.dp))
        
        // Content area
        if (stats.isEmpty()) {
            // Empty state
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = context.getString(R.string.widget_empty),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = GlanceTheme.colors.onSurfaceVariant
                    )
                )
            }
        } else {
            // Stats list
            LazyColumn {
                items(stats, itemId = { it.stat.id }) { statWithSummary ->
                    StatItem(
                        statWithSummary = statWithSummary,
                        context = context
                    )
                }
            }
        }
    }
}

/**
 * Individual stat item in the widget list.
 */
@Composable
private fun StatItem(
    statWithSummary: StatWithSummary,
    context: Context
) {
    val stat = statWithSummary.stat
    Column() {
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(GlanceTheme.colors.surfaceVariant)
                .cornerRadius(0.5.dp)
                .clickable(
                    actionStartActivity<MainActivity>(
                        actionParametersOf(TodayWidget.StatIdKey to stat.id)
                    )
                )
        ) {
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stat info
                Column(modifier = GlanceModifier.defaultWeight()) {
                    Text(
                        text = stat.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = GlanceTheme.colors.onSurface
                        ),
                        maxLines = 1
                    )

                    // Show entry count and time of last entry
                    val entryText =
                        if (statWithSummary.todayRecordCount == 1) "1 entry" else "${statWithSummary.todayRecordCount} entries"
                    val timeText =
                        statWithSummary.latestRecordedAt?.let { " • ${formatTime(it)}" } ?: ""
                    Text(
                        text = "$entryText$timeText",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = GlanceTheme.colors.onSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.width(8.dp))

                // Latest values - show all data points separated by pipes
                val displayValue = formatAllValues(statWithSummary.latestValues, stat.dataPoints)
                if (displayValue.isNotEmpty()) {
                    Text(
                        text = displayValue,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = GlanceTheme.colors.primary
                        )
                    )
                }
            }
        }
        Spacer(modifier = GlanceModifier.height(4.dp))
    }
}

/**
 * Format timestamp as time (e.g., "2:30 PM").
 */
private fun formatTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

/**
 * Format stat value based on its type.
 */
private fun formatStatValue(value: String, statType: StatType): String {
    return when (statType) {
        StatType.NUMBER -> value.toDoubleOrNull()?.let {
            if (it == it.toLong().toDouble()) it.toLong().toString() else String.format("%.1f", it)
        } ?: value
        StatType.DURATION -> {
            val millis = value.toLongOrNull() ?: 0L
            StatRecord.formatDuration(millis)
        }
        StatType.RATING -> "★".repeat(value.toIntOrNull() ?: 0)
        StatType.CHECKBOX -> if (value == "true") "✓" else "✗"
    }
}

/**
 * Format all values from a record, separated by pipes.
 * Each value is formatted according to its data point type.
 */
private fun formatAllValues(values: List<String>, dataPoints: List<DataPoint>): String {
    if (values.isEmpty()) return ""
    
    return values.mapIndexed { index, value ->
        val dataPoint = dataPoints.getOrNull(index)
        if (dataPoint != null) {
            val formatted = formatStatValue(value, dataPoint.type)
            if (dataPoint.unit.isNotEmpty() && formatted.isNotEmpty()) {
                "$formatted ${dataPoint.unit}"
            } else {
                formatted
            }
        } else {
            value
        }
    }.filter { it.isNotEmpty() }.joinToString(" | ")
}

/**
 * Action callback for refreshing the widget.
 */
class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Update the widget
        TodayWidget().update(context, glanceId)
    }
}
