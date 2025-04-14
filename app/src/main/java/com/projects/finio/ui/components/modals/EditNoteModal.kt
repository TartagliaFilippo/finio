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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.projects.finio.data.local.entity.Note

@Composable
fun EditNoteModal(
    modifier: Modifier = Modifier,
    showModal: Boolean,
    note: Note?,
    errorMessage: String?,
    onNoteTitleChange: (String) -> Unit,
    onNoteContentChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var titleError by remember { mutableStateOf<String?>(null) }
    var contentError by remember { mutableStateOf<String?>(null) }

    if (showModal && note != null) {
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
                            text = "Modifica ${note.title}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        OutlinedTextField(
                            value = note.title,
                            onValueChange = {
                                onNoteTitleChange(it)
                                titleError = when {
                                    it.isBlank() -> "Il titolo non può essere vuoto"
                                    it.length > 30 -> "Il titolo non può superare i 30 caratteri"
                                    else -> null
                                }
                            },
                            label = { Text("Titolo") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.9f),
                            isError = titleError != null || errorMessage != null
                        )

                        val errorText = titleError ?: errorMessage
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
                            value = note.content,
                            onValueChange = {
                                onNoteContentChange(it)
                                contentError = when {
                                    it.isBlank() -> "Scrivi qualcosa su questa nota"
                                    else -> null
                                }
                            },
                            label = { Text("Testo") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )

                        val contentErrorText = contentError ?: errorMessage
                        if (contentErrorText != null) {
                            Text(
                                text = contentErrorText,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text("Annulla")
                            }
                            Button(
                                enabled = titleError == null && contentError == null && errorMessage == null,
                                onClick = {
                                    if (note.title.isBlank()) {
                                        titleError = "Il titolo non può essere vuoto"
                                    } else if (note.title.length > 30) {
                                        titleError = "Il titolo non può superare i 30 caratteri"
                                    } else if (note.content.isBlank()) {
                                        contentError = "Scrivi qualcosa su questa nota"
                                    } else {
                                        onConfirm()
                                    }
                                }
                            ) {
                                Text("Salva")
                            }
                        }

                    }
                }
            }
        }
    }
}