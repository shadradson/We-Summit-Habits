package com.humangamestats.di

import android.content.Context
import androidx.room.Room
import com.humangamestats.data.database.AppDatabase
import com.humangamestats.data.database.dao.DataPointTemplateDao
import com.humangamestats.data.database.dao.StatCategoryDao
import com.humangamestats.data.database.dao.StatDao
import com.humangamestats.data.database.dao.StatRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provide the Room database instance.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()
    }
    
    /**
     * Provide the StatCategoryDao.
     */
    @Provides
    @Singleton
    fun provideStatCategoryDao(database: AppDatabase): StatCategoryDao {
        return database.statCategoryDao()
    }
    
    /**
     * Provide the StatDao.
     */
    @Provides
    @Singleton
    fun provideStatDao(database: AppDatabase): StatDao {
        return database.statDao()
    }
    
    /**
     * Provide the StatRecordDao.
     */
    @Provides
    @Singleton
    fun provideStatRecordDao(database: AppDatabase): StatRecordDao {
        return database.statRecordDao()
    }
    
    /**
     * Provide the DataPointTemplateDao.
     */
    @Provides
    @Singleton
    fun provideDataPointTemplateDao(database: AppDatabase): DataPointTemplateDao {
        return database.dataPointTemplateDao()
    }
}
