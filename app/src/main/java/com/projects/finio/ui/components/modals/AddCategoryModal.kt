package com.projects.finio.ui.components.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.projects.finio.data.local.entity.Category

@Composable
fun AddCategoryModal(
    modifier: Modifier,
    showModal: Boolean,
    title: String,
    categoryTitle: String,
    categoryDescription: String,
    categories: List<Category>,
    rootCategories: List<Category>,
    categorySelected: Category?,
    errorMessage: String?,
    onCategoryTitleChange: (String) -> Unit,
    onCategoryDescriptionChange: (String) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var expandedSelect by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    if (showModal) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        OutlinedTextField(
                            value = categoryTitle,
                            onValueChange = {
                                onCategoryTitleChange(it)
                                localError = when {
                                    it.isBlank() -> "Il titolo non può essere vuoto"
                                    it.length > 30 -> "Il titolo non può superare i 30 caratteri"
                                    categories.any { category -> category.title == it } -> "Il titolo esiste già!"
                                    else -> null
                                }
                            },
                            label = { Text("Titolo") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.9f),
                            isError = localError != null || errorMessage != null
                        )

                        // Messaggi di errore (sia locale che dal repository)
                        val errorText = localError ?: errorMessage
                        if (errorText != null) {
                            Text(
                                text = errorText,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = categoryDescription,
                            onValueChange = onCategoryDescriptionChange,
                            label = { Text("Descrizione") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { expandedSelect = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = categorySelected?.title ?: "Seleziona categoria padre")
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                            }

                            DropdownMenu(
                                expanded = expandedSelect,
                                onDismissRequest = { expandedSelect = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                rootCategories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.title) },
                                        onClick = {
                                            onCategorySelect(category)
                                            expandedSelect = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text("Annulla")
                            }
                            Button(
                                enabled = localError == null && errorMessage == null,
                                onClick = {
                                    if (categoryTitle.isBlank()) {
                                        localError = "Il titolo non può essere vuoto"
                                    } else if (categoryTitle.length > 30) {
                                        localError = "Il titolo non può superare i 30 caratteri"
                                    } else {
                                        onConfirm()
                                    }
                                }
                            ) {
                                Text("Conferma")
                            }
                        }
                    }
                }
            }
        }
    }
}