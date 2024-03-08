package com.example.app.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserDataModel
import com.example.app.util.SharedViewModel

@Composable
fun SkillListElement(sharedViewModel: SharedViewModel, skillProgression: SkillProgressionModel, taskId: String){
    var skill : MutableState<SkillModel> = remember{mutableStateOf(SkillModel())};
    sharedViewModel.retrieveSkill(skillProgression.skillId, LocalContext.current, ){data ->
        skill.value = data
    }

    var skillSection : MutableState<SkillSectionModel> = remember{mutableStateOf(SkillSectionModel())};
    sharedViewModel.retrieveSkillSection(skillProgression.skillId, skillProgression.currentSectionId, LocalContext.current, ){data ->
        skillSection.value = data
    }

    var skillTask : MutableState<SkillTaskModel> = remember{mutableStateOf(SkillTaskModel())};
    sharedViewModel.retrieveSkillTask(skillProgression.skillId, skillProgression.currentSectionId, taskId, LocalContext.current, ){data ->
        skillTask.value = data
    }


    Box(modifier = Modifier
        .clip(RoundedCornerShape(8))
        .background(color = Color.Red)
        .padding(20.dp)){
        Row {
            Column {
                Row {
                    Text(text = skill.value.titleSkill)
                    Text(text = "" + skillProgression.mapNonCompletedTasks.get(taskId) + "/" + skillTask.value.requiredAmount )
                }
                Row {

                }
            }
        }
    }
}

@Composable
fun SkillListBlock(sharedViewModel: SharedViewModel){

    var skill:SkillModel = SkillModel("aaaaaa", "firstSkill", mutableListOf("0"))
    var skillSection:SkillSectionModel = SkillSectionModel("0", "aaaaaa", "my step title eat", "fjefjn", mutableListOf())
    var skillTask0:SkillTaskModel = SkillTaskModel("0", "0", "aaaaaa", 2)
    var skillTask1:SkillTaskModel = SkillTaskModel("1", "0", "aaaaaa", 1)

    /*
    //SAVE EVERYTHING
    sharedViewModel.saveSkill(skill, LocalContext.current)
    sharedViewModel.saveSkillSection(skillSection, LocalContext.current)
    sharedViewModel.saveSkillTask(skillTask0, LocalContext.current)
    sharedViewModel.saveSkillTask(skillTask1, LocalContext.current)
    */


    SkillListElement(sharedViewModel, skillProgression = SkillProgressionModel("aaaa", "aaaaaa", "0", mutableMapOf(
        Pair<String, Int>("0", 1),
        Pair<String, Int>("1", 0))),
        "0")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MySkillsScreen(navController: NavHostController,
                  sharedViewModel: SharedViewModel
){
    Scaffold(
        topBar = { AppToolBar(title = "My Skills", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            )
            {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8))
                            .background(color = Color.Green)
                            .padding(20.dp)
                    ) {
                        Text(text = "My Skills", fontSize = 32.sp)
                    }



                    SkillListBlock(sharedViewModel);
                }
            }
        }
    }

}


