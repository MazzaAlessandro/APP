package com.example.app.bottomNavigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController){

    val items = listOf<BottomNavItem>(
        BottomNavItem.Search,
        BottomNavItem.MySkills,
        BottomNavItem.Profile,
        BottomNavItem.Create
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach{item->
            NavigationBarItem(
                selected = currentRoute == item.screen_route,
                onClick = {
                          navController.navigate(item.screen_route){
                              popUpTo(navController.graph.startDestinationId)
                              launchSingleTop = true
                          }
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.screen_route) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title)
                },
                label = {
                    Text(text = item.title)
                },)
        }
    }
}