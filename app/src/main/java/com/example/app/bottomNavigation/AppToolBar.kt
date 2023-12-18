package com.example.app.bottomNavigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.util.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolBar(
    title : String,
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    showBackButton : Boolean = false,
    backRoute : String = ""
){
    TopAppBar(
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal
                ),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            if(showBackButton){
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(5.dp, 0.dp)
                        .clickable {
                        navController.navigate(backRoute)
                    }
                )
            }
        },
        actions = {
            IconButton(onClick = {
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signOut()

                val authStateListener = AuthStateListener{
                    if(it.currentUser == null){
                        sharedViewModel.resetSession()
                        navController.navigate(Routes.Login.route)
                    }
                }

                firebaseAuth.addAuthStateListener(authStateListener)
            }) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Logout"
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
    )
}