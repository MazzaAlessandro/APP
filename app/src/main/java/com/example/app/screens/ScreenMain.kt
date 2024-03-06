package com.example.app.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.Routes
import com.example.app.util.SharedViewModel

@Composable
fun ScreenMain(){
    val navController = rememberNavController()
    val sharedViewModel = SharedViewModel()

    NavHost(navController = navController, startDestination = Routes.Login.route) {

        composable(Routes.Login.route) {
            LoginScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.SignUp.route) {
            SignUpScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.Profile.route) {
            ProfileScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.Search.route) {
            SearchScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.Create.route) {
            CreateScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.Update.route){
            ModifyAccountScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}