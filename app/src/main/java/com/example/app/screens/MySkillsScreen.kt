package com.example.app.screens

import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.SkillCompleteStructureModel
import com.example.app.models.UserDataModel
import com.example.app.util.SharedViewModel

@Composable
fun SkillListElement(sharedViewModel: SharedViewModel, skillCompleteStructureModel: SkillCompleteStructureModel){

    Box(modifier = Modifier
        .clip(RoundedCornerShape(8))
        .background(color = Color.Red)
        .padding(20.dp)){
        Row {
            Column {
                Row (horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                    Text(text = skillCompleteStructureModel.skill.titleSkill)
                    val completedAmount: Int = skillCompleteStructureModel.skillTasks.values.map { pair -> pair.first }.sum()
                    val requiredAmount: Int = skillCompleteStructureModel.skillTasks.values.map { pair -> pair.second }.sum()

                    Text(text = completedAmount.toString() + "/" + requiredAmount)
                }
                Row {

                }
            }
        }
    }
}

@Composable
fun SkillListBlock(sharedViewModel: SharedViewModel){

    var skill1:SkillModel = SkillModel("aaaaaa", "First Skill", mutableListOf("0"))
    var skill1Section:SkillSectionModel = SkillSectionModel("0", "aaaaaa", "my step title eat", "fjefjn", mutableListOf("0", "1"))
    var skill1Task0:SkillTaskModel = SkillTaskModel("0", "0", "aaaaaa", "touch this twice", 2)
    var skill1Task1:SkillTaskModel = SkillTaskModel("1", "0", "aaaaaa", "touch this once", 1)

    var skill2:SkillModel = SkillModel("bbbbbb", "Second Skill", mutableListOf("0"))
    var skill2Section:SkillSectionModel = SkillSectionModel("0", "bbbbbb", "my step title eat", "fjefjn", mutableListOf("0", "1"))
    var skill2Task0:SkillTaskModel = SkillTaskModel("0", "0", "bbbbbb", "touch this four times", 4)
    var skill2Task1:SkillTaskModel = SkillTaskModel("1", "0", "bbbbbb", "touch this once", 1)

    var skillProgressionA0 = SkillProgressionModel("aaaa", "aaaaaa", "0", mutableMapOf(
        Pair<String, Int>("0", 1),
        Pair<String, Int>("1", 0)))

    var skillProgressionA1 = SkillProgressionModel("aaaa", "bbbbbb", "0", mutableMapOf(
        Pair<String, Int>("0", 3),
        Pair<String, Int>("1", 1)))


    var currentUser : MutableState<UserDataModel> = remember{mutableStateOf(UserDataModel())};
    currentUser.value = UserDataModel("aaaa", "ee", "ee", "ee", emptyList())

    var listCompleteStructures:MutableState<List<SkillCompleteStructureModel>> = remember {
        mutableStateOf(listOf())
    }

    val currentContext: Context = LocalContext.current;


    //SAVE EVERYTHING
    /*
    sharedViewModel.saveSkill(skill1, LocalContext.current)
    sharedViewModel.saveSkillSection(skill1Section, LocalContext.current)
    sharedViewModel.saveSkillTask(skill1Task0, LocalContext.current)
    sharedViewModel.saveSkillTask(skill1Task1, LocalContext.current)

    sharedViewModel.saveSkill(skill2, LocalContext.current)
    sharedViewModel.saveSkillSection(skill2Section, LocalContext.current)
    sharedViewModel.saveSkillTask(skill2Task0, LocalContext.current)
    sharedViewModel.saveSkillTask(skill2Task1, LocalContext.current)*/

    /*
    sharedViewModel.saveSkillProgression(skillProgressionA0, LocalContext.current)
    sharedViewModel.saveSkillProgression(skillProgressionA1, LocalContext.current)*/


    var listOfSkillProgressions: MutableState<List<SkillProgressionModel>> = remember {
        mutableStateOf(listOf(SkillProgressionModel()))
    };


    LaunchedEffect(currentUser){
        sharedViewModel.retrieveUserSkillProgressionList("aaaa", context = currentContext){skillProgList ->

            skillProgList.forEach{skillProg ->
                var skill: SkillModel;
                var skillSection: SkillSectionModel;
                var structure: SkillCompleteStructureModel;

                sharedViewModel.retrieveSkill(skillProg.skillId, currentContext, ){data ->
                    skill = data
                    sharedViewModel.retrieveSkillSection(skillProg.skillId, skillProg.currentSectionId, currentContext, ){data ->
                        skillSection = data

                        structure = SkillCompleteStructureModel(skillProg, skill, skillSection, mapOf())

                        sharedViewModel.retrieveAllSkillTasks(skill.id, skillSection.id, skillSection.skillTasksList, currentContext){listTasks ->
                            structure = SkillCompleteStructureModel(
                                skillProg, skill, skillSection, listTasks.associate { task ->
                                    if(skillProg.mapNonCompletedTasks.containsKey(task.id)){
                                        Pair(task, Pair(skillProg.mapNonCompletedTasks.getOrDefault(task.id, 0), task.requiredAmount))
                                    }else{
                                        Pair(task, Pair(task.requiredAmount, task.requiredAmount))
                                    }
                                }
                            )

                            listCompleteStructures.value += structure
                        }
                    }
                }
            }
        }
    }


    /*
    sharedViewModel.retrieveSkillProgression("aaaa", "aaaaaa", LocalContext.current){
        listOfSkillProgressions.value += it
    }

    for(skillProg: SkillProgressionModel in listOfSkillProgressions.value){
        //Text(text = skillProg.skillId + skillProg.userId + skillProg.currentSectionId)

        val skill : MutableState<SkillModel> = remember{mutableStateOf(SkillModel())};
        sharedViewModel.retrieveSkill(skillProg.skillId, LocalContext.current, ){data ->
            skill.value = data
        }


        val skillSection : MutableState<SkillSectionModel> = remember{mutableStateOf(SkillSectionModel())};
        sharedViewModel.retrieveSkillSection(skillProg.skillId, skillProg.currentSectionId, LocalContext.current, ){data ->
            skillSection.value = data
        }

        //SkillListElement(sharedViewModel = sharedViewModel, skillProgression = skillProg, skill = skill.value)
    }
    */

    SkillContainerFunction(sharedViewModel = sharedViewModel, list = listCompleteStructures.value)

    /*
    for (i in listCompleteStructures.value){
        Text(text = i.skill.id + " " + i.skillProgression.userId + " " + i.skillProgression.currentSectionId)
    }*/
    Text(text = listCompleteStructures.value.count().toString())



/*
    SkillListElement(sharedViewModel, skillProgression = SkillProgressionModel("aaaa", "aaaaaa", "0", mutableMapOf(
        Pair<String, Int>("0", 1),
        Pair<String, Int>("1", 0))))*/

}

@Composable
fun SkillContainerFunction(sharedViewModel: SharedViewModel, list: List<SkillCompleteStructureModel>){
    list.forEach{
            structure -> SkillListElement(sharedViewModel = sharedViewModel, structure)

    }
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


