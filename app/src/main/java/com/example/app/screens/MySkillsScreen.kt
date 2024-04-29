package com.example.app.screens

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.additionalUI.BadgeColor
import com.example.app.additionalUI.BadgeIcon
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillCompleteStructureModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserDataModel
import com.example.app.util.SharedViewModel
import com.example.app.util.relative


//TODO MAKE THE USER CONNECTED TO THE SKILL
//TODO MAKE IT POSSIBLE FOR A USER TO SEARCH FOR THE SKILL HE CREATED
//TODO MAKE IT NON AUTOMATIC TO START A SKILL


@Composable
fun SkillTitleBlock(skillCompleteStructureModel: SkillCompleteStructureModel){
    val colorCircle = MaterialTheme.colorScheme.primary;
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0XFFF0F0F0),
                RoundedCornerShape(10)
            ) // Use the color of the background in your image
            .padding(horizontal = 10.dp, vertical = 10.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        BadgeIcon(badge = BadgeColor.BRONZE, size = relative(65.dp))
        Spacer(Modifier.width(10.dp)) // Space between the circle and the text
        Column(modifier = Modifier.weight(1f)) {
            Text(skillCompleteStructureModel.skillSection.titleSection, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(skillCompleteStructureModel.skillSection.descriptionSection, fontSize = 16.sp)
        }
        val completedAmount: Int = skillCompleteStructureModel.skillTasks.values.map { pair -> pair.first }.sum()
        val requiredAmount: Int = skillCompleteStructureModel.skillTasks.values.map { pair -> pair.second }.sum()
        Text(text = completedAmount.toString() + "/" + requiredAmount, fontSize = 20.sp)
    }
}

@Composable
fun SkillListElement(skillCompleteStructureModel: SkillCompleteStructureModel, index:Int,  onClickTask: (Int, SkillTaskModel) -> Unit){

    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10))
        .background(color = Color(0XFFD9D9D9))
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)){

        Text(
            skillCompleteStructureModel.skill.titleSkill,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center)

        SkillTitleBlock(skillCompleteStructureModel)

        Column(modifier = Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = Color.Black,
                    thickness = 1.dp
                )

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Tasks",
                    fontSize = 18.sp
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = Color.Black,
                    thickness = 1.dp
                )
            }

            skillCompleteStructureModel.skillTasks.forEach{
                TaskListElement(progression = it.value.first, task = it.key, {onClickTask(index, it.key)})
            }
        }

    }
}

@Composable
fun TaskListElement(progression: Int, task: SkillTaskModel, onClickTask: () -> Unit){

    CustomProgressIndicator(
        description = task.taskDescription,
        amount = progression,
        required = task.requiredAmount,
        height = 40.dp,
        onClickTask
    )



}

@Composable
fun SkillListBlock(listSkills: List<SkillCompleteStructureModel>, onClickTask: (Int, SkillTaskModel) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp, 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listSkills.forEachIndexed{index, skillStructure ->
            SkillListElement(skillCompleteStructureModel = skillStructure, index, onClickTask)
        }
    }
}

@Composable
fun CustomProgressIndicator(description: String, amount: Int, required: Int, height: Dp, onClickTask: () -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)
        .height(height)
        .clickable { onClickTask() },
        contentAlignment = Alignment.Center
    ){
        Box(modifier = Modifier
            .fillMaxSize()
        ){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(15))
                .border(1.dp, Color.Black, RoundedCornerShape(15)))
            Box(modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFFA3FF88), RoundedCornerShape(15))
                .border(1.dp, Color.Black, RoundedCornerShape(15))
                .fillMaxWidth(amount.toFloat() / required.toFloat()))
        }

        Row (modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically){
            Text(text = description,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.0f))

            Text(text = amount.toString() + "/" + required.toString(),
                modifier = Modifier.padding(horizontal = 20.dp))

        }

    }
}

fun ComputeListTaskAdd1(context: Context, index: Int, task: SkillTaskModel, listCompleteStructures: MutableList<SkillCompleteStructureModel>) : MutableList<SkillCompleteStructureModel>{
    var updatedList = listCompleteStructures
    var updatedStructure = updatedList.get(index)

    val basedNumber = updatedStructure.skillTasks[task]!!.first
    var updatedMap = updatedStructure.skillTasks.toMutableMap()
    //Toast.makeText(context, minOf(basedNumber + 1, task.requiredAmount).toString(), Toast.LENGTH_SHORT).show()
    updatedMap.set(task, Pair(minOf(basedNumber + 1, task.requiredAmount), task.requiredAmount))

    var updatedProgression = updatedStructure.skillProgression
    var updatedProgMap = updatedProgression.mapNonCompletedTasks.toMutableMap()

    if(basedNumber + 1 >= task.requiredAmount){
        updatedProgMap.remove(task.id)
    }else{
        updatedProgMap.set(task.id, minOf(basedNumber + 1, task.requiredAmount))
    }


    updatedProgression = updatedProgression.copy(mapNonCompletedTasks = updatedProgMap)
    updatedStructure = updatedStructure.copy(skillProgression = updatedProgression, skillTasks = updatedMap)

    updatedList.set(index, updatedStructure)

    return updatedList
}


