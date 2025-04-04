package com.projects.finio.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.projects.finio.data.local.entity.Period
import kotlinx.coroutines.flow.Flow

@Dao
interface PeriodDao {

    @Query("SELECT * FROM periods ORDER BY value DESC")
    fun getAllPeriods(): Flow<List<Period>>

}