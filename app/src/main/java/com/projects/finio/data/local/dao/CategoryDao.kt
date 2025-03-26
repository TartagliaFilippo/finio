package com.projects.finio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.projects.finio.data.local.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY created_at DESC")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?

    @Query("SELECT * FROM categories WHERE parent_id IS NULL ORDER BY title ASC")
    fun getRootCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE parent_id = :parentId")
    fun getSubcategories(parentId: Int): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("UPDATE categories SET updated_at = :timestamp WHERE id = :categoryId")
    suspend fun updateTimestamp(categoryId: Int, timestamp: Long)

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("DELETE FROM categories where id = :id")
    suspend fun deleteCategory(id: Int)
}