package com.projects.finio.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.projects.finio.data.local.entity.Category
import com.projects.finio.data.local.entity.Item
import com.projects.finio.ui.components.AppDrawer
import com.projects.finio.ui.components.CustomSnackbar
import com.projects.finio.ui.components.modals.AddItemModal
import com.projects.finio.ui.components.modals.ConfirmDialog
import com.projects.finio.ui.components.modals.EditItemModal
import com.projects.finio.viewmodel.CategoryViewModel
import com.projects.finio.viewmodel.ItemViewModel
import com.projects.finio.viewmodel.snackbar.SnackbarManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    navController: NavController,
    viewModel: ItemViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var newItemModal by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<Item?>(null) }
    var editItem by remember { mutableStateOf<Item?>(null) }

    var itemName by remember { mutableStateOf("") }
    var itemDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var itemShow by remember { mutableStateOf<Item?>(null) }

    val items by viewModel.allItems.collectAsState()
    val categories by categoryViewModel.allCategories.collectAsState()
    val snackbarManager = remember { SnackbarManager() }
    val snackbarMessage by snackbarManager.snackbarMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val columns = 2
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / columns

    AppDrawer(
        drawerState = drawerState,
        onItemClick = { selectedItem ->
            scope.launch { drawerState.close() }
            navController.navigate(selectedItem)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = { Text("Items") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "open drawer")
                        }
                    }
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .clickable { itemShow = if (itemShow  == item) null else item }
                                    .zIndex(if (itemShow  == item) 1f else 0f),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Yellow),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .size(cardSize - 16.dp),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Text(text = item.name, fontWeight = FontWeight.Bold)
                                            if (itemShow == item) {
                                                Text(text = item.description ?: "Nessuna descrizione")
                                            }
                                        }
                                    }

                                    if (itemShow == item) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Button(
                                                onClick = { editItem = item },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent,
                                                    contentColor = Color.Black
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Modifica Articolo ${editItem?.id}"
                                                )
                                                Text("Modifica")
                                            }

                                            Button(
                                                onClick = { itemToDelete = item },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent,
                                                    contentColor = Color.Red
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Elimina Articolo"
                                                )
                                                Text("Elimina")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { newItemModal = true },
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.BottomEnd),
                containerColor = Color.Magenta
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuovo Articolo"
                )
            }

            snackbarMessage?.let { message ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    CustomSnackbar(
                        message = message,
                        onDismiss = { snackbarManager.clearMessage() },
                        backgroundColor = Color.Cyan,
                        textColor = Color.Black
                    )
                }
            }

            AddItemModal(
                modifier = Modifier,
                showModal = newItemModal,
                title = "Nuovo Articolo",
                itemName = itemName,
                itemDescription = itemDescription,
                categorySelected = selectedCategory,
                items = items,
                categories = categories,
                errorMessage = errorMessage,
                onItemNameChange = { itemName = it },
                onItemDescriptionChange = { itemDescription = it },
                onCategorySelect = { selectedCategory = it },
                onConfirm = {
                    viewModel.addItem(
                        name = itemName,
                        description = itemDescription.takeIf { it.isNotBlank() },
                        categoryId = selectedCategory?.id ?: -1,
                        subscription = false,
                    )
                    newItemModal = false
                    snackbarManager.showMessage("Articolo aggiunto con successo!")
                    itemName = ""
                    itemDescription = ""
                    selectedCategory = null
                },
                onDismiss = { newItemModal = false }
            )

            EditItemModal(
                showModal = editItem != null,
                item = editItem,
                items = items,
                categories = categories,
                errorMessage = errorMessage,
                onItemNameChange = { newName ->
                    editItem = editItem?.copy(name = newName)
                },
                onItemDescriptionChange = { newDesc ->
                    editItem = editItem?.copy(description = newDesc)
                },
                onCategorySelect = { selectedCategory ->
                    editItem = editItem?.copy(categoryId = selectedCategory.id)
                },
                onConfirm = {
                    editItem?.let { item ->
                        viewModel.updateItem(item)
                    }
                    editItem = null
                },
                onDismiss = { editItem = null}
            )

            // modale di conferma cancellazione
            itemToDelete?.let { item ->
                ConfirmDialog(
                    title = "Cancellazione dell'articolo ${item.name}",
                    message = "Sei sicuro di voler eliminare questo articolo?",
                    onConfirm = {
                        viewModel.deleteItem(item.id)
                        snackbarManager.showMessage("Articolo '${item.name}' eliminato!")
                        itemToDelete = null
                    },
                    onDismiss = { itemToDelete = null }
                )
            }
        }
    }
}