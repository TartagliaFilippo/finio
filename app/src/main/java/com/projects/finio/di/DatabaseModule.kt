package com.projects.finio.di

import android.content.Context
import com.projects.finio.data.local.AppDatabase
import com.projects.finio.data.local.dao.CategoryDao
import com.projects.finio.data.local.dao.ItemDao
import com.projects.finio.data.local.dao.NoteDao
import com.projects.finio.data.local.dao.PeriodDao
import com.projects.finio.data.local.dao.ScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    fun provideItemDao(database: AppDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    fun provideScheduleDao(database: AppDatabase): ScheduleDao {
        return database.scheduleDao()
    }

    @Provides
    fun providePeriodDao(database: AppDatabase): PeriodDao {
        return database.periodDao()
    }
}