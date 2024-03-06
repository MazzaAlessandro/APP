package com.example.app.bottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.app.Routes

sealed class BottomNavItem(var title:String, var selectedIcon:ImageVector, var unselectedIcon:ImageVector, var screen_route:String){

    object Search : BottomNavItem("Search Skills", Icons.Filled.Search, Icons.Outlined.Search, Routes.Search.route)
    object Profile : BottomNavItem("Profile", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle, Routes.Profile.route)
    object Create : BottomNavItem("Create Skills", Icons.Filled.Edit, Icons.Outlined.Edit, Routes.Create.route)
    object MySkills : BottomNavItem("My Skills", Icons.Filled.Star, Icons.Outlined.Star, Routes.MySkills.route)
}