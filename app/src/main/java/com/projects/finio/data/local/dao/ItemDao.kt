package com.projects.finio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.projects.finio.data.local.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items ORDER BY name DESC")
    fun getAllItems(): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteItem(id: Int)
}