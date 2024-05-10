package com.example.app.screens

import android.content.pm.ActivityInfo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.Routes
import com.example.app.util.LockScreenOrientation
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository

@Composable
fun ScreenMain(){
    val navController = rememberNavController()
    val sharedViewModel = SharedViewModel(UserRepository(), SkillRepository())

    val openDialog = remember { mutableStateOf(false) }
    val pendingRoute = remember { mutableStateOf<String?>(null) }

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    NavHost(navController = navController, startDestination = "Login") {

        composable(Routes.Login.route) {
            LoginScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.SignUp.route) {
            SignUpScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.Profile.route) {
            ProfileScreen(navController = navController, sharedViewModel = sharedViewModel, openDialog, pendingRoute)
        }

        composable(Routes.Search.route) {
            SearchScreen(navController = navController, sharedViewModel = sharedViewModel, openDialog, pendingRoute)
        }

        composable(Routes.Create.route) {
            CreateScreen(navController = navController, sharedViewModel = sharedViewModel, openDialog = openDialog, pendingRoute = pendingRoute)
        }

        composable(Routes.Update.route){
            ModifyAccountScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(Routes.MySkills.route){
            MySkillsScreen(navController = navController, sharedViewModel = sharedViewModel, openDialog, pendingRoute)
        }

        composable(Routes.Badges.route){
            BadgesScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.Stats.route){
            StatsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.History.route){
            StatsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
    }



    if (openDialog.value) {
        ConfirmDialog(openDialog, pendingRoute, navController)
    }

}

@Composable
fun ConfirmDialog(
    openDialog: MutableState<Boolean>,
    pendingRoute: MutableState<String?>,
    navController: NavController
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("Confirm Navigation") },
            text = { Text("Are you sure you want to navigate away from this page?") },
            confirmButton = {
                Button(onClick = {
                    openDialog.value = false
                    pendingRoute.value?.let {
                        navController.navigate(it) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                    pendingRoute.value = null
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }
}
