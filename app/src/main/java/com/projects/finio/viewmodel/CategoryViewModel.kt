package com.projects.finio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.finio.data.local.entity.Category
import com.projects.finio.data.repository.CategoryRepository
import com.projects.finio.viewmodel.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // textField di ricerca sulla vista
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // tutte le categorie
    val allCategories: StateFlow<List<Category>> = repository
        .allCategories
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    // categorie filtrate
    val filteredCategories = allCategories
        .combine(_searchQuery) { categories, query ->
            if (query.isBlank()) {
                categories
            } else {
                categories.filter { it.title.contains(query, ignoreCase = true) }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    // solo le categorie padre
    val rootCategories: StateFlow<List<Category>> = repository
        .getRootCategories()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun getSubCategories(parentId: Int) = repository.getSubCategories(parentId)

    // aggiorno l'input di ricerca
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

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

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            if (category.title.isBlank()) {
                errorMessage = "Il titolo non può essere vuoto"
                return@launch
            }

            if (category.title.length > 30) {
                errorMessage = "Il titolo non può superare i 30 caratteri"
                return@launch
            }

            val result = repository.updateCategory(category)

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
                snackbarManager.showMessage("Categoria eliminata con successo")
            } catch (_: Exception) {
                snackbarManager.showMessage("Errore nella cancellazione")
            }

            delay(2000)
            snackbarManager.clearMessage()
        }
    }
}