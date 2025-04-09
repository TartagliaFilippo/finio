package com.projects.finio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.finio.data.local.entity.Category
import com.projects.finio.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

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

    private fun validateItem(
        name: String,
    ): String? {
        return when {
            name.isBlank() -> "La categoria deve avere un titolo"
            name.length > 30 -> "Il titolo non può superare i 30 caratteri"
            else -> null
        }
    }

    fun addCategory(
        title: String,
        description: String?,
        parentId: Int?
    ): Result<Unit> {
        val error = validateItem(title)

        if (error != null) {
            _errorMessage.value = error
            return Result.failure(Exception(error))
        }

        viewModelScope.launch {
            val result = repository.insertCategory(Category(
                title = title,
                description = description,
                color = "0xFFA8D0DB",
                parentId = parentId,
            ))

            result.onFailure {
                _errorMessage.value = it.message
            }

            result.onSuccess {
                _errorMessage.value = null
            }
        }
        return Result.success(Unit)
    }

    fun updateCategory(category: Category): Result<Unit> {
        val error = validateItem(category.title)

        if (error != null) {
            _errorMessage.value = error
            return Result.failure(Exception(error))
        }

        viewModelScope.launch {

            val result = repository.updateCategory(category)

            result.onFailure {
                _errorMessage.value = it.message
            }

            result.onSuccess {
                _errorMessage.value = null
            }
        }
        return Result.success(Unit)
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
            } catch (_: Exception) {
                _errorMessage.value = "Errore nella cancellazione"
            }
        }
    }
}