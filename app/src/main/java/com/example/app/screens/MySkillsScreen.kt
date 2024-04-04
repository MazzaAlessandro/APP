package com.example.app.screens

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
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

//TODO ADD THE DATES IN THE SKILL PROGRESSIONS

@Composable
fun SkillListElement(skillCompleteStructureModel: SkillCompleteStructureModel){

    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10))
        .background(color = Color(0XFFD9D9D9))
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)){
        Box(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .background(color = Color(0XFFF0F0F0))
            .padding(5.dp),
            contentAlignment = Alignment.Center
            ){
            Text(text = skillCompleteStructureModel.skill.titleSkill,
                fontSize = 28.sp,
            )
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(25.dp)) {
            Box(modifier = Modifier
                .weight(2.0f),
                contentAlignment = Alignment.Center
            ){
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(40))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(vertical = 3.dp, horizontal = 20.dp),

                    text = skillCompleteStructureModel.skillSection.titleSection, color = Color.White, fontSize = 20.sp)
            }

            Box(modifier = Modifier
                .weight(1.0f)
                .clip(RoundedCornerShape(10))
                .padding(5.dp),
                contentAlignment = Alignment.Center
            ){
                val completedAmount: Int = skillCompleteStructureModel.skillTasks.values.map { pair -> pair.first }.sum()
                val requiredAmount: Int = skillCompleteStructureModel.skillTasks.values.map { pair -> pair.second }.sum()
                Text(text = completedAmount.toString() + "/" + requiredAmount, fontSize = 15.sp)
            }
        }
        skillCompleteStructureModel.skillTasks.forEach{
            TaskListElement(progression = it.value.first, task = it.key)
        }
    }

    /*
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
    }*/
}

@Composable
fun TaskListElement(progression: Int, task: SkillTaskModel){

    val containerColor = Color(0xFFF0F0F0)


    Box(modifier = Modifier
        .background(color = Color(0xFFD3E9FF), shape = RoundedCornerShape(10.dp))
        .padding(15.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = task.taskDescription,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(containerColor, RoundedCornerShape(10))
                    .padding(10.dp)
                    .weight(3.0f)
            )

            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(40))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(vertical = 3.dp, horizontal = 20.dp),

                text = progression.toString() + "/" + task.requiredAmount.toString(), color = Color.White, fontSize = 20.sp)


        }
    }

}

@Composable
fun SkillListBlock(listSkills: List<SkillCompleteStructureModel>){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        listSkills.forEach{skillStructure ->
            SkillListElement(skillCompleteStructureModel = skillStructure)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MySkillsScreen(navController: NavHostController,
                  sharedViewModel: SharedViewModel
){

    var currentUser : MutableState<UserDataModel> = remember{mutableStateOf(UserDataModel())};
    currentUser.value = UserDataModel("aaaa", "ee", "ee", "ee", emptyList())

    var listCompleteStructures:MutableState<List<SkillCompleteStructureModel>> = remember {
        mutableStateOf(listOf())
    }

    val currentContext: Context = LocalContext.current;


    LaunchedEffect(currentUser) {
        sharedViewModel.retrieveUserSkillProgressionList(
            "aaaa",
            context = currentContext
        ) { skillProgList ->

            skillProgList.forEach { skillProg ->
                var skill: SkillModel;
                var skillSection: SkillSectionModel;
                var structure: SkillCompleteStructureModel;

                sharedViewModel.retrieveSkill(skillProg.skillId, currentContext,) { data ->
                    skill = data
                    sharedViewModel.retrieveSkillSection(
                        skillProg.skillId,
                        skillProg.currentSectionId,
                        currentContext,
                    ) { data ->
                        skillSection = data

                        structure =
                            SkillCompleteStructureModel(skillProg, skill, skillSection, mapOf())

                        sharedViewModel.retrieveAllSkillTasks(
                            skill.id,
                            skillSection.id,
                            skillSection.skillTasksList,
                            currentContext
                        ) { listTasks ->
                            structure = SkillCompleteStructureModel(
                                skillProg, skill, skillSection, listTasks.associate { task ->
                                    if (skillProg.mapNonCompletedTasks.containsKey(task.id)) {
                                        Pair(
                                            task,
                                            Pair(
                                                skillProg.mapNonCompletedTasks.getOrDefault(
                                                    task.id,
                                                    0
                                                ), task.requiredAmount
                                            )
                                        )
                                    } else {
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

    Scaffold(
        topBar = { AppToolBar(title = "My Skills", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10))
                    .background(color = Color(0XFFD9D9D9))
                    .padding(20.dp)
            ) {
                Text(text = "My Skills", fontSize = 32.sp)
            }


            SkillListBlock(listSkills = listCompleteStructures.value)
        }
    }

}


