package com.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.additionalUI.BadgeColor
import com.example.app.additionalUI.BadgeIcon
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.util.SharedViewModel

@Composable
fun BadgesScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    val badgeList = listOf(
        BadgeData(badge = BadgeColor.BRONZE, description = "This is a longer badge description that goes on multiple lines to see how it fits in the box", date = "24/10/12"),
        BadgeData(badge = BadgeColor.SILVER, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.GOLD, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.SILVER, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.GOLD, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
        BadgeData(badge = BadgeColor.BRONZE, description = "badge description", date = "24/10/12"),
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
            badgeList.map {
                BadgeBanner(it.badge, it.description, it.date)
            }
        }
    }
}


@Composable
fun BadgeBanner(
    badge : BadgeColor,
    description : String,
    date : String
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp, 2.dp)
        .clip(shape = RoundedCornerShape(10.dp))
        .background(Color.Gray.copy(alpha = 0.5f))){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically){
            BadgeIcon(badge, 60.dp)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp, 0.dp, 5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = description, lineHeight = 15.sp)
                
                Spacer(modifier = Modifier.fillMaxHeight())

                Text("Achieved on: $date", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class BadgeData(
    val badge: BadgeColor,
    val description : String,
    val date : String
)