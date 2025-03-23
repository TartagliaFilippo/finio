package com.projects.finio.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.projects.finio.ui.components.AppDrawer
import com.projects.finio.ui.components.HeaderBar
import com.projects.finio.ui.components.NavigationSlider
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navController: NavController
) {
    var showModal by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppDrawer(
        drawerState = drawerState,
        onItemClick = { selectedItem ->
            scope.launch { drawerState.close() }
            println("navigaverso: $selectedItem")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderBar(
                onClick = { showModal = true }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showModal = false },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { scope.launch { drawerState.open() } }
                    ) {
                        Icon(imageVector = Icons.Default.MailOutline, contentDescription = "Apri drawer")
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
}