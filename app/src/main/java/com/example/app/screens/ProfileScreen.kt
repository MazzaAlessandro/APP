package com.example.app.screens

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.app.Routes

@Composable
fun ProfileScreen(navController: NavHostController){
    Text(
        text = "Profile Screen",
    )

    BackHandler {
        navController.navigate(Routes.Login.route)
    }
}