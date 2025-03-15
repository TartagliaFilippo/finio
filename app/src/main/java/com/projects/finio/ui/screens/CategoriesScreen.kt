package com.projects.finio.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.projects.finio.ui.components.HeaderBar
import com.projects.finio.ui.components.NavigationSlider

@Composable
fun CategoriesScreen(
    navController: NavController
) {
    var showModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ){
        HeaderBar(
            onClick = { showModal = !showModal }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { showModal = false },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Categories",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
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