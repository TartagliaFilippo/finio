package com.projects.finio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.finio.data.local.entity.Item
import com.projects.finio.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private fun validateItem(
        name: String,
        categoryId: Int
    ): String? {
        return when {
            name.isBlank() -> "l'articolo deve avere un nome"
            name.length > 30 -> "Il nome non può superare i 30 caratteri"
            categoryId <= 0 -> "Seleziona una categoria valida"
            else -> null
        }
    }

    val allItems: StateFlow<List<Item>> = repository
        .allItems
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun addItem(
        name: String,
        description: String?,
        categoryId: Int,
        subscription: Boolean?,
    ): Result<Unit> {
        val error = validateItem(name, categoryId)

        if (error != null) {
            _errorMessage.value = error
            return Result.failure(Exception(error))
        }

        viewModelScope.launch {
            val result = repository.insertItem(Item(
                name = name,
                description = description,
                categoryId = categoryId,
                subscription = subscription == false,
                expireDate = 0,
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

    fun updateItem(item: Item): Result<Unit> {
        val error = validateItem(item.name, item.categoryId)

        if (error != null) {
            _errorMessage.value = error
            return Result.failure(Exception(error))
        }

        viewModelScope.launch {
            val result = repository.updateItem(item)

            result.onFailure {
                _errorMessage.value = it.message
            }

            result.onSuccess {
                _errorMessage.value = null
            }
        }
        return Result.success(Unit)
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteItem(id)
            } catch (_: Exception) {
                _errorMessage.value = "Errore nella cancellazione"
            }
        }
    }
}