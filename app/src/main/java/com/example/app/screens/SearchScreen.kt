package com.example.app.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillCompleteStructureModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserSkillSubsModel
import com.example.app.ui.theme.greenColor
import com.example.app.ui.theme.redColor
import com.example.app.ui.theme.yellowColor
import com.example.app.util.SharedViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max


//TODO PROBLEMS WITH PROG SAVE

//TODO DEFINE A SEARCH OBJECT UI
//TODO PUT A BUTTON TO ENROLL TO THAT SKILL
//TODO THINK ABOUT THE ACTIONS THAT SHOULD BE DONE WHEN CLICKING ON A SKILL

//TODO CHANGE THE ON ADD PROGRESSION SINCE THINGS ARE ALREADY COMPUTED

//TODO SAVE FROM CREATE AND DELETE ON SAVE SYSTEM PROPERTIES


//TODO CALLS VIEWMODEL

enum class SelectedSkillState {
    NOT_SELECTED, NEW_SELECTED, STARTED_SELECTED, REGISTERED_SELECTED
}

const val EXPANSION_ANIMATION_DURATION = 300

@Composable
fun SectionElementBlock(
    section: SkillSectionModel,
    index: Int,
    isResetable: Boolean,
    amount: Int,
    required: Int,
    onChangeSection:(SkillSectionModel) -> Unit
) {

    val initialColor: Color = Color(180, 180, 255)
    val endingColor: Color = Color(200, 255, 200)



    val colorNum = amount.toFloat() / (required)

    var colorRed = initialColor.red + (endingColor.red - initialColor.red) * colorNum
    var colorGreen = initialColor.green + (endingColor.green - initialColor.green) * colorNum
    var colorBlue: Float = initialColor.blue + (endingColor.blue - initialColor.blue) * colorNum

    val color = Color(colorRed, colorGreen , colorBlue)

    val isDoneText = if(amount == required) "Done" else amount.toString() + "/" + required.toString()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 2.dp)
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(10))
                    .border(1.dp, Color.Black, RoundedCornerShape(10))
                    .then(
                        if (isResetable) {
                            Modifier.clickable {
                                onChangeSection(section)
                            }
                        } else {
                            Modifier
                        }
                    )
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Section " + index.toString() + ": " + section.titleSection,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.0f)
            )

            Text(
                text = isDoneText,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }

}


@Composable
fun SkillSearchBlock(
    skill: SkillModel,
    selectedSkillState: SelectedSkillState,
    onClick: () -> Unit
) {
    val colorCircle = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 2.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            .background(Color.Gray.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { onClick() },

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp) // Set the size of the circle
                    .background(
                        colorCircle,
                        shape = CircleShape
                    ) // Use the color of the circle in your image
            )
            Spacer(Modifier.width(25.dp))
            // Space between the circle and the text
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(skill.titleSkill, fontWeight = FontWeight.Bold, fontSize = 25.sp)

                    val sectionAmount = skill.skillSectionsList.size

                    Text(
                        text = sectionAmount.toString() + " section" + if (sectionAmount > 1) "s" else "",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }

                if (selectedSkillState == SelectedSkillState.STARTED_SELECTED) {
                    Text(
                        text = "In Progress",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .background(greenColor, shape = RoundedCornerShape(40.dp))
                            .padding(2.dp)
                            .weight(1f)
                    )
                } else if (selectedSkillState == SelectedSkillState.NEW_SELECTED) {
                    Text(
                        text = "Not Started",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .background(redColor, shape = RoundedCornerShape(40.dp))
                            .padding(2.dp)
                            .weight(1f)
                    )
                } else {
                    Text(
                        text = "Registered",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .background(yellowColor, shape = RoundedCornerShape(40.dp))
                            .padding(2.dp)
                            .weight(1f),
                    )
                }
            }
            /*Column(modifier = Modifier.weight(1f)) {
                Text(skill.titleSkill, fontSize = 25.sp)

                if (selectedSkillState == SelectedSkillState.STARTED_SELECTED) {
                    Text(
                        text = "Already in progress", fontSize = 12.sp, color = Color.White,
                        modifier = Modifier
                            .background(Color.Blue, shape = RoundedCornerShape(40.dp))
                            .padding(horizontal = 20.dp, vertical = 2.dp)
                    )
                } else if (selectedSkillState == SelectedSkillState.NEW_SELECTED) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Not started", fontSize = 12.sp, textAlign = TextAlign.Center,
                            modifier = Modifier
                        )

                        val sectionAmount = skill.skillSectionsList.size

                        Text(
                            text = sectionAmount.toString() + " section" + if (sectionAmount > 1) "s" else "",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1.0f)
                        )
                    }
                } else {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Registered",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            modifier = Modifier
                                .background(Color.Green, shape = RoundedCornerShape(40.dp))
                                .padding(2.dp)
                                .weight(1.0f)
                        )

                        val sectionAmount = skill.skillSectionsList.size

                        Text(
                            text = sectionAmount.toString() + " section" + if (sectionAmount > 1) "s" else "",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1.0f)
                        )
                    }
                }
            }*/
        }
    }
}


