package com.humangamestats.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.humangamestats.data.database.dao.DataPointTemplateDao
import com.humangamestats.data.database.dao.StatCategoryDao
import com.humangamestats.data.database.dao.StatDao
import com.humangamestats.data.database.dao.StatRecordDao
import com.humangamestats.data.database.entity.DataPointTemplateEntity
import com.humangamestats.data.database.entity.StatCategoryEntity
import com.humangamestats.data.database.entity.StatEntity
import com.humangamestats.data.database.entity.StatRecordEntity

/**
 * Room database for Human Game Stats app.
 * 
 * Contains four tables:
 * - stat_categories: Categories that group stats together
 * - stats: Individual stats within categories (with multiple data points)
 * - stat_records: Data entries for each stat (with multiple values)
 * - data_point_templates: Reusable data point configurations
 * 
 * Database is stored at: /data/data/com.humangamestats/databases/human_game_stats.db
 */
@Database(
    entities = [
        StatCategoryEntity::class,
        StatEntity::class,
        StatRecordEntity::class,
        DataPointTemplateEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Get the StatCategoryDao for category operations.
     */
    abstract fun statCategoryDao(): StatCategoryDao
    
    /**
     * Get the StatDao for stat operations.
     */
    abstract fun statDao(): StatDao
    
    /**
     * Get the StatRecordDao for record operations.
     */
    abstract fun statRecordDao(): StatRecordDao
    
    /**
     * Get the DataPointTemplateDao for template operations.
     */
    abstract fun dataPointTemplateDao(): DataPointTemplateDao
    
    companion object {
        /**
         * Database name.
         */
        const val DATABASE_NAME = "human_game_stats.db"
        
        /**
         * Migration from version 1 to version 2.
         * 
         * Adds:
         * - data_point_templates table for reusable data point configurations
         * - data_points_json column to stats table for multiple data points
         * - template_id column to stats table for template reference
         * - values_json column to stat_records table for multiple values
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create data_point_templates table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS data_point_templates (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        description TEXT NOT NULL DEFAULT '',
                        data_points_json TEXT NOT NULL DEFAULT '[]',
                        is_system INTEGER NOT NULL DEFAULT 0,
                        sort_order INTEGER NOT NULL DEFAULT 0,
                        created_at INTEGER NOT NULL DEFAULT 0,
                        updated_at INTEGER NOT NULL DEFAULT 0
                    )
                """)
                
                // Add data_points_json column to stats table
                database.execSQL("""
                    ALTER TABLE stats ADD COLUMN data_points_json TEXT NOT NULL DEFAULT '[]'
                """)
                
                // Add template_id column to stats table
                database.execSQL("""
                    ALTER TABLE stats ADD COLUMN template_id INTEGER DEFAULT NULL
                """)
                
                // Create index on template_id
                database.execSQL("""
                    CREATE INDEX IF NOT EXISTS index_stats_template_id ON stats(template_id)
                """)
                
                // Add values_json column to stat_records table
                database.execSQL("""
                    ALTER TABLE stat_records ADD COLUMN values_json TEXT NOT NULL DEFAULT '[]'
                """)
                
                // Migrate existing stats to use data_points_json
                // Convert the existing stat_type and type_label into a single data point JSON
                database.execSQL("""
                    UPDATE stats SET data_points_json = 
                        '[{"label":"' || type_label || '","type":"' || stat_type || '","unit":"","minValue":null,"maxValue":null,"step":null}]'
                    WHERE data_points_json = '[]'
                """)
                
                // Migrate existing stat_records to use values_json
                // Convert the existing value into a single value JSON
                database.execSQL("""
                    UPDATE stat_records SET values_json =
                        '[{"dataPointIndex":0,"value":"' || REPLACE(value, '"', '\"') || '"}]'
                    WHERE values_json = '[]' AND value != ''
                """)
            }
        }
        
        /**
         * Migration from version 2 to version 3.
         *
         * Adds:
         * - default_sort_option column to stat_categories table for pinned sort preferences
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add default_sort_option column to stat_categories table
                database.execSQL("""
                    ALTER TABLE stat_categories ADD COLUMN default_sort_option TEXT DEFAULT NULL
                """)
            }
        }
    }
}
