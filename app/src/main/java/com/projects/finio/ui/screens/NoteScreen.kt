package com.projects.finio.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.projects.finio.data.local.entity.Category
import com.projects.finio.data.local.entity.Note
import com.projects.finio.ui.components.AppDrawer
import com.projects.finio.ui.components.CustomSnackbar
import com.projects.finio.ui.components.modals.ConfirmDialog
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

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    notes.forEach { note ->
                        Text(note.title)
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

            // noteToDelete.?let { note ->
            //     ConfirmDialog(
            //         title = "Cancella nota",
            //         message = "Sei sicuro di voler eliminare questa nota?",
            //         onConfirm = {
            //             viewModel.deleteNote(note)
            //             snackbarManager.showMessage("Nota '${note.id}' eliminata!")
            //             noteToDelete = null
            //         },
            //         onDismiss = { noteToDelete = null }
            //     )
            // }
        }
    }
}