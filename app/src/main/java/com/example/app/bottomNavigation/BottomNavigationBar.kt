package com.example.app.bottomNavigation

import androidx.compose.foundation.border
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app.ui.theme.PrimaryColor


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

    NavigationBar(modifier = Modifier.testTag("BottomNavigationBar"),
        containerColor = Color.LightGray.copy(alpha = 0.2f)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach{item->
            NavigationBarItem(

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryColor, // Color when selected
                    //unselectedIconColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium), // Color when not selected
                    selectedTextColor = PrimaryColor, // Text color when selected
                    //unselectedTextColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium) // Text color when not selected
                    indicatorColor = Color.White

                ),

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
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                },
            )
        }
    }
}