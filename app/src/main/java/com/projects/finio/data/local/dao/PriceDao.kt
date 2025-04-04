package com.projects.finio.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.projects.finio.data.local.entity.Price
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceDao {

    @Query("SELECT * FROM prices ORDER BY created_at DESC")
    fun getAllPrices(): Flow<List<Price>>
    
}