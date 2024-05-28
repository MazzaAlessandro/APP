package com.example.app.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.additionalUI.BadgeBanner
import com.example.app.additionalUI.BadgeCard
import com.example.app.additionalUI.BadgeColor
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.models.BadgeDataModel
import com.example.app.models.UserSkillSubsModel
import com.example.app.util.SharedViewModel
import com.example.app.util.WindowInfo
import com.example.app.util.rememberWindowInfo
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BadgesScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    val currentContext: Context = LocalContext.current

    val windowInfo = rememberWindowInfo()

    val selected = remember { mutableStateOf(false) }

    var selectedBadge : MutableState<BadgeDataModel> = remember {
        mutableStateOf(
            BadgeDataModel(
                BadgeColor.BRONZE,
                "",
                ""
            )
        )
    }

    val badgeList:MutableState<List<BadgeDataModel>> = remember {
        mutableStateOf(listOf())
    }

    val badgeTitleEditText: MutableState<String> = remember {
        mutableStateOf("")
    }

    val currentUserSkillSub: MutableState<UserSkillSubsModel> = remember {
        mutableStateOf(UserSkillSubsModel())
    }

    if(sharedViewModel.getCurrentUserMail()!=""){
        LaunchedEffect(sharedViewModel.getCurrentUserMail()) {
            sharedViewModel.retrieveUserSkillSub(
                sharedViewModel.getCurrentUserMail(),
                currentContext,
            ){userSkillSub ->

                currentUserSkillSub.value = userSkillSub

                sharedViewModel.retrieveAllBadges(
                    userSkillSub.badgesObtained,
                    currentContext,
                ){badges ->
                    badgeList.value = badges
                }

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
        ){

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextField(
                    value = badgeTitleEditText.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onValueChange = { badgeTitleEditText.value = it },
                    label = { Text(text = "Search a skill by name") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search"
                        )
                    },
                )


                if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                    var list = badgeList.value
                        .filter { badgeTitleEditText.value in it.badgeName }
                        .sortedBy { it.date }
                    var listA : List<BadgeDataModel>
                    var listB : List<BadgeDataModel>

                    if(list.isNotEmpty()) {
                        listA = list.subList(0, list.size / 2)
                        listB = list.subList(list.size / 2, list.size)

                        Row(){
                            Column(
                                Modifier
                                    .width(windowInfo.screenWidth.div(2))
                                    .weight(1f)){
                                listB.map{
                                    BadgeBanner(
                                        it.badgeColor,
                                        it.badgeName,
                                        it.description,
                                        it.date,
                                        it.done,
                                        onClick = {
                                            selectedBadge.value = it
                                            selected.value = true
                                        })
                                }
                            }

                            Column(
                                Modifier
                                    .width(windowInfo.screenWidth.div(2))
                                    .weight(1f)){
                                listA.map{
                                    BadgeBanner(
                                        it.badgeColor,
                                        it.badgeName,
                                        it.description,
                                        it.date,
                                        it.done,
                                        onClick = {
                                            selectedBadge.value = it
                                            selected.value = true
                                        })
                                }
                            }
                        }
                    }
                }
                else{
                    badgeList.value
                        .filter { badgeTitleEditText.value in it.badgeName }
                        .sortedBy { it.date }
                        .map {
                            BadgeBanner(
                                it.badgeColor,
                                it.badgeName,
                                it.description,
                                it.date,
                                it.done,
                                onClick = {
                                    selectedBadge.value = it
                                    selected.value = true
                                })
                        }
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
        }
    }

    if (selected.value){
        //TODO FIND THE GOOD INDEXING
        val indexOfBadge = currentUserSkillSub.value.badgesObtained.indexOf(selectedBadge.value.skillId + selectedBadge.value.sectionId);

        val time = if(indexOfBadge == -1) ZonedDateTime.parse(selectedBadge.value.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME) else ZonedDateTime.now()


        BadgeCard(
            selectedBadge.value,
            sharedViewModel,
            time,
            onCloseClick = {
                selected.value = false
            })
    }
}
