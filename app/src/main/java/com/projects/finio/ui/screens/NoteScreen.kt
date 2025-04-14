package com.projects.finio.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.projects.finio.data.local.entity.Category
import com.projects.finio.data.local.entity.Note
import com.projects.finio.ui.components.AppDrawer
import com.projects.finio.ui.components.CustomSnackbar
import com.projects.finio.ui.components.formatTimestampUniversal
import com.projects.finio.ui.components.modals.AddNoteModal
import com.projects.finio.ui.components.modals.ConfirmDialog
import com.projects.finio.ui.components.modals.EditNoteModal
import com.projects.finio.viewmodel.NoteViewModel
import com.projects.finio.viewmodel.snackbar.SnackbarManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var addNoteModal by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    var editNote by remember { mutableStateOf<Note?>(null) }
    var showNote by remember { mutableStateOf<Note?>(null) }

    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val notes by viewModel.allNotes.collectAsState()
    val snackbarManager = remember { SnackbarManager() }
    val snackbarMessage by snackbarManager.snackbarMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

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
                    title = { Text("Notes") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "open drawer"
                            )
                        }
                    }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(notes) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .clickable {
                                    showNote = if (showNote == note) null else note
                                },
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = note.title, fontWeight = FontWeight.Bold)
                                    if (showNote == note) {
                                        Text(text = note.content)
                                    }
                                }

                                if (showNote == note) {
                                    Text(
                                        text = formatTimestampUniversal(note.createdAt),
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                        fontSize = 12.sp,
                                        color = Color.Red
                                    )
                                }
                            }
                            if (showNote == note) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        onClick = { editNote = note },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Modifica categoria ${editNote?.id}"
                                        )
                                        Text("Modifica")
                                    }

                                    Button(
                                        onClick = { noteToDelete = note },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = Color.Red
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Elimina Categoria"
                                        )
                                        Text("Elimina")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { addNoteModal = true },
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.BottomEnd),
                containerColor = Color.LightGray
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuova nota"
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

            AddNoteModal (
                modifier = Modifier,
                showModal = addNoteModal,
                title = "Nuova Nota",
                noteTitle = noteTitle,
                noteContent = noteContent,
                errorMessage = errorMessage,
                onNoteTitleChange = { noteTitle = it },
                onNoteContentChange = { noteContent = it },
                onConfirm = {
                    viewModel.addNote(noteTitle, noteContent, 1)
                    addNoteModal = false
                    snackbarManager.showMessage("Categoria aggiunta con successo!")
                    noteTitle = ""
                    noteContent = ""
                    selectedCategory = null
                },
                onDismiss = { addNoteModal = false }
            )

            EditNoteModal(
                showModal = editNote != null,
                note = editNote,
                errorMessage = errorMessage,
                onNoteTitleChange = { newTitle ->
                    editNote = editNote?.copy(title = newTitle)
                },
                onNoteContentChange = { newContent ->
                    editNote = editNote?.copy(title = newContent)
                },
                onConfirm = {
                    editNote?.let { note ->
                        viewModel.updateNote(note)
                    }
                    editNote = null
                },
                onDismiss = { editNote = null }

            )

            noteToDelete?.let { note ->
                ConfirmDialog(
                    title = "Cancella nota",
                    message = "Sei sicuro di voler eliminare questa nota?",
                    onConfirm = {
                        viewModel.deleteNote(note)
                        snackbarManager.showMessage("Nota '${note.id}' eliminata!")
                        noteToDelete = null
                    },
                    onDismiss = { noteToDelete = null }
                )
            }
        }
    }
}