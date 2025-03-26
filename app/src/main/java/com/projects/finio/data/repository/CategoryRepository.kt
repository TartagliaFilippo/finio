package com.projects.finio.data.repository

import android.os.Build
import android.util.Log
import com.projects.finio.data.local.dao.CategoryDao
import com.projects.finio.data.local.entity.Category
import kotlinx.coroutines.flow.Flow
import java.sql.SQLException
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    fun getRootCategories(): Flow<List<Category>> {
        return categoryDao.getRootCategories()
    }

    fun getSubCategories(parentId: Int): Flow<List<Category>> {
        return categoryDao.getSubcategories(parentId)
    }

    suspend fun insertCategory(category: Category): Result<Unit>{
        return try {
            categoryDao.insert(category)
            Result.success(Unit)
        } catch (e: SQLException) {
            // intercetto l'errore di unicità con SQLException
            Log.e("ROOM ERROR", "Errore durante l'inserimento: ${e.message}")
            Result.failure(Exception("La categoria esiste già"))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Errore sconosciuto"

            // intercetto l'errore di unicità manualmente se passa e cerco gli errori generici
            return if (errorMessage.contains("UNIQUE constraint failed", ignoreCase = true)) {
                Log.e("RoomError", "Errore di unicità: $errorMessage")
                Result.failure(Exception("Il titolo della categoria esiste già!"))
            } else {
                Log.e("RoomError", "Errore generico: $errorMessage")
                Result.failure(Exception("Errore durante l'inserimento"))
            }
        }
    }

    suspend fun updateCategory(category: Category): Result<Unit> {
        return try {
            // aggiorno la categoria con il corretto timestamp
            val updatedCategory = category.copy(
                updatedAt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Instant.now().epochSecond
                } else {
                    System.currentTimeMillis() / 1000
                }
            )
            categoryDao.updateCategory(updatedCategory)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAllCategories() {
        categoryDao.deleteAllCategories()
    }

    suspend fun deleteCategory(id: Int) {
        categoryDao.deleteCategory(id)
    }
}