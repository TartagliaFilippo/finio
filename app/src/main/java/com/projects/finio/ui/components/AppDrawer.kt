package com.projects.finio.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
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
            "Home" to Icons.Default.Home,
            "Notes" to Icons.Default.MailOutline,
            "Categories" to Icons.Default.Menu,
            "Items" to Icons.Default.AddCircle,
            "Schedule" to Icons.Default.Star,
            "Settings" to Icons.Default.Settings,
            "Stats" to Icons.Default.Info
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