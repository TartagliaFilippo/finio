package com.projects.finio.data.repository

import android.database.SQLException
import android.util.Log
import com.projects.finio.data.local.dao.ScheduleDao
import com.projects.finio.data.local.entity.Schedule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao
) {
    val allSchedules: Flow<List<Schedule>> = scheduleDao.getAllSchedules()

    suspend fun insertSchedule(schedule: Schedule): Result<Unit> {
        return try {
            scheduleDao.insertSchedule(schedule)
            Result.success(Unit)
        } catch (e: SQLException) {
            Log.e("RoomError", "Errore durante l'inserimento: ${e.message}")
            Result.failure(Exception("La lista esiste già"))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Errore sconosciuto"

            return if (errorMessage.contains("UNIQUE constraint failed", ignoreCase = true)) {
                Log.e("RoomError", "Errore di unicità: $errorMessage")
                Result.failure(Exception("Il nome della lista esiste già"))
            } else {
                Log.e("RoomError", "Errore generico: $errorMessage")
                Result.failure(Exception("Errore durante l'inserimento"))
            }
        }
    }
}