@Composable
fun SkillInfoPopUp_STARTED(
    sharedViewModel: SharedViewModel,
    skill: SkillModel,
    skillProgression: SkillProgressionModel,
    onSubTask: (SkillTaskModel, SkillCompleteStructureModel) -> Unit,
    onResetSkillProgression: () -> Unit,
    onCloseClick: () -> Unit,
    onChangeSection: (SkillSectionModel) -> Unit,
) {
    val currentContext = LocalContext.current

    val sectionsList: MutableState<List<SkillSectionModel>> = remember {
        mutableStateOf(emptyList())
    }

    var tasksMap: MutableState<Map<String, List<SkillTaskModel>>> = remember {
        mutableStateOf(emptyMap())
    }

    var completeStructure: MutableState<SkillCompleteStructureModel> = remember {
        mutableStateOf(
            SkillCompleteStructureModel(
                SkillProgressionModel(), SkillModel(), SkillSectionModel(), emptyMap()
            )
        )
    }

    LaunchedEffect(skill) {
        sharedViewModel.retrieveAllSkillSection(skill.id, currentContext) { sectionModels ->
            sectionsList.value = sectionModels.sortedWith { a, b ->
                if (skill.skillSectionsList.indexOf(a.id) < skill.skillSectionsList.indexOf(b.id)) -1 else 1
            }
            sectionModels.forEach { section ->
                sharedViewModel.retrieveAllSkillTasks(
                    skill.id,
                    section.id,
                    section.skillTasksList,
                    currentContext
                ) { taskModels ->
                    tasksMap.value += Pair(section.id, taskModels)

                    completeStructure.value = SkillCompleteStructureModel(skillProgression,
                        skill,
                        sectionsList.value.find { it.id == skillProgression.currentSectionId }
                            ?: sectionsList.value.get(0),
                        tasksMap.value.get(skillProgression.currentSectionId)?.associate {


                            var progressionNumer: Int =
                                if (it.id in skillProgression.mapNonCompletedTasks.keys) skillProgression.mapNonCompletedTasks.get(
                                    it.id
                                ) ?: 0 else it.requiredAmount

                            Pair(it, Pair(progressionNumer, it.requiredAmount))
                        } ?: emptyMap())
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.7f))
            .zIndex(10F)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Popup(
            alignment = Alignment.Center,

            properties = PopupProperties(
                excludeFromSystemGesture = true,
            ),

            // to dismiss on click outside
            onDismissRequest = { onCloseClick() }
        ) {
            Box(
                Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.75f)
                    .clip(shape = RoundedCornerShape(25.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surface)
                //.verticalScroll(rememberScrollState()),
            ) {
                IconButton(
                    onClick = {
                        onCloseClick()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .zIndex(11F),
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 0.dp, 0.dp, 15.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(100.dp)
                ) {


                    val colorCircle = MaterialTheme.colorScheme.primary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0XFFF0F0F0),
                                RoundedCornerShape(10)
                            ) // Use the color of the background in your image
                            .padding(horizontal = 20.dp, vertical = 17.dp),

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        /*Box(
                            modifier = Modifier
                                .size(50.dp) // Set the size of the circle
                                .background(colorCircle, shape = CircleShape) // Use the color of the circle in your image
                        )
                        Spacer(Modifier.width(25.dp))*/ // Space between the circle and the text
                        Column(modifier = Modifier.weight(2f)) {
                            Text(skill.titleSkill, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                            Text(text = completeStructure.value.skillSection.titleSection, fontSize = 15.sp)
                        }
                        Text(
                            skill.skillSectionsList.count().toString() + " section" + if (skill.skillSectionsList.count() > 1) "s" else "",
                            color = Color.White,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .background(color = colorCircle, RoundedCornerShape(20))
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .weight(1f)
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxWidth(),
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
                                text = "Skill Description",
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp, 0.dp)
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
                        ) {
                            Text(
                                text = skill.skillDescription,
                                modifier = Modifier.padding(15.dp),
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }

                    Column(modifier = Modifier
                        .fillMaxWidth(),) {
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
                                text = "Sections",
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
                        //SECTION ELEMENT

                        sectionsList.value.forEachIndexed { index, section ->
                            val indexOfCurrent = skill.skillSectionsList.indexOf(section.id)
                            val indexOfProg =
                                skill.skillSectionsList.indexOf(completeStructure.value.skillSection.id)

                            if (indexOfCurrent < indexOfProg) {

                                val amount = completeStructure.value.skillTasks.map { it.value.second }.sum()

                                SectionElementBlock(section, index , true,  1, 1)
                                {sectionArg ->

                                    var updatedCompleteStructureModel = completeStructure.value


                                    var newSection = sectionArg
                                    var newTasks = tasksMap.value.get(newSection.id)!!.associateWith { Pair(0, it.requiredAmount) }

                                    updatedCompleteStructureModel = updatedCompleteStructureModel.copy(skillSection = newSection, skillTasks = newTasks)

                                    completeStructure.value = updatedCompleteStructureModel

                                    onChangeSection(sectionArg)

                                }
                            } else if (indexOfCurrent == indexOfProg) {

                                val amount = completeStructure.value.skillTasks.map { entry ->
                                    entry.value.first
                                }.sum()

                                val total = completeStructure.value.skillTasks.map { entry ->
                                    entry.value.second
                                }.sum()

                                val required = if(total == 0) 1 else total

                                SectionElementBlock(section, index,  true, amount, required)
                                {sectionArg ->
                                    var updatedCompleteStructureModel = completeStructure.value


                                    var newSection = sectionArg
                                    var newTasks = tasksMap.value.get(newSection.id)!!.associateWith { Pair(0, it.requiredAmount) }

                                    updatedCompleteStructureModel = updatedCompleteStructureModel.copy(skillSection = newSection, skillTasks = newTasks)

                                    completeStructure.value = updatedCompleteStructureModel

                                    onChangeSection(sectionArg)

                                }


                            } else {

                                val total = completeStructure.value.skillTasks.map { entry ->
                                    entry.value.second
                                }.sum()

                                val required = if(total == 0) 1 else total

                                SectionElementBlock(section, index, false, 0, 2, onChangeSection)
                            }

                            // THE TASKS

                            Column(modifier = Modifier
                                .fillMaxWidth(),) {
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

                                tasksMap.value.get(section.id)?.forEach {task->

                                    val am: Int
                                    val req: Int

                                    if (indexOfCurrent < indexOfProg) {
                                        am = task.requiredAmount
                                        req = task.requiredAmount
                                    } else {
                                        am = completeStructure.value.skillTasks.get(task)?.first ?: 0
                                        req = task.requiredAmount
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp)
                                                .background(Color(255, 200, 200), RoundedCornerShape(15.dp))
                                                .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                                                .size(40.dp)
                                                ,
                                            onClick = {
                                            if(indexOfCurrent == indexOfProg){
                                                var updatedCompleteStructureModel = completeStructure.value

                                                val baseValue = max((updatedCompleteStructureModel.skillTasks.get(task)!!.first) - 1, 0)
                                                var newTasks = updatedCompleteStructureModel.skillTasks.toMutableMap()

                                                newTasks.put(task, Pair(baseValue, task.requiredAmount))

                                                var updatedProgression = ComputeListTaskSub1(currentContext, task, updatedCompleteStructureModel.skillProgression)

                                                updatedCompleteStructureModel = updatedCompleteStructureModel.copy(skillTasks = newTasks, skillProgression = updatedProgression)

                                                completeStructure.value = updatedCompleteStructureModel

                                                onSubTask(task, completeStructure.value)
                                            }
                                            })
                                        {
                                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                                        }


                                        Box(modifier = Modifier.weight(1.0f)){
                                            CustomProgressIndicator(task.taskDescription, am, req, 40.dp, false, false, false){}
                                        }
                                    }


                                }

                                Spacer(modifier = Modifier.height(50.dp))

                            }

                        }
                        /*
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp, 0.dp)
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
                        ) {
                            Text(
                                text = completeStructure.value.skillSection.descriptionSection,
                                modifier = Modifier.padding(15.dp),
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }*/



                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .weight(1.0f)
                                .padding(horizontal = 30.dp),
                            contentAlignment = Alignment.TopStart
                        ){
                            Text(
                                "In Progress",
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .background(color = greenColor, RoundedCornerShape(20))
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            )
                        }

                        Box(
                            Modifier
                                .weight(1.0f)
                                .padding(horizontal = 30.dp),
                            contentAlignment = Alignment.TopEnd
                        ){

                            Button(onClick = {
                                var updatedCompleteStructureModel = completeStructure.value


                                var newSection = sectionsList.value.get(0)
                                var newTasks = tasksMap.value.get(newSection.id)!!.associateWith { Pair(0, it.requiredAmount) }

                                updatedCompleteStructureModel = updatedCompleteStructureModel.copy(skillSection = newSection, skillTasks = newTasks)

                                completeStructure.value = updatedCompleteStructureModel

                                onResetSkillProgression()

                            }) {
                                Text(
                                    "Restart",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(vertical = 4.dp, horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SkillInfoPopUp_UNSTARTED(
    skill: SkillModel,
    sharedViewModel: SharedViewModel,
    isRegistered: Boolean,
    onAddProgression: () -> Unit,
    onCloseClick: () -> Unit,
    onRegisterSkill: () -> Unit
) {
    val currentContext = LocalContext.current

    var sectionModel: MutableState<SkillSectionModel> = remember {
        mutableStateOf(SkillSectionModel())
    }

    var sectionsList: MutableState<List<SkillSectionModel>> = remember {
        mutableStateOf(emptyList())
    }
    var tasksMap: MutableState<Map<String, List<SkillTaskModel>>> = remember {
        mutableStateOf(emptyMap())
    }

    LaunchedEffect(skill) {
        sharedViewModel.retrieveAllSkillSection(skill.id, currentContext) { sectionModels ->
            sectionsList.value = sectionModels
            sectionModels.forEach { section ->
                sharedViewModel.retrieveAllSkillTasks(
                    skill.id,
                    section.id,
                    section.skillTasksList,
                    currentContext
                ) { taskModels ->
                    tasksMap.value += Pair(section.id, taskModels)
                }
                if (section.id == "0"){
                    sectionModel.value = section
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.7f))
            .zIndex(10F)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Popup(
            alignment = Alignment.Center,

            properties = PopupProperties(
                excludeFromSystemGesture = true,
            ),

            // to dismiss on click outside
            onDismissRequest = { onCloseClick() }
        ) {
            Box(
                Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.75f)
                    .clip(shape = RoundedCornerShape(25.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    //.verticalScroll(rememberScrollState()),
            ) {
                IconButton(
                    onClick = {
                        onCloseClick()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .zIndex(11F),
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 0.dp, 0.dp, 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {


                    val colorCircle = MaterialTheme.colorScheme.primary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0XFFF0F0F0),
                                RoundedCornerShape(10)
                            ) // Use the color of the background in your image
                            .padding(horizontal = 20.dp, vertical = 17.dp),

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        /*Box(
                            modifier = Modifier
                                .size(50.dp) // Set the size of the circle
                                .background(colorCircle, shape = CircleShape) // Use the color of the circle in your image
                        )
                        Spacer(Modifier.width(25.dp))*/ // Space between the circle and the text
                        Column(modifier = Modifier.weight(2f)) {
                            Text(skill.titleSkill, fontSize = 30.sp)
                            Text(text = sectionModel.value.titleSection, fontSize = 15.sp)
                        }
                        Text(
                            skill.skillSectionsList.count().toString() + " section" + if (skill.skillSectionsList.count() > 1) "s" else "",
                            color = Color.White,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .background(color = colorCircle, RoundedCornerShape(20))
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .weight(1f)
                        )
                    }
                    /*Box(
                        modifier = Modifier
                            .size(windowWidth / 5) // Set the size of the circle
                            .background(colorCircle, shape = CircleShape)
                    )*/

                    Column(modifier = Modifier
                        .fillMaxWidth(),) {
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
                                text = "Skill Description",
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp, 0.dp)
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
                        ) {
                            Text(
                                text = skill.skillDescription,
                                modifier = Modifier.padding(15.dp),
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }

                    /*Column(modifier = Modifier
                        .fillMaxWidth(),) {
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
                                text = "Section 1",
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp, 0.dp)
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
                        ) {
                            Text(
                                text = sectionModel.value.descriptionSection,
                                modifier = Modifier.padding(15.dp),
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }*/

                    Column(modifier = Modifier
                        .fillMaxWidth(),) {
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
                                text = "Section" + if (skill.skillSectionsList.count() > 1) "s" else "",
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

                        sectionsList.value.forEachIndexed { index, section ->
                            SectionElementBlock(
                                section,
                                index,
                                false,
                                amount = 0,
                                required = 1,
                            ){

                            }
                        }
                    }

                    if (!isRegistered) {
                        Button(
                            //modifier = Modifier.align(Alignment.BottomCenter),
                            onClick = { onRegisterSkill() },
                            colors = ButtonDefaults.buttonColors(containerColor = yellowColor)
                        ) {
                            Text(text = "REGISTER SKILL")
                        }
                    } else {
                        Button(
                            //modifier = Modifier.align(Alignment.BottomCenter),
                            onClick = { onAddProgression() },
                            colors = ButtonDefaults.buttonColors(containerColor = greenColor)
                        ) {
                            Text(text = "START SKILL")
                        }
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    openDialog: MutableState<Boolean>,
    pendingRoute: MutableState<String?>
) {

    val currentContext = LocalContext.current


    /*
        sharedViewModel.retrieveAllUserSkill(
            sharedViewModel.getCurrentUserMail(),
            currentContext,
        ){skill ->
            sharedViewModel.retrieveUserSkillSub(
                sharedViewModel.getCurrentUserMail(),
                currentContext
            ){
                sharedViewModel.updateUserSub(
                    it.copy(createdSkillsId = skill.map { it.id }),
                    currentContext
                )
            }
        }

        */

    var skillTitleEditText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val skillModelsStarted: MutableState<List<SkillModel>> = remember {
        mutableStateOf(mutableListOf())
    }

    val skillModelsRegistered: MutableState<List<SkillModel>> = remember {
        mutableStateOf(mutableListOf())
    }

    val skillModelsCreated: MutableState<List<SkillModel>> = remember {
        mutableStateOf(mutableListOf())
    }

    val skillProgressions: MutableState<List<SkillProgressionModel>> = remember {
        mutableStateOf(mutableListOf())
    }

    val isSkillSelected: MutableState<SelectedSkillState> = remember {
        mutableStateOf(SelectedSkillState.NOT_SELECTED)
    }

    val skillSelected: MutableState<SkillModel> = remember {
        mutableStateOf(SkillModel())
    }

    val currentUserSkillSubs: MutableState<UserSkillSubsModel> = remember {
        mutableStateOf(UserSkillSubsModel())
    }

    LaunchedEffect(currentUserSkillSubs.value) {
        sharedViewModel.retrieveUserSkillProgressionList(
            sharedViewModel.getCurrentUserMail(),
            currentContext,
        ) {
            skillProgressions.value = it.sortedByDescending {
                ZonedDateTime.parse(it.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME) }

            sharedViewModel.retrieveSkillsFromList(
                currentContext,
                it.map { it.skillId }
            ) {
                skillModelsStarted.value = it.sortedBy { skillProgressions.value.map { it.skillId }.indexOf(it.id) }
            }

        }

        sharedViewModel.retrieveUserSkillSub(
            sharedViewModel.getCurrentUserMail(),
            currentContext,
        ) {
            currentUserSkillSubs.value = it



            sharedViewModel.retrieveSkillsFromList(
                currentContext,
                currentUserSkillSubs.value.registeredSkillsIDs
            ) {
                skillModelsRegistered.value = it.sortedByDescending { ZonedDateTime.parse(it.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME) }
            }

            sharedViewModel.retrieveSkillsFromList(
                currentContext,
                currentUserSkillSubs.value.createdSkillsId) {
                skillModelsCreated.value = it.sortedByDescending { ZonedDateTime.parse(it.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME) }
            }
        }
    }


    Scaffold(
        topBar = { AppToolBar(title = "Search a Skill", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController, openDialog, pendingRoute)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
        ) {


            item {
                TextField(
                    value = skillTitleEditText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onValueChange = { skillTitleEditText = it },
                    label = { Text(text = "Search a skill by name") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search"
                        )
                    },
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Started Skills", fontSize = 25.sp)

                    Text(
                        text = skillModelsStarted.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }
                            .count().toString() + " elements",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )

                    /*Icon(imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        Modifier.clickable {  })*/

                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.dp),
                    color = Color.Black
                )
            }


            items(skillModelsStarted.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }
                ) {
                SkillSearchBlock(it, SelectedSkillState.STARTED_SELECTED)
                {
                    skillSelected.value = it
                    isSkillSelected.value = SelectedSkillState.STARTED_SELECTED
                }
            }


            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Registered Skills", fontSize = 25.sp)

                    Text(
                        text = skillModelsRegistered.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }
                            .count().toString() + " elements",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )

                    /*Icon(imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        Modifier.clickable {  })*/
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.dp),
                    color = Color.Black
                )
            }

            items(skillModelsRegistered.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() && it.id !in skillModelsStarted.value.map { it.id }}
                ) {
                SkillSearchBlock(it, SelectedSkillState.REGISTERED_SELECTED)
                {
                    skillSelected.value = it
                    isSkillSelected.value = SelectedSkillState.REGISTERED_SELECTED
                }
            }


            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Created Skills", fontSize = 25.sp)

                    Text(
                        text = skillModelsCreated.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }
                            .count().toString() + " elements",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )

                    /*Icon(imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        Modifier.clickable {  })*/

                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.dp),
                    color = Color.Black
                )
            }

            items(skillModelsCreated.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }
                ) {
                SkillSearchBlock(
                    it,
                    if(it in skillModelsStarted.value) SelectedSkillState.STARTED_SELECTED else if (it in skillModelsRegistered.value) SelectedSkillState.REGISTERED_SELECTED else SelectedSkillState.NEW_SELECTED
                )
                {
                    skillSelected.value = it
                    isSkillSelected.value =
                        if(it in skillModelsStarted.value) SelectedSkillState.STARTED_SELECTED
                        else if (it in skillModelsRegistered.value) SelectedSkillState.REGISTERED_SELECTED
                        else SelectedSkillState.NEW_SELECTED
                }
            }



        }

        Spacer(modifier = Modifier.height(20.dp))

    }

    if (isSkillSelected.value == SelectedSkillState.NEW_SELECTED || isSkillSelected.value == SelectedSkillState.REGISTERED_SELECTED) {
        Box {
            SkillInfoPopUp_UNSTARTED(skillSelected.value, sharedViewModel,
                isSkillSelected.value == SelectedSkillState.REGISTERED_SELECTED,
                {
                    sharedViewModel.retrieveSkillSection(
                        skillSelected.value.id,
                        skillSelected.value.skillSectionsList.get(0),
                        currentContext,
                    ) { section ->
                        val mapNonCompletedTasks: Map<String, Int> =
                            section.skillTasksList.associateWith { 0 }

                        val skillProgression = SkillProgressionModel(
                            sharedViewModel.getCurrentUserMail(),
                            skillSelected.value.id,
                            skillSelected.value.skillSectionsList.get(0),
                            mapNonCompletedTasks,
                            dateTime = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        )
                        skillProgressions.value += skillProgression
                        skillProgressions.value = skillProgressions.value.sortedByDescending {
                            ZonedDateTime.parse(it.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME) }

                        sharedViewModel.saveSkillProgression(skillProgression, currentContext)

                        var startedSkillIds = skillProgressions.value.map {
                            it.skillId
                        }

                        var skillCustomOrdering = currentUserSkillSubs.value.customOrdering.toMutableList()
                        skillCustomOrdering.add(0, skillProgression.skillId)

                        currentUserSkillSubs.value = currentUserSkillSubs.value.copy(
                            startedSkillsIDs = startedSkillIds,
                            customOrdering = skillCustomOrdering,
                        )

                        sharedViewModel.updateUserSub(
                            currentUserSkillSubs.value, currentContext
                        )

                        isSkillSelected.value = SelectedSkillState.STARTED_SELECTED
                    }
                },

                {
                    isSkillSelected.value = SelectedSkillState.NOT_SELECTED
                },
                {
                    //COMEBACK
                    val regSkillIds = currentUserSkillSubs.value.registeredSkillsIDs

                    currentUserSkillSubs.value =
                        currentUserSkillSubs.value.copy(registeredSkillsIDs = regSkillIds + skillSelected.value.id)

                    sharedViewModel.updateUserSub(currentUserSkillSubs.value, currentContext)

                    skillModelsRegistered.value += skillSelected.value

                    isSkillSelected.value = SelectedSkillState.REGISTERED_SELECTED
                }
            )
        }
    } else if (isSkillSelected.value == SelectedSkillState.STARTED_SELECTED) {


        Box {

            SkillInfoPopUp_STARTED(
                sharedViewModel,
                skillSelected.value,
                skillProgressions.value.find { it.skillId == skillSelected.value.id }
                    ?: SkillProgressionModel(),
                {task, completeStructure ->

                    var updatedProgression = completeStructure.skillProgression

                    val index = skillProgressions.value.indexOf(skillProgressions.value.find { it.skillId == skillSelected.value.id })

                    var updatedList = skillProgressions.value.toMutableList()
                    updatedList.set(index, updatedProgression)

                    skillProgressions.value = updatedList.toList()

                    sharedViewModel.updateSkillProgression(sharedViewModel.getCurrentUserMail(), skillSelected.value.id, updatedProgression, currentContext)
                },
                {
                    var updatedProgression = skillProgressions.value.find { it.skillId == skillSelected.value.id } ?: SkillProgressionModel()
                    var newSection = skillSelected.value.skillSectionsList.get(0)

                    sharedViewModel.retrieveSkillSection(
                        skillSelected.value.id,
                        newSection,
                        currentContext,
                    ){
                        var newTasks = it.skillTasksList.associateWith { 0 }

                        updatedProgression = updatedProgression.copy(currentSectionId =  newSection, isFinished = false, mapNonCompletedTasks = newTasks)

                        val index = skillProgressions.value.indexOf(skillProgressions.value.find { it.skillId == skillSelected.value.id })

                        var updatedList = skillProgressions.value.toMutableList()
                        updatedList.set(index, updatedProgression)

                        skillProgressions.value = updatedList.toList()

                        sharedViewModel.updateSkillProgression(sharedViewModel.getCurrentUserMail(), skillSelected.value.id, updatedProgression, currentContext)
                    }
                },
                {
                    isSkillSelected.value = SelectedSkillState.NOT_SELECTED
                },
                {section ->

                    var updatedProgression = skillProgressions.value.find { it.skillId == section.idSkill } ?: SkillProgressionModel()
                    var newSection = section.id

                    sharedViewModel.retrieveSkillSection(
                        skillSelected.value.id,
                        newSection,
                        currentContext,
                    ){
                        var newTasks = it.skillTasksList.associateWith { 0 }

                        updatedProgression = updatedProgression.copy(currentSectionId =  newSection, isFinished = false, mapNonCompletedTasks = newTasks)

                        val index = skillProgressions.value.indexOf(skillProgressions.value.find { it.skillId == skillSelected.value.id })

                        var updatedList = skillProgressions.value.toMutableList()
                        updatedList.set(index, updatedProgression)

                        skillProgressions.value = updatedList.toList()

                        sharedViewModel.updateSkillProgression(sharedViewModel.getCurrentUserMail(), skillSelected.value.id, updatedProgression, currentContext)
                    }

                }
            )
        }
    }


    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}


