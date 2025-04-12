package com.projects.finio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.finio.data.local.entity.Note
import com.projects.finio.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private fun validateNote(
        title: String,
        content: String,
        categoryId: Int
    ): String? {
        return when {
            title.isBlank() -> "Assegna un titolo alla nota"
            title.length > 30 -> "Il titolo non può superare i 30 caratteri"
            content.isBlank() -> "La nota non può essere vuota"
            categoryId <= 0 -> "Seleziona una categoria"
            else -> null
        }
    }

    val allNotes: StateFlow<List<Note>> = repository
        .allNotes
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun addNote(
        title: String,
        content: String,
        categoryId: Int
    ): Result<Unit> {
        val error = validateNote(title, content, categoryId)

        if (error != null) {
            _errorMessage.value = error
            return Result.failure(Exception(error))
        }

        viewModelScope.launch {
            val result = repository.addNote(
                Note(
                    title = title,
                    content = content,
                    categoryId = categoryId,
                    periodId = null,
                    expireDate = 0
                )
            )

            result.onFailure {
                _errorMessage.value = it.message
            }

            result.onSuccess {
                _errorMessage.value = null
            }

        }
        return Result.success(Unit)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.deleteNote(note)
            } catch (_: Exception) {
                _errorMessage.value = "Errore nella cancellazione"
            }

        }
    }
}