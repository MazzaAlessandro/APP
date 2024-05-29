package com.example.app.bottomNavigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController,
                        openDialog: MutableState<Boolean>,
                        pendingRoute: MutableState<String?>
){

    val items = listOf<BottomNavItem>(
        BottomNavItem.Search,
        BottomNavItem.MySkills,
        BottomNavItem.Profile,
        BottomNavItem.Create
    )

    NavigationBar(modifier = Modifier.testTag("BottomNavigationBar")) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach{item->
            NavigationBarItem(
                modifier = Modifier.testTag(item.title),
                selected = currentRoute == item.screen_route,
                onClick = {
                    if (currentRoute != item.screen_route) {
                        if (currentRoute == "Create") { // Only show dialog if current screen is "CREATE"

                            pendingRoute.value = item.screen_route
                            openDialog.value = true
                        } else {
                            // Navigate without confirmation for other screens
                            navController.navigate(item.screen_route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }

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