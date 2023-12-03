package com.example.app.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.Routes

@Composable
fun ScreenMain(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Login.route) {

        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Routes.SignUp.route) {
            SignUpScreen(navController = navController)
        }

        composable(Routes.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(Routes.Search.route) {
            SearchScreen(navController = navController)
        }

        composable(Routes.Create.route) {
            CreateScreen(navController = navController)
        }
    }
}