fun ComputeSkipSection(index: Int, listCompleteStructures: MutableList<SkillCompleteStructureModel>) : Boolean{

    var updatedList = listCompleteStructures
    var updatedStructure = updatedList.get(index)

    val indexOfSection = updatedStructure.skill.skillSectionsList.indexOf(updatedStructure.skillProgression.currentSectionId)

    val mustSkipSection: Boolean = updatedStructure.skillTasks.all {
        it.value.first == it.value.second
    }

    return mustSkipSection
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MySkillsScreen(navController: NavHostController,
                  sharedViewModel: SharedViewModel,
                   openDialog: MutableState<Boolean>,
                   pendingRoute: MutableState<String?>

){

    var currentUser : MutableState<UserDataModel> = remember{mutableStateOf(UserDataModel())};
    currentUser.value = UserDataModel("aaaa", "ee", "ee", "ee", emptyList(),)

    var listCompleteStructures:MutableState<List<SkillCompleteStructureModel>> = remember {
        mutableStateOf(listOf())
    }

    val currentContext: Context = LocalContext.current;

    var isStartRun: MutableState<Boolean> = remember {
        mutableStateOf(true)
    }

    var currentStructureIndex : MutableState<Int> = remember {
        mutableStateOf(0)
    }

    var triggerSectionSkip : MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(currentUser) {
        sharedViewModel.retrieveUserSkillProgressionList(
            sharedViewModel.getCurrentUserMail(),
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

    LaunchedEffect(triggerSectionSkip.value) {

        if(isStartRun.value){
            return@LaunchedEffect
        }


        var updatedList = listCompleteStructures.value.toMutableList()
        var updatedStructure = updatedList.get(currentStructureIndex.value)


        var updatedProgression = updatedStructure.skillProgression

        val indexOfSection = updatedStructure.skill.skillSectionsList.indexOf(updatedProgression.currentSectionId)

        if(indexOfSection == updatedStructure.skill.skillSectionsList.size-1){
            sharedViewModel.saveSkillProgression(listCompleteStructures.value.get(currentStructureIndex.value).skillProgression.copy(isFinished = true), currentContext)



            sharedViewModel.retrieveUserSkillSub(sharedViewModel.getCurrentUserMail(), currentContext){


                val skillSection = listCompleteStructures.value.get(currentStructureIndex.value).skillSection

                var updatedBadges = it.badgesObtained

                if(skillSection.hasBadge && !(skillSection.badgeID in updatedBadges)){
                    updatedBadges = updatedBadges + skillSection.badgeID
                }

                sharedViewModel.saveUserSub(it.copy(badgesObtained = updatedBadges), context = currentContext)
            }

            return@LaunchedEffect
        }

        //FIRST WE ADD THE BADGE
        sharedViewModel.retrieveUserSkillSub(sharedViewModel.getCurrentUserMail(), currentContext){


            val skillSection = listCompleteStructures.value.get(currentStructureIndex.value).skillSection

            var updatedBadges = it.badgesObtained

            if(skillSection.hasBadge && !(skillSection.badgeID in updatedBadges)){
                updatedBadges = updatedBadges + skillSection.badgeID
            }

            sharedViewModel.saveUserSub(it.copy(badgesObtained = updatedBadges), context = currentContext)
        }

        //SECOND WE UPDATE THE STRUCT
        updatedProgression.currentSectionId = updatedStructure.skill.skillSectionsList.get(indexOfSection + 1)

        sharedViewModel.retrieveSkillSection(
            updatedProgression.skillId,
            updatedProgression.currentSectionId,
            currentContext,
        ) { data ->
            updatedStructure.skillSection = data

            sharedViewModel.retrieveAllSkillTasks(
                updatedStructure.skill.id,
                updatedStructure.skillSection.id,
                updatedStructure.skillSection.skillTasksList,
                currentContext
            ) { listTasks ->

                updatedStructure = updatedStructure.copy(skillTasks = listTasks.associate {
                    Pair(it, Pair(0, it.requiredAmount))
                })

                updatedProgression = updatedProgression.copy(mapNonCompletedTasks = listTasks.associate {
                    Pair(it.id, 0)
                })

                sharedViewModel.saveSkillProgression(updatedProgression, currentContext)

                updatedStructure = updatedStructure.copy(skillProgression = updatedProgression)
                updatedList.set(currentStructureIndex.value, updatedStructure)
                listCompleteStructures.value = updatedList
            }
        }


    }

    Scaffold(
        topBar = { AppToolBar(title = "My Skills", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController, openDialog, pendingRoute)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SkillListBlock(listSkills = listCompleteStructures.value
            ) { index, task ->

                val updatedList = ComputeListTaskAdd1(currentContext, index, task, listCompleteStructures.value.toMutableList())

                val mustSkipSection = ComputeSkipSection(index, updatedList)
                if(mustSkipSection){
                    currentStructureIndex.value = index

                    isStartRun.value = false
                    triggerSectionSkip.value = !triggerSectionSkip.value
                }else{
                    sharedViewModel.saveSkillProgression(updatedList.get(index).skillProgression, currentContext)
                }

                listCompleteStructures.value = updatedList
            }
        }
    }

}


