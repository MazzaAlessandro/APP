package com.example.app.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.additionalUI.BadgeBanner
import com.example.app.additionalUI.BadgeCard
import com.example.app.additionalUI.BadgeColor
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.models.BadgeDataModel
import com.example.app.util.SharedViewModel

@Composable
fun BadgesScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    val currentContext: Context = LocalContext.current


    val selected = remember { mutableStateOf(false) }

    var selectedBadge : BadgeDataModel = BadgeDataModel(
        BadgeColor.BRONZE,
        "",
        ""
    )

    val badgeList:MutableState<List<BadgeDataModel>> = remember {
        mutableStateOf(listOf())
    }



    LaunchedEffect(sharedViewModel.getCurrentUserMail()) {
        sharedViewModel.retrieveUserSkillSub(
            sharedViewModel.getCurrentUserMail(),
            currentContext,
        ){userSkillSub ->

            sharedViewModel.retrieveAllBadges(
                userSkillSub.badgesObtained,
                currentContext,
            ){badges ->
                badgeList.value = badges
            }

        }
    }


    Scaffold (
        topBar = { AppToolBar(title = "Total Badges", navController, sharedViewModel, true, Routes.Profile.route) }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ){

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //BadgeCard(badge = BadgeColor.BRONZE, skillName = "Skill 1", description = "This is a description for the card. Let's make it longer to see how it fits", date = "24/10/12", done = false)
                badgeList.value.map {
                    BadgeBanner(
                        it.badgeColor,
                        it.badgeName,
                        it.description,
                        it.date,
                        it.done,
                        onClick = {
                            selectedBadge = it
                            selected.value = true
                        })
                }

                if(badgeList.value.isEmpty()){
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "You have not obtained a badge yet",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )

                    Button(
                        onClick = {
                            navController.navigate(Routes.MySkills.route)
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text("My Skills", fontSize = 20.sp)
                    }
                }
            }


            if (selected.value){
                BadgeCard(
                    selectedBadge,
                    onCloseClick = {
                        selected.value = false
                    })
            }
        }
    }
}
