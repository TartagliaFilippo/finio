package com.projects.finio.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.projects.finio.data.local.entity.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedules ORDER BY created_at DESC")
    fun getAllLists(): Flow<List<Schedule>>

}