package com.projects.finio.data.repository

import android.database.SQLException
import android.util.Log
import com.projects.finio.data.local.dao.NoteDao
import com.projects.finio.data.local.entity.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun addNote(note: Note): Result<Unit> {
        return try {
            noteDao.insertNote(note)
            Result.success(Unit)
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Errore sconosciuto"

            return if (errorMessage.contains("UNIQUE constraint failed", ignoreCase = true)) {
                Log.e("RoomError", "Errore di unicità: $errorMessage")
                Result.failure(Exception("Il nome della nota esiste già"))
            } else {
                Log.e("RoomError", "Errore generico: $errorMessage")
                Result.failure(Exception("Errore durante l'inserimento"))
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}