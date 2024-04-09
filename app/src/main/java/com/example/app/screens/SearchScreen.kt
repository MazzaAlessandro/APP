package com.example.app.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillCompleteStructureModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.UserDataModel
import com.example.app.util.SharedViewModel

//TODO FIRST IT SHOULD LOAD THE LIST OF SKILLS AND SKILL PROGRESSIONS
//THE SKILLS ARE USED TO GET ITS SKILLS
//THE PROGRESSIONS ARE USED TO KNOW IF THE SKILLS ARE STARTED OR NOT
//TODO DEFINE A SEARCH OBJECT UI
//TODO PUT A BUTTON TO ENROLL TO THAT SKILL
//TODO THINK ABOUT THE ACTIONS THAT SHOULD BE DONE WHEN CLICKING ON A SKILL

@Composable
fun SkillBlock(skill: SkillModel){
    val colorCircle = MaterialTheme.colorScheme.primary;
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0XFFF0F0F0),
                RoundedCornerShape(10)
            ) // Use the color of the background in your image
            .padding(horizontal = 20.dp, vertical = 15.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp) // Set the size of the circle
                .background(colorCircle, shape = CircleShape) // Use the color of the circle in your image
        )
        Spacer(Modifier.width(25.dp)) // Space between the circle and the text
        Column(modifier = Modifier.weight(1f)) {
            Text(skill.titleSkill, fontSize = 25.sp)

        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController,
                 sharedViewModel: SharedViewModel,
                 openDialog: MutableState<Boolean>,
                 pendingRoute: MutableState<String?>
){

    val currentContext = LocalContext.current

    var skillTitleEditText by remember {mutableStateOf("")}
    var active by remember {mutableStateOf(false)}

    var skillModels: List<SkillModel> = remember {
        mutableListOf()
    }

    var skillProgressions: List<SkillProgressionModel> = remember {
        mutableListOf()
    }

    var items = remember {
        mutableStateListOf(
            "Android development",
            "Soup"
        )
    }


    LaunchedEffect(Unit) {
        sharedViewModel.retrieveUserSkillProgressionList(
            sharedViewModel.getCurrentUserMail(),
            currentContext,
        ){
            skillProgressions = it
        }

        sharedViewModel.retrieveAllUserSkill(
            sharedViewModel.getCurrentUserMail(),
            currentContext
        ){
            skillModels = it
        }
    }


    Scaffold(
        topBar = { AppToolBar(title = "Search a Skill", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController, openDialog, pendingRoute)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = skillTitleEditText,
                onQueryChange = {
                                skillTitleEditText = it
                },
                onSearch = {
                    //items.add(text)
                    active = false
                },
                active = active,
                onActiveChange = {
                    active = it
                },
                placeholder = {
                    Text(text = "Search")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                trailingIcon = {
                    if(active){
                        Icon(
                            modifier = Modifier.clickable {
                                if(skillTitleEditText.isNotEmpty())
                                    skillTitleEditText = ""
                                else
                                    active = false
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon"
                        )
                    }
                }) {
                skillModels.forEach{
                    SkillBlock(it)
                }
            }
        }

    }

    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}


