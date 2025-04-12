package com.projects.finio.ui.components.modals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.projects.finio.data.local.entity.Schedule

@Composable
fun AddScheduleModal(
    modifier: Modifier,
    showModal: Boolean,
    title: String,
    scheduleTitle: String,
    scheduleDescription: String,
    errorMessage: String?,
    onScheduleTitleChange: (String) -> Unit,
    onScheduleDescriptionChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showModal) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Nuova Lista",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = scheduleTitle,
                    onValueChange = { onScheduleTitleChange(it) },
                    label = { Text("Titolo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = scheduleDescription,
                    onValueChange = { onScheduleTitleChange(it) },
                    label = { Text("Corpo (una voce per riga)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 10,
                    textStyle = LocalTextStyle.current.copy(lineHeight = 22.sp)
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            val cleanDescription = scheduleDescription
                                .split("\n")
                                .filter { it.isNotBlank() }
                                .joinToString("\n")

                            val schedule = Schedule(
                                title = title.trim(),
                                description = cleanDescription,
                                total = 0.0,
                                expireDate = null
                            )

                            onConfirm()
                            onDismiss()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Crea")
                }
            }
        }
    }
}