package com.projects.finio.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    onItemClick: (String) -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(onItemClick)
        }
    ) {
        content()
    }
}

@Composable
fun DrawerContent(onItemClick: (String) -> Unit) {
    ModalDrawerSheet {
        Text(
            text = "Menu",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        val items = listOf(
            "home" to Icons.Default.Home,
            "categories" to Icons.Default.Menu,
            "items" to Icons.Default.AddCircle,
            "settings" to Icons.Default.Settings
        )

        items.forEach { (title, icon) ->
            NavigationDrawerItem(
                label = { Text(title) },
                icon = { Icon(imageVector = icon, contentDescription = title) },
                selected = false,
                onClick = { onItemClick(title) }
            )
        }
    }
}