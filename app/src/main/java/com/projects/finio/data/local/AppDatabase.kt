package com.projects.finio.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.projects.finio.data.local.dao.CategoryDao
import com.projects.finio.data.local.dao.ItemDao
import com.projects.finio.data.local.dao.NoteDao
import com.projects.finio.data.local.dao.PeriodDao
import com.projects.finio.data.local.dao.PriceDao
import com.projects.finio.data.local.dao.ScheduleDao
import com.projects.finio.data.local.dao.ScheduleItemDao
import com.projects.finio.data.local.dao.SubscriptionDao
import com.projects.finio.data.local.entity.Category
import com.projects.finio.data.local.entity.Note
import com.projects.finio.data.local.entity.Item
import com.projects.finio.data.local.entity.Period
import com.projects.finio.data.local.entity.Price
import com.projects.finio.data.local.entity.Schedule
import com.projects.finio.data.local.entity.ScheduleItem
import com.projects.finio.data.local.entity.Subscription

@Database(
    entities = [
        Category::class,
        Note::class,
        Item::class,
        Period::class,
        Price::class,
        Schedule::class,
        ScheduleItem::class,
        Subscription::class
               ],
    version = 11,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao
    abstract fun itemDao(): ItemDao
    abstract fun periodDao(): PeriodDao
    abstract fun priceDao(): PriceDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun scheduleItemDao(): ScheduleItemDao
    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database_finio"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}