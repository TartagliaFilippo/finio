package com.projects.finio.room.repository

import android.util.Log
import com.projects.finio.room.dao.CategoryDao
import com.projects.finio.room.entity.Category
import kotlinx.coroutines.flow.Flow
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    fun getSubCategories(parentId: Int): Flow<List<Category>> {
        return categoryDao.getSubcategories(parentId)
    }

    suspend fun insertCategory(category: Category): Result<Unit>{
        return try {
            categoryDao.insert(category)
            Result.success(Unit)
        } catch (e: SQLException) {
            Log.e("ROOM ERROR", "Errore durante l'inserimento: ${e.message}")
            Result.failure(Exception("La categoria esiste già"))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Errore sconosciuto"

            return if (errorMessage.contains("UNIQUE constraint failed", ignoreCase = true)) {
                Log.e("RoomError", "Errore di unicità: $errorMessage")
                Result.failure(Exception("Il titolo della categoria esiste già!"))
            } else {
                Log.e("RoomError", "Errore generico: $errorMessage")
                Result.failure(Exception("Errore durante l'inserimento"))
            }
        }
    }

    suspend fun deleteAllCategories() {
        categoryDao.deleteAllCategories()
    }

    suspend fun deleteCategory(id: Int) {
        categoryDao.deleteCategory(id)
    }
}