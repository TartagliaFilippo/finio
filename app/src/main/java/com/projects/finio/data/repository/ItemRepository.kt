package com.projects.finio.data.repository

import android.database.SQLException
import android.util.Log
import com.projects.finio.data.local.dao.ItemDao
import com.projects.finio.data.local.entity.Item
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(
    private val itemDao: ItemDao
) {
    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    suspend fun insertItem(item: Item): Result<Unit> {
        return try {
            itemDao.insert(item)
            Result.success(Unit)
        } catch (e: SQLException) {
            Log.e("RoomError", "Errore durante l'inserimento: ${e.message}")
            Result.failure(Exception("L'articolo esiste già"))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Errore sconosciuto"

            return if (errorMessage.contains("UNIQUE constraint failed", ignoreCase = true)) {
                Log.e("RoomError", "Errore di unicità: $errorMessage")
                Result.failure(Exception("Il nome dell'articolo esiste già"))
            } else {
                Log.e("RoomError", "Errore generico: $errorMessage")
                Result.failure(Exception("Errore durante l'inserimento"))
            }
        }
    }
}