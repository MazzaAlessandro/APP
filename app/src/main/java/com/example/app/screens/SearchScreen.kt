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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
    amount: Int,
    required: Int,
    sharedViewModel: SharedViewModel
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 0.dp)
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(10))
                    .border(1.dp, Color.Black, RoundedCornerShape(10))
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color(0xFFD3E9FF), RoundedCornerShape(10))
                    .border(1.dp, Color.Black, RoundedCornerShape(10))
                    .fillMaxWidth(amount.toFloat() / required.toFloat())
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = section.titleSection,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.0f)
            )

            Text(
                text = amount.toString() + "/" + required.toString(),
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
    val colorCircle = MaterialTheme.colorScheme.primary;

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

                    Text(skill.titleSkill, fontSize = 25.sp)

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
    onCloseClick: () -> Unit
) {
    val currentContext = LocalContext.current

    var sectionsList: MutableState<List<SkillSectionModel>> = remember {
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
                        sectionsList.value?.find { it.id == skillProgression.currentSectionId }
                            ?: sectionsList.value.get(0),
                        tasksMap.value.get(skillProgression.currentSectionId)?.associate {


                            var progressionNumer: Int =
                                if (it.id in skillProgression.mapNonCompletedTasks.keys) skillProgression.mapNonCompletedTasks?.get(
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
            .background(Color.Gray.copy(alpha = 0.5f))
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


                    val colorCircle = MaterialTheme.colorScheme.primary;
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

                            var sectionId = completeStructure.value.skillSection.id + 1
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "Section $sectionId",
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
                                text = completeStructure.value.skillSection.descriptionSection,
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
                                text = "Section tasks",
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

                        sectionsList.value.forEach { section ->
                            val indexOfCurrent = skill.skillSectionsList.indexOf(section.id)
                            val indexOfProg =
                                skill.skillSectionsList.indexOf(skillProgression.currentSectionId)

                            if (indexOfCurrent < indexOfProg) {
                                SectionElementBlock(section, 1, 1, sharedViewModel)
                            } else if (indexOfCurrent == indexOfProg) {


                                SectionElementBlock(section, 1, 2, sharedViewModel)

                            } else {
                                SectionElementBlock(
                                    section = section,
                                    amount = 0,
                                    required = 1,
                                    sharedViewModel = sharedViewModel
                                )
                            }
                        }
                    }

                    Text(
                        "In Progress",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(color = greenColor, RoundedCornerShape(20))
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                    )

                    /*Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
                    ) {
                        Text(
                            text = skill.skillDescription,
                            modifier = Modifier.padding(15.dp),
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }*/
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
            .background(Color.Gray.copy(alpha = 0.5f))
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


                    val colorCircle = MaterialTheme.colorScheme.primary;
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
                                text = "Section tasks",
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

                        sectionsList.value.forEach { section ->
                            SectionElementBlock(
                                section = section,
                                amount = 0,
                                required = 1,
                                sharedViewModel = sharedViewModel
                            )
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
            skillProgressions.value = it
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
                skillModelsRegistered.value = it
            }

            sharedViewModel.retrieveSkillsFromList(
                currentContext,
                currentUserSkillSubs.value.startedSkillsIDs
            ) {
                skillModelsStarted.value = it
            }

            sharedViewModel.retrieveSkillsFromList(
                currentContext,
                currentUserSkillSubs.value.createdSkillsId.filter { !(it in currentUserSkillSubs.value.startedSkillsIDs) }) {
                skillModelsCreated.value = it
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


            items(skillModelsStarted.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }) {
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

            items(skillModelsRegistered.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }) {
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
                        .padding(10.dp, 0.dp),
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

            items(skillModelsCreated.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }) {
                SkillSearchBlock(
                    it,
                    if (it in skillModelsRegistered.value) SelectedSkillState.REGISTERED_SELECTED else SelectedSkillState.NEW_SELECTED
                )
                {
                    skillSelected.value = it
                    isSkillSelected.value =
                        if (it in skillModelsRegistered.value) SelectedSkillState.REGISTERED_SELECTED
                        else SelectedSkillState.NEW_SELECTED
                }
            }


            /*
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
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
                })*/
        }

        Spacer(modifier = Modifier.height(20.dp))

        /*
        Column {
            skillModelsRegistered.forEach{
                SkillSearchBlock(skill = it, isInProgress = false) {
                    
                }
            }
        }*/




        if (isSkillSelected.value == SelectedSkillState.NEW_SELECTED || isSkillSelected.value == SelectedSkillState.REGISTERED_SELECTED) {
            Box() {
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
                                mapNonCompletedTasks
                            )
                            skillProgressions.value += skillProgression

                            sharedViewModel.saveSkillProgression(skillProgression, currentContext)

                            var startedSkillIds = skillProgressions.value.map {
                                it.skillId
                            }
                            sharedViewModel.updateUserSub(
                                currentUserSkillSubs.value.copy(
                                    startedSkillsIDs = startedSkillIds
                                ), currentContext
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
            Box() {

                SkillInfoPopUp_STARTED(
                    sharedViewModel,
                    skillSelected.value,
                    skillProgressions.value?.find { it.skillId == skillSelected.value.id }
                        ?: SkillProgressionModel(),
                    {
                        isSkillSelected.value = SelectedSkillState.NOT_SELECTED
                    })
            }
        }

    }




    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}


