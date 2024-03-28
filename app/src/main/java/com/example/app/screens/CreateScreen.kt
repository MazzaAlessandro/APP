package com.example.app.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillModel
import com.example.app.models.SkillSectionModel
import com.example.app.util.SharedViewModel

@Composable
fun GeneralInfoBox(sharedViewModel: SharedViewModel, skill:SkillModel, onTitleChange: (String) -> Unit, onDescriptionChange: (String) -> Unit){

    Box(modifier = Modifier
        .background(Color.Cyan)
        .clip(RoundedCornerShape(8))
        .padding(10.dp)
    ){
        Column {
            Row (verticalAlignment = Alignment.CenterVertically)
            {

                Text(text = "Title:",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(2.dp))


                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8))
                        .background(Color(245, 245, 245))
                ){
                    TextField(value = skill.titleSkill,
                        onValueChange = onTitleChange,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }

            Text(text = "Description:",
                fontSize = 15.sp,
                modifier = Modifier.padding(2.dp))

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8))
                    .background(Color(245, 245, 245))
            ){
                TextField(value = skill.skillDescription,
                    onValueChange = onDescriptionChange,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun SectionBox(sharedViewModel: SharedViewModel, section:SkillSectionModel){
    Box(modifier = Modifier
        .background(Color.Cyan)
        .clip(RoundedCornerShape(8))
        .padding(10.dp)
    ){
        Column {
            Row (verticalAlignment = Alignment.CenterVertically)
            {

                Text(text = "Title:",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(2.dp))


                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8))
                        .background(Color(245, 245, 245))
                ){
                    Text(text = section.titleSection)
                }
            }
        }
    }
}












@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){

    var skill : MutableState<SkillModel> = rememberSaveable {
        mutableStateOf(SkillModel())
    }

    var skillSections: MutableState<List<SkillSectionModel>> = rememberSaveable {
        mutableStateOf(mutableListOf())
    }


    Scaffold(
        topBar = { AppToolBar(title = "Create a new Skill", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ){innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Create a skill", fontSize = 32.sp)

                //GeneralInfoBox(sharedViewModel, title = title, {title = it}, description, {description = it})
                GeneralInfoBox(sharedViewModel, skill.value, {skill.value = skill.value.copy(titleSkill = it)}, {skill.value = skill.value.copy(skillDescription = it)})

                for(section in skillSections.value){

                    SectionBox(sharedViewModel = sharedViewModel, section = section)
                    
                }

                Button(onClick = {
                    skillSections.value = skillSections.value + SkillSectionModel("a", "a", "SECTION TITLE LOL", "", mutableListOf()) }) {
                    Text(text = "ADD SECTION +")
                }
            }
        }
    }

    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}