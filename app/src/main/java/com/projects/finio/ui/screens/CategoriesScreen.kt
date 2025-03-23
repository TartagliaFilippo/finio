package com.projects.finio.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.projects.finio.data.ui.SnackbarManager
import com.projects.finio.data.viewModels.CategoryViewModel
import com.projects.finio.room.entity.Category
import com.projects.finio.ui.components.CustomSnackbar
import com.projects.finio.ui.components.HeaderBar
import com.projects.finio.ui.components.NavigationSlider
import com.projects.finio.ui.components.NewCategoryModal
import com.projects.finio.ui.components.formatTimestampUniversal
import java.nio.file.WatchEvent

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel(),
    snackbarManager: SnackbarManager = hiltViewModel()
) {
    var showModal by remember { mutableStateOf(false) }
    var newCategoryModal by remember { mutableStateOf(false) }

    var categoryTitle by remember { mutableStateOf("") }
    var categoryDescription by remember { mutableStateOf("") }
    var categorySelected by remember { mutableStateOf<Category?>(null) }

    val categories by viewModel.allCategories.collectAsState()
    val snackbarMessage by snackbarManager.snackbarMessage.collectAsState()

    LaunchedEffect(categories) {
        Log.d("CategoriesDebug", "Lista aggiornata: $categories")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBar(onClick = { showModal = !showModal })

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
                        Button(onClick = {
                            viewModel.deleteCategory(category.id)
                            snackbarManager.showMessage("Categoria eliminata!")
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Elimina Categoria")
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Button(
                onClick = { newCategoryModal = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuova Categoria")
            }
        }

        NewCategoryModal(
            modifier = Modifier,
            showModal = newCategoryModal,
            title = "Nuova Categoria",
            categoryTitle = categoryTitle,
            categoryDescription = categoryDescription,
            categories = categories,
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

        if (showModal) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxHeight()
            ) {
                NavigationSlider(navController)
            }
        }


        snackbarMessage?.let { message ->
            CustomSnackbar(
                message = message,
                onDismiss = { snackbarManager.clearMessage() },
                backgroundColor = Color(0xFF6200EA),
                textColor = Color.White
            )
        }
    }
}