package com.projects.finio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.projects.finio.ui.screens.CategoriesScreen
import com.projects.finio.ui.screens.HomeScreen
import com.projects.finio.ui.screens.ItemsScreen
import com.projects.finio.ui.screens.SettingsScreen
import com.projects.finio.ui.screens.StatsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("categories") { CategoriesScreen(navController) }
        composable("items") { ItemsScreen(navController) }
        composable("stats") { StatsScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}