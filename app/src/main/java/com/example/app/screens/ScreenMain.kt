package com.example.app.screens

import android.content.pm.ActivityInfo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.Routes
import com.example.app.ui.theme.PrimaryColor
import com.example.app.util.LockScreenOrientation
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository

@Composable
fun ScreenMain(){
    val navController = rememberNavController()
    val sharedViewModel = remember{SharedViewModel(UserRepository(), SkillRepository())}

    var openDialog = remember { mutableStateOf(false) }
    var pendingRoute = remember { mutableStateOf<String?>("Profile") }

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    NavHost(navController = navController, startDestination = "Login") {

        composable(Routes.Login.route,
            enterTransition = {fadeIn(tween(350), )},
            ) {
            LoginScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.SignUp.route,
            enterTransition = {fadeIn(tween(350), )},
            ) {
            SignUpScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.Profile.route,
            enterTransition = {fadeIn(tween(350), )},
        ) {
            ProfileScreen(navController = navController, sharedViewModel = sharedViewModel, openDialog, pendingRoute)
        }

        composable(Routes.Search.route,
            enterTransition = {fadeIn(tween(350), )},
        ) {
            SearchScreen(navController = navController, sharedViewModel = sharedViewModel, openDialog, pendingRoute)
        }

        composable(Routes.Create.route,
            enterTransition = {fadeIn(tween(350), )},
        ) {
            CreateScreen(navController = navController, sharedViewModel = sharedViewModel, openDialog = openDialog, pendingRoute = pendingRoute)
        }

        composable(Routes.Update.route,
            enterTransition = {fadeIn(tween(350), )},
        ){
            ModifyAccountScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(Routes.MySkills.route,
            enterTransition = {fadeIn(tween(350), )},
        ){
            MySkillsScreen(navController = navController, sharedViewModel = sharedViewModel, openDialog, pendingRoute)
        }

        composable(Routes.Badges.route,
            enterTransition = {fadeIn(tween(350), )},
        ){
            BadgesScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.Stats.route,
            enterTransition = {fadeIn(tween(350), )},
        ){
            StatsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Routes.History.route,
            enterTransition = {fadeIn(tween(350), )},
        ){
            HistoryScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
    }



    if (openDialog.value) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            onDismissRequest = { openDialog.value = false },
            title = { Text("Confirm Navigation") },
            text = { Text("Are you sure you want to navigate away from this page?") },
            confirmButton = {
                Button(
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    ),
                    onClick = {
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
                Button(
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    ),
                    onClick = { openDialog.value = false }) {
                    Text("No")
                }
            }
        )
     }


}

@Composable
fun ConfirmDialog(
    openDialog: MutableState<Boolean>,
    pendingRoute: MutableState<String?>,
    navController: NavController
) {
    if (openDialog.value) {

    }
}
