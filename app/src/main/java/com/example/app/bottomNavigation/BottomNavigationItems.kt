package com.example.app.bottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.app.Routes

sealed class BottomNavItem(var title:String, var selectedIcon:ImageVector, var unselectedIcon:ImageVector, var screen_route:String){

    object Search : BottomNavItem("Search", Icons.Filled.Search, Icons.Outlined.Search, Routes.Search.route)
    object Profile : BottomNavItem("Profile", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle, Routes.Profile.route)
    object Create : BottomNavItem("Create", Icons.Filled.Edit, Icons.Outlined.Edit, Routes.Create.route)
    object MySkills : BottomNavItem("MySkills", Icons.Filled.List, Icons.Outlined.List, Routes.MySkills.route)
}