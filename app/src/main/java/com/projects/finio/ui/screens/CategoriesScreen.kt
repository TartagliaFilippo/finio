package com.projects.finio.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.projects.finio.viewmodel.snackbar.SnackbarManager
import com.projects.finio.viewmodel.CategoryViewModel
import com.projects.finio.data.local.entity.Category
import com.projects.finio.ui.components.AppDrawer
import com.projects.finio.ui.components.CustomSnackbar
import com.projects.finio.ui.components.formatTimestampUniversal
import com.projects.finio.ui.components.modals.AddCategoryModal
import com.projects.finio.ui.components.modals.ConfirmDialog
import com.projects.finio.ui.components.modals.EditCategoryModal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var newCategoryModal by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }
    var editCategory by remember { mutableStateOf<Category?>(null) }

    var categoryTitle by remember { mutableStateOf("") }
    var categoryDescription by remember { mutableStateOf("") }
    var categorySelected by remember { mutableStateOf<Category?>(null) }

    val categories by viewModel.allCategories.collectAsState()
    val rootCategories = viewModel.rootCategories.collectAsState().value
    val snackbarManager = remember { SnackbarManager() }
    val snackbarMessage by snackbarManager.snackbarMessage.collectAsState()

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
                    title = { Text("Category") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "open drawer")
                        }
                    }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(categories) { category ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = category.title, fontWeight = FontWeight.Bold)
                                    Text(text = category.description ?: "Nessuna descrizione")
                                }
                                Text(
                                    text = formatTimestampUniversal(category.createdAt),
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                IconButton(onClick = { editCategory = category }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Modifica categoria ${editCategory?.id}"
                                    )
                                }

                                Button(onClick = { categoryToDelete = category }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Elimina Categoria")
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { newCategoryModal = true },
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.BottomEnd),
                containerColor = Color(0xFF6200EA)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuova Categoria")
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
                        backgroundColor = Color(0xFF6200EA),
                        textColor = Color.White
                    )
                }
            }

            // modale per aggiungere una nuova categoria
            AddCategoryModal(
                modifier = Modifier,
                showModal = newCategoryModal,
                title = "Nuova Categoria",
                categoryTitle = categoryTitle,
                categoryDescription = categoryDescription,
                categories = categories,
                rootCategories = rootCategories,
                categorySelected = categorySelected,
                errorMessage = viewModel.errorMessage,
                onCategoryTitleChange = { categoryTitle = it },
                onCategoryDescriptionChange = { categoryDescription = it },
                onCategorySelect = { categorySelected = it },
                onConfirm = {
                    viewModel.addCategory(categoryTitle, categoryDescription.takeIf { it.isNotBlank() }, categorySelected?.id)
                    newCategoryModal = false
                    snackbarManager.showMessage("Categoria aggiunta con successo!")
                    categoryTitle = ""
                    categoryDescription = ""
                    categorySelected = null
                },
                onDismiss = { newCategoryModal = false }
            )

            // modale di modifica
            EditCategoryModal(
                showModal = editCategory != null,
                category = editCategory,
                categories = categories,
                rootCategories = rootCategories,
                errorMessage = viewModel.errorMessage,
                onCategoryTitleChange = { newTitle ->
                    editCategory = editCategory?.copy(title = newTitle)
                },
                onCategoryDescriptionChange = { newDesc ->
                    editCategory = editCategory?.copy(description = newDesc)
                },
                onCategorySelect = { selectedCategory ->
                    editCategory = editCategory?.copy(parentId = selectedCategory.id)
                },
                onConfirm = {
                    editCategory?.let { category ->
                        viewModel.updateCategory(category)
                    }
                    editCategory = null
                },
                onDelete = {
                    editCategory?.let { category ->
                        viewModel.deleteCategory(category.id)
                    }
                    editCategory = null
                },
                onDismiss = { editCategory = null }
            )

            // modale di conferma cancellazione
            categoryToDelete?.let { category ->
                ConfirmDialog(
                    title = "Cancellazione della categoria ${category.title}",
                    message = "Sei sicuro di voler eliminare questa categoria?",
                    onConfirm = {
                        viewModel.deleteCategory(category.id)
                        snackbarManager.showMessage("Categoria '${category.title}' eliminata!")
                        categoryToDelete = null
                    },
                    onDismiss = { categoryToDelete = null }
                )
            }
        }
    }
}