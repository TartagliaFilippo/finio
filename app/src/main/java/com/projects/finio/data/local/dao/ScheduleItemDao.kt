package com.projects.finio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projects.finio.data.local.entity.Schedule
import com.projects.finio.data.local.entity.Item
import com.projects.finio.data.local.entity.ScheduleItem

@Dao
interface ScheduleItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItemSchedule(scheduleItem: ScheduleItem)

    @Query("SELECT * FROM schedules INNER JOIN schedule_items ON schedules.id = schedule_items.schedule_id WHERE schedule_items.item_id = :itemId")
    fun getAllSchedulesFromItem(itemId: Int): List<Schedule>

    @Query("SELECT * FROM items INNER JOIN schedule_items ON items.id = schedule_items.item_id WHERE schedule_items.schedule_id = :scheduleId")
    fun getAllItemsFromSchedule(scheduleId: Int): List<Item>

    @Query("DELETE FROM schedule_items WHERE item_id = :itemId AND schedule_id = :scheduleId")
    suspend fun deleteItemFromSchedule(itemId: Int, scheduleId: Int)

    @Query("DELETE FROM schedule_items WHERE schedule_id = :scheduleId")
    suspend fun deleteAllItemsFromSchedule(scheduleId: Int)
}