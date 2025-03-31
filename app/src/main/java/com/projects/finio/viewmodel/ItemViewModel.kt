package com.projects.finio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.finio.data.local.entity.Item
import com.projects.finio.data.repository.ItemRepository
import com.projects.finio.viewmodel.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
        private set

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
        categoryId: Int? = 0,
        subscription: Boolean?,
        offer: Boolean?
    ) {
        viewModelScope.launch {
            if (name.isBlank()) {
                errorMessage = "L'articolo deve avere un nome"
                return@launch
            }

            if (name.length > 30) {
                errorMessage = "Il nome non può superare i 30 caratteri"
                return@launch
            }

            val result = repository.insertItem(Item(
                name = name,
                description = description,
                categoryId = categoryId,
                subscription = subscription == false,
                offer = offer == false,
                expireDate = 0,
            ))

            result.onFailure {
                errorMessage = it.message
                snackbarManager.showMessage("$errorMessage")

                delay(2000)
                snackbarManager.clearMessage()
            }

            result.onSuccess {
                errorMessage = null
            }
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            if (item.name.isBlank()) {
                errorMessage = "L'articolo deve avere un nome"
                return@launch
            }

            if (item.name.length > 30) {
                errorMessage = "Il nome non può superare i 30 caratteri"
                return@launch
            }

            val result = repository.updateItem(item)

            result.onFailure {
                errorMessage = it.message
            }

            result.onSuccess {
                errorMessage = null
            }
        }
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteItem(id)
                snackbarManager.showMessage("Articolo eliminato con successo!")
            } catch (_: Exception) {
                snackbarManager.showMessage("Errore nella cancellazione")
            }
        }
    }
}