package com.projects.finio.data.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.finio.room.entity.Category
import com.projects.finio.room.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    val allCategories: StateFlow<List<Category>> = repository
        .allCategories
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun getSubCategories(parentId: Int) = repository.getSubCategories(parentId)

    fun addCategory(
        title: String,
        description: String?,
        parentId: Int?
    ) {
        viewModelScope.launch {
            if (title.isBlank()) {
                errorMessage = "Il titolo non può essere vuoto"
                return@launch
            }

            if (title.length > 30) {
                errorMessage = "Il titolo non può superare i 30 caratteri"
                return@launch
            }

            val result = repository.insertCategory(Category(
                title = title,
                description = description,
                parentId = parentId
            ))

            result.onFailure {
                errorMessage = it.message
            }

            result.onSuccess {
                errorMessage = null
            }
        }
    }

    fun deleteAllCategories() {
        viewModelScope.launch {
            repository.deleteAllCategories()
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteCategory(id)
                _uiState.update { it.copy(message = "Categoria eliminata con successo") }
            } catch (_: Exception) {
                _uiState.update { it.copy(message = "Errore nella cancellazione") }
            }

            delay(2000)
            _uiState.update { it.copy(message = null) }
        }
    }
}

data class CategoryUiState(
    val message: String? = null
)