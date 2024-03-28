package com.example.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.additionalUI.BadgeBanner
import com.example.app.additionalUI.BadgeColor
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.util.SharedViewModel

@Composable
fun BadgesScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    val badgeList = listOf(
        BadgeData(BadgeColor.BRONZE, "Skill 1",  "This is a longer badge description that goes on multiple lines to see how it fits in the box", "24/10/12"),
        BadgeData(BadgeColor.SILVER, "Skill 2", "badge description", "24/10/12"),
        BadgeData(BadgeColor.BRONZE, "Skill 3", "badge description", "24/10/12"),
        BadgeData(BadgeColor.GOLD, "Skill 1", "badge description"),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12", false),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.SILVER, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.GOLD, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12"),
        BadgeData(BadgeColor.BRONZE, "Skill 1", "badge description", "24/10/12"),
    )
    Scaffold (
        topBar = { AppToolBar(title = "Total Badges", navController, sharedViewModel, true, Routes.Profile.route) }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //BadgeCard(badge = BadgeColor.BRONZE, skillName = "Skill 1", description = "This is a description for the card. Let's make it longer to see how it fits", date = "24/10/12", done = false)
            badgeList.map {
                BadgeBanner(
                    it.badge,
                    it.skillName,
                    it.description,
                    it.date,
                    it.done)
            }
        }
    }
}

data class BadgeData(
    val badge: BadgeColor,
    val skillName : String,
    val description : String,
    val date : String = "",
    val done : Boolean = true
)