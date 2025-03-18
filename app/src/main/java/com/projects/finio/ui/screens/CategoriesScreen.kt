package com.projects.finio.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.projects.finio.data.viewModels.CategoryViewModel
import com.projects.finio.ui.components.HeaderBar
import com.projects.finio.ui.components.NavigationSlider

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    var showModal by remember { mutableStateOf(false) }

    val categories by viewModel.allCategories.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ){
        HeaderBar(
            onClick = { showModal = !showModal }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { showModal = false },
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = { viewModel.addCategory(
                        title = "nuova categoria",
                        description = "descrizione",
                        parentId = null
                    ) }
                ) {
                    Text("Aggiungi Categoria")
                }

                categories.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = category.title,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }


    if (showModal) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxHeight()
        ) {
            NavigationSlider(navController)
        }
    }
}