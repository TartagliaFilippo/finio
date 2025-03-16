package com.projects.finio.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NavigationSlider(
    navController: NavController
) {
    val currentDestination = navController.currentBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight(),
        shape = RoundedCornerShape(
            topEnd = 16.dp,
            bottomEnd = 16.dp
        ),
        color = Color(0xFF44CF6C)
    ) {
        NavigationRail(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            containerColor = Color(0xFFA9FDAC)
        ) {
            NavigationRailItem(
                selected = currentDestination == "home",
                onClick = { navController.navigate("home") },
                icon = { Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) },
                label = { Text("Home") }
            )

            NavigationRailItem(
                selected = currentDestination == "categories",
                onClick = { navController.navigate("categories") },
                icon = { Icon(
                    Icons.Default.Menu,
                    contentDescription = "Categories",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) },
                label = { Text("Categories") }
            )

            NavigationRailItem(
                selected = currentDestination == "items",
                onClick = { navController.navigate("items") },
                icon = { Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Items",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) },
                label = { Text("Items") }
            )

            NavigationRailItem(
                selected = currentDestination == "stats",
                onClick = { navController.navigate("stats") },
                icon = { Icon(
                    Icons.Default.Info,
                    contentDescription = "Stats",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) },
                label = { Text("Stats") }
            )

            NavigationRailItem(
                selected = currentDestination == "settings",
                onClick = { navController.navigate("settings") },
                icon = { Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) },
                label = { Text("Settings") }
            )
        }
    }
}