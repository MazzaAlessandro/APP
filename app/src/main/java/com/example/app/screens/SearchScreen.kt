package com.example.app.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.app.ui.theme.PrimaryColor
import com.example.app.ui.theme.greenColor
import com.example.app.ui.theme.redColor
import com.example.app.ui.theme.yellowColor
import com.example.app.util.SharedViewModel
import com.example.app.util.WindowInfo
import com.example.app.util.rememberWindowInfo
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max


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

    val currentColor: Color = PrimaryColor
    val otherColor: Color = Color.LightGray.copy(alpha = 0.2f)

    val color = if(amount != 0) currentColor else otherColor
    val textColor = if(amount != 0) Color.White else Color.DarkGray
    val borderColor = if(amount != 0) PrimaryColor else Color.Gray

    val isDoneText = if(amount == required) "Done" else if(amount != 0 && required != -1) "In Progress" else "Not Started"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 0.dp, start = 2.dp, end = 2.dp)
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
                    .border(1.dp, borderColor, RoundedCornerShape(10))
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
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 15.dp).weight(2.0f)
            )

            Text(
                text = isDoneText,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 15.dp).weight(1.0f)
            )
        }
    }

}

@Composable
fun SkillSearchBlockUnavailable(
    skill: SkillModel,
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 2.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
            .background(Color(133, 133, 133, 255))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { onClick() },

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

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

                    Text(
                        skill.titleSkill,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 25.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val sectionAmount = skill.skillSectionsList.size

                    Text(
                        text = "Creator: " + skill.creatorUserName,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1.0f),
                        textAlign = TextAlign.End
                    )

                }

                Text(
                    text = "This skill is not publicly available anymore. Click to remove it from the list.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(2.5f)
                )
            }
        }
    }


}

@Composable
fun SkillSearchBlock(
    skill: SkillModel,
    selectedSkillState: SelectedSkillState,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 4.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
            .testTag("SkillSearchBlock " + skill.titleSkill)
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
                    .size(40.dp) // Set the size of the circle
            ){
                Icon(imageVector = Icons.Filled.RadioButtonChecked, "SkillLogo",  modifier = Modifier.fillMaxSize(), tint = PrimaryColor)
            }
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

                    Text(skill.titleSkill,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontSize = 25.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)

                    val sectionAmount = skill.skillSectionsList.size

                    Row(horizontalArrangement = Arrangement.Start){
                        Text(
                            text = sectionAmount.toString() + " section" + if (sectionAmount > 1) "s" else "",
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Creator: " + skill.creatorUserName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1.0f),
                            textAlign = TextAlign.End
                        )
                    }

                }

                if (selectedSkillState == SelectedSkillState.STARTED_SELECTED) {
                    /*Text(
                        text = "In Progress",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .background(greenColor, shape = RoundedCornerShape(10.dp))
                            .padding(2.dp)
                            .weight(1f)
                    )*/

                    Text(
                        text = "In Progress",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = greenColor,
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .border(1.dp, greenColor, RoundedCornerShape(10.dp))
                            .padding(5.dp)
                            .weight(1f)
                    )
                } else if (selectedSkillState == SelectedSkillState.NEW_SELECTED) {
                    /*Text(
                        text = "Not Started",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .background(redColor, shape = RoundedCornerShape(10.dp))
                            .padding(2.dp)
                            .weight(1f)
                    )*/

                    Text(
                        text = "Not Started",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = redColor,
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .border(1.dp, redColor, RoundedCornerShape(10.dp))
                            .padding(5.dp)
                            .weight(1f)
                    )
                } else {
                    /*Text(
                        text = "Registered",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .background(yellowColor, shape = RoundedCornerShape(10.dp))
                            .padding(2.dp)
                            .weight(1f),
                    )*/

                    Text(
                        text = "Registered",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = yellowColor,
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .border(1.dp, yellowColor, RoundedCornerShape(10.dp))
                            .padding(5.dp)
                            .weight(1f)
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
    onStopSkillProgression: (SkillProgressionModel)->Unit,
    onUnpublishSkill: (SkillModel) -> Unit,
    onPublishSkill: (SkillModel) -> Unit,
    onDeleteSkill: (SkillModel) -> Unit
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

    if(skill.id!=""){
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
                    .clip(shape = RoundedCornerShape(10.dp))
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                //.verticalScroll(rememberScrollState()),
            ) {
                IconButton(
                    onClick = {
                        onCloseClick()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .zIndex(11F)
                        .padding(top = 10.dp, end = 10.dp),
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.spacedBy(100.dp)
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.LightGray.copy(alpha = 0.2f),
                                RoundedCornerShape(10.dp)
                            ) // Use the color of the background in your image
                            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                            .padding(horizontal = 20.dp, vertical = 17.dp),

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        /*Box(
                            modifier = Modifier
                                .size(50.dp) // Set the size of the circle
                                .background(PrimaryColor, shape = CircleShape) // Use the color of the circle in your image
                        )
                        Spacer(Modifier.width(25.dp))*/ // Space between the circle and the text
                        Column(modifier = Modifier.weight(2f)) {
                            Text(skill.titleSkill, fontWeight = FontWeight.Bold, fontSize = 30.sp,
                                color = Color.DarkGray,
                            )
                            Text(text = "Creator: " + skill.creatorUserName, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                                color = Color.DarkGray,
                            )
                        }
                        Text(
                            skill.skillSectionsList.count().toString() + " section" + if (skill.skillSectionsList.count() > 1) "s" else "",
                            color = Color.White,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .background(color = PrimaryColor, RoundedCornerShape(20))
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .weight(1f)
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxWidth(),
                        ) {

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(text = "Skill Description", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                                color = Color.DarkGray,
                            )

                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 0.dp, end = 0.dp),
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.LightGray.copy(alpha = 0.2f),
                                    RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                        ) {
                            Text(
                                text = skill.skillDescription,
                                modifier = Modifier.padding(15.dp),
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }
                    }


                    Column(modifier = Modifier
                        .fillMaxWidth(),) {

                        Spacer(modifier = Modifier.height(40.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(text = "Sections", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                                color = Color.DarkGray,
                            )

                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(15.dp))

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
                                        .padding(5.dp, 0.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        color = Color.DarkGray,
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
                                        color = Color.DarkGray,
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

                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp, 0.dp)
                                        .height(IntrinsicSize.Max),
                                        verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .padding(vertical = 5.dp)
                                                .background(
                                                    PrimaryColor,
                                                    RoundedCornerShape(
                                                        topStart = 10.dp,
                                                        bottomStart = 10.dp
                                                    )
                                                )
                                                .border(
                                                    1.dp,
                                                    PrimaryColor,
                                                    RoundedCornerShape(
                                                        topStart = 10.dp,
                                                        bottomStart = 10.dp
                                                    )
                                                ),
                                            onClick = {
                                            if(indexOfCurrent == indexOfProg){
                                                var updatedCompleteStructureModel = completeStructure.value

                                                val baseValue = max((updatedCompleteStructureModel.skillTasks.get(task)!!.first) - 1, 0)
                                                var newTasks = updatedCompleteStructureModel.skillTasks.toMutableMap()

                                                newTasks.put(task, Pair(baseValue, task.requiredAmount))

                                                var updatedProgression = sharedViewModel.ComputeListTaskSub1(currentContext, task, updatedCompleteStructureModel.skillProgression)

                                                updatedCompleteStructureModel = updatedCompleteStructureModel.copy(skillTasks = newTasks, skillProgression = updatedProgression)

                                                completeStructure.value = updatedCompleteStructureModel

                                                onSubTask(task, completeStructure.value)
                                            }
                                            })
                                        {
                                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "", tint = Color.White)
                                        }


                                        Box(modifier = Modifier.weight(1.0f)){
                                            CustomProgressIndicator(task.taskDescription, am, req, 40.dp, false, false, true){}
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(5.dp))


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
                                color = Color.DarkGray
                            )
                        }*/



                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {


                        Box(
                            Modifier
                                .weight(1.0f)
                                .padding(horizontal = 0.dp),
                            contentAlignment = Alignment.Center
                        ){

                            Button(
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                                onClick = {
                                var updatedCompleteStructureModel = completeStructure.value


                                var newSection = sectionsList.value.get(0)
                                var newTasks = tasksMap.value.get(newSection.id)!!.associateWith { Pair(0, it.requiredAmount) }

                                updatedCompleteStructureModel = updatedCompleteStructureModel.copy(skillSection = newSection, skillTasks = newTasks)

                                completeStructure.value = updatedCompleteStructureModel

                                onResetSkillProgression()

                            }) {

                                Text(
                                    "Restart",
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(vertical = 0.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Box(
                            Modifier
                                .weight(1.0f)
                                .padding(horizontal = 0.dp),
                            contentAlignment = Alignment.Center
                        ){

                            Button(
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                                onClick = {
                                    onStopSkillProgression(skillProgression)
                                }) {
                                Text(
                                    "Stop Skill",
                                    fontSize = 18.sp, modifier = Modifier
                                        .padding(vertical = 0.dp), textAlign = TextAlign.Center
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(10.dp))

                    if(skill.creatorEmail == sharedViewModel.getCurrentUserMail()){
                        var isPublicText = if(skill.isPublic) "Public" else "Private"
                        var buttonText = if(skill.isPublic) "Un-publish" else "Publish"
                        var colorText = if (skill.isPublic) PrimaryColor else Color.Gray

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(text = "Skill Status", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                                color = Color.DarkGray,
                            )

                            Text(
                                modifier = Modifier
                                    .border(1.dp, colorText, RoundedCornerShape(10.dp))
                                    .padding(20.dp, 5.dp),
                                text = isPublicText,
                                fontSize = 15.sp,
                                color = colorText
                            )

                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(15.dp))




                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Box(
                                Modifier
                                    .weight(1.0f)
                                    .padding(horizontal = 0.dp),
                                contentAlignment = Alignment.Center
                            ){

                                Button(

                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),

                                    onClick = {
                                        if(skill.isPublic){
                                            onUnpublishSkill(skill)
                                        }else{
                                            onPublishSkill(skill)
                                        }
                                    }) {

                                    Text(text = buttonText, fontSize = 18.sp, modifier = Modifier
                                        .fillMaxWidth(0.35f)
                                        .padding(vertical = 0.dp), textAlign = TextAlign.Center)

                                }
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
    onRegisterSkill: () -> Unit,
    onUnpublishSkill: (SkillModel) -> Unit,
    onPublishSkill: (SkillModel) -> Unit,
    onDeleteSkill: (SkillModel) -> Unit
) {
    val currentContext = LocalContext.current

    val sectionsList: MutableState<List<SkillSectionModel>> = remember {
        mutableStateOf(emptyList())
    }

    var tasksMap: MutableState<Map<String, List<SkillTaskModel>>> = remember {
        mutableStateOf(emptyMap())
    }


    if(skill.id!=""){
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
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.7f))
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
                    .clip(shape = RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    //.verticalScroll(rememberScrollState()),
            ) {
                IconButton(
                    onClick = {
                        onCloseClick()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .zIndex(11F)
                        .padding(top = 10.dp, end = 10.dp),
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.SpaceBetween
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.LightGray.copy(alpha = 0.2f),
                                RoundedCornerShape(10.dp)
                            ) // Use the color of the background in your image
                            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                            .padding(horizontal = 20.dp, vertical = 17.dp),

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        /*Box(
                            modifier = Modifier
                                .size(50.dp) // Set the size of the circle
                                .background(PrimaryColor, shape = CircleShape) // Use the color of the circle in your image
                        )
                        Spacer(Modifier.width(25.dp))*/ // Space between the circle and the text
                        Column(modifier = Modifier.weight(2f)) {
                            Text(skill.titleSkill, fontWeight = FontWeight.Bold, fontSize = 30.sp,
                                color = Color.DarkGray,
                            )
                            Text(text = "Creator: " + skill.creatorUserName, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                                color = Color.DarkGray,
                            )
                        }

                        Text(
                            skill.skillSectionsList.count().toString() + " section" + if (skill.skillSectionsList.count() > 1) "s" else "",
                            color = Color.White,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .background(color = PrimaryColor, RoundedCornerShape(10.dp))
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .weight(1f)
                        )
                    }
                    /*Box(
                        modifier = Modifier
                            .size(windowWidth / 5) // Set the size of the circle
                            .background(PrimaryColor, shape = CircleShape)
                    )*/

                    Column(modifier = Modifier
                        .fillMaxWidth(),) {

                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(text = "Skill Description", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                                color = Color.DarkGray,
                            )

                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 0.dp, end = 0.dp),
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(15.dp))



                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.LightGray.copy(alpha = 0.2f),
                                    RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                        ) {
                            Text(
                                text = skill.skillDescription,
                                modifier = Modifier.padding(15.dp),
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Column(modifier = Modifier
                        .fillMaxWidth(),) {

                        Spacer(modifier = Modifier.height(40.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(text = "Sections", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                                color = Color.DarkGray,
                            )

                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        //SECTION ELEMENT

                        sectionsList.value.forEachIndexed { index, section ->
                            val indexOfCurrent = skill.skillSectionsList.indexOf(section.id)

                            val total = (tasksMap.value.get(section.id)?.map { entry ->
                                entry.requiredAmount
                            } ?: listOf(1)).sum()

                            val required = if(total == 0) 1 else total

                            if(index == 0){
                                SectionElementBlock(
                                    section = section,
                                    index = index,
                                    isResetable = false,
                                    amount = 1,
                                    required = -1
                                ) {

                                }
                            }else{
                                SectionElementBlock(section, index,  false, 0, required)
                                {}
                            }


                            // THE TASKS

                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp, 0.dp),) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        color = Color.DarkGray,
                                        thickness = 1.dp
                                    )

                                    Text(
                                        modifier = Modifier.padding(8.dp),
                                        text = "Tasks",
                                        color = Color.DarkGray,
                                        fontSize = 18.sp
                                    )

                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        color = Color.DarkGray,
                                        thickness = 1.dp
                                    )
                                }

                                tasksMap.value.get(section.id)?.forEach {task->

                                    val am = 0
                                    val req = task.requiredAmount

                                    CustomProgressIndicator(task.taskDescription, am, req, 40.dp, false, false, false){}

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
                                color = Color.DarkGray
                            )
                        }*/



                    }

                    if (!isRegistered) {
                        Button(

                            shape = RoundedCornerShape(10.dp),
                            onClick = { onRegisterSkill() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                        ) {
                            Text(text = "Register Skill", fontSize = 18.sp, modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .padding(vertical = 3.dp), textAlign = TextAlign.Center)
                        }
                    } else {
                        Button(
                            shape = RoundedCornerShape(10.dp),
                            onClick = { onAddProgression() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                        ) {
                            Text(text = "Start Skill", fontSize = 18.sp, modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .padding(vertical = 3.dp), textAlign = TextAlign.Center)                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    if(sharedViewModel.isMySkill(skill)){

                        var isPublicText = if(skill.isPublic) "Public" else "Private"
                        var buttonText = if(skill.isPublic) "Un-publish" else "Publish"
                        var colorText = if (skill.isPublic) PrimaryColor else Color.Gray


                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(text = "Skill Status", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                                color = Color.DarkGray,
                            )

                            Text(
                                modifier = Modifier
                                    .border(1.dp, colorText, RoundedCornerShape(10.dp))
                                    .padding(20.dp, 5.dp),
                                text = isPublicText,
                                fontSize = 15.sp,
                                color = colorText
                            )

                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Box(
                                Modifier
                                    .weight(1.0f)
                                    .padding(horizontal = 0.dp),
                                contentAlignment = Alignment.Center
                            ){

                                Button(

                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),

                                    onClick = {
                                        if(skill.isPublic){
                                            onUnpublishSkill(skill)
                                        }else{
                                            onPublishSkill(skill)
                                        }
                                }) {

                                    Text(text = buttonText, fontSize = 18.sp, modifier = Modifier
                                        .fillMaxWidth(0.35f)
                                        .padding(vertical = 0.dp), textAlign = TextAlign.Center)

                                }
                            }
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
    val windowInfo = rememberWindowInfo()


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

    val onlineFetchedSkills: MutableState<List<SkillModel>> = remember {
        mutableStateOf(mutableListOf())
    }

    val filteredOnlineFetchedSkills: MutableState<List<SkillModel>> = remember {
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

    var loadPublic: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    if(sharedViewModel.getCurrentUserMail()!="") {
        LaunchedEffect(currentUserSkillSubs.value) {
            sharedViewModel.LoadSearchScreen(
                currentContext,
                skillProgressions,
                skillModelsStarted,
                currentUserSkillSubs,
                skillModelsRegistered,
                skillModelsCreated
            )
        }

        LaunchedEffect(loadPublic.value) {
            sharedViewModel.retrieveOnlineSkills(currentContext) {
                onlineFetchedSkills.value =
                    it.filter { it.creatorEmail != sharedViewModel.getCurrentUserMail() }
            }
        }
    }

    Scaffold(
        modifier = Modifier.background(PrimaryColor),
        topBar = { AppToolBar(title = "Search a Skill", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController, openDialog, pendingRoute)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .testTag("SearchScreen"),
        ) {


            item {
/*
                Text(text = "Search a Skill", fontSize = 50.sp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp), textAlign = TextAlign.Center)*/

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = PrimaryColor,
                        focusedLabelColor = PrimaryColor,
                        focusedLeadingIconColor = PrimaryColor
                    ),
                    value = skillTitleEditText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .testTag("SearchBar"),
                    onValueChange = { skillTitleEditText = it },
                    label = { Text(text = "Search a skill by name") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    shape = RoundedCornerShape(10.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search"
                        )
                    },
                )

                Spacer(Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 0.dp)
                        .testTag("StartedSkillsDivider"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Started Skills", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.DarkGray,
                    )

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
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                var list = skillModelsStarted.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }
                var listA : List<SkillModel>
                var listB : List<SkillModel>

                if(list.isNotEmpty()){
                    listA = list.subList(0, list.size/2)
                    listB = list.subList(list.size/2, list.size)

                    item{
                        Row(){
                            Column(
                                Modifier
                                    .width(windowInfo.screenWidth.div(2))
                                    .weight(1f)){
                                listB.map{
                                    SkillSearchBlock(it, SelectedSkillState.STARTED_SELECTED)
                                    {
                                        skillSelected.value = it
                                        isSkillSelected.value = SelectedSkillState.STARTED_SELECTED
                                    }
                                }
                            }

                            Column(
                                Modifier
                                    .width(windowInfo.screenWidth.div(2))
                                    .weight(1f)){
                                listA.map{
                                    SkillSearchBlock(it, SelectedSkillState.STARTED_SELECTED)
                                    {
                                        skillSelected.value = it
                                        isSkillSelected.value = SelectedSkillState.STARTED_SELECTED
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else{
                items(skillModelsStarted.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }
                ) {
                    SkillSearchBlock(it, SelectedSkillState.STARTED_SELECTED)
                    {
                        skillSelected.value = it
                        isSkillSelected.value = SelectedSkillState.STARTED_SELECTED
                    }
                }
            }



            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, start = 10.dp, end = 10.dp)
                        .testTag("RegisteredSkillsDivider"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Registered Skills", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.DarkGray,
                    )

                    Text(
                        text = skillModelsRegistered.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase()
                                && it.id !in skillModelsStarted.value.map { it.id }}
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
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                var list = skillModelsRegistered.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase()
                        && it.id !in skillModelsStarted.value.map { it.id }}
                var listA : List<SkillModel>
                var listB : List<SkillModel>

                if(list.isNotEmpty()){
                    listA = list.subList(0, list.size/2)
                    listB = list.subList(list.size/2, list.size)

                    item{
                        Row(){
                            Column(
                                Modifier
                                    .width(windowInfo.screenWidth.div(2))
                                    .weight(1f),
                                verticalArrangement = Arrangement.Top){
                                listB.map{

                                    if(!it.isPublic && it.creatorEmail != sharedViewModel.getCurrentUserMail()){
                                        SkillSearchBlockUnavailable(it)
                                        {
                                            var listReg = currentUserSkillSubs.value.registeredSkillsIDs.toMutableList();
                                            var listTimeReg = currentUserSkillSubs.value.timeRegistered.toMutableList();
                                            val index = listReg.indexOf(it.id)

                                            listReg.removeAt(index);
                                            listTimeReg.removeAt(index);

                                            currentUserSkillSubs.value = currentUserSkillSubs.value.copy(registeredSkillsIDs = listReg, timeRegistered = listTimeReg)

                                            sharedViewModel.updateUserSub(currentUserSkillSubs.value, currentContext)

                                            skillModelsRegistered.value  = skillModelsRegistered.value - it
                                        }
                                    }else{

                                        SkillSearchBlock(it, SelectedSkillState.REGISTERED_SELECTED)
                                        {
                                            skillSelected.value = it
                                            isSkillSelected.value = SelectedSkillState.REGISTERED_SELECTED
                                        }
                                    }

                                }
                            }

                            Column(
                                Modifier
                                    .width(windowInfo.screenWidth.div(2))
                                    .weight(1f),
                                verticalArrangement = Arrangement.Top){
                                listA.map{

                                    if(!it.isPublic && it.creatorEmail != sharedViewModel.getCurrentUserMail()){
                                        SkillSearchBlockUnavailable(it)
                                        {
                                            var listReg = currentUserSkillSubs.value.registeredSkillsIDs.toMutableList();
                                            var listTimeReg = currentUserSkillSubs.value.timeRegistered.toMutableList();
                                            val index = listReg.indexOf(it.id)

                                            listReg.removeAt(index);
                                            listTimeReg.removeAt(index);

                                            currentUserSkillSubs.value = currentUserSkillSubs.value.copy(registeredSkillsIDs = listReg, timeRegistered = listTimeReg)

                                            sharedViewModel.updateUserSub(currentUserSkillSubs.value, currentContext)

                                            skillModelsRegistered.value  = skillModelsRegistered.value - it
                                        }
                                    }
                                    else{

                                        SkillSearchBlock(it, SelectedSkillState.REGISTERED_SELECTED)
                                        {
                                            skillSelected.value = it
                                            isSkillSelected.value = SelectedSkillState.REGISTERED_SELECTED
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else{
                items(skillModelsRegistered.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() && it.id !in skillModelsStarted.value.map { it.id }}
                ) {

                    if(!it.isPublic && it.creatorEmail != sharedViewModel.getCurrentUserMail()){
                        SkillSearchBlockUnavailable(it)
                        {
                            var listReg = currentUserSkillSubs.value.registeredSkillsIDs.toMutableList();
                            var listTimeReg = currentUserSkillSubs.value.timeRegistered.toMutableList();
                            val index = listReg.indexOf(it.id)

                            listReg.removeAt(index);
                            listTimeReg.removeAt(index);

                            currentUserSkillSubs.value = currentUserSkillSubs.value.copy(registeredSkillsIDs = listReg, timeRegistered = listTimeReg)

                            sharedViewModel.updateUserSub(currentUserSkillSubs.value, currentContext)

                            skillModelsRegistered.value  = skillModelsRegistered.value - it
                        }
                    }else{
                        SkillSearchBlock(it, SelectedSkillState.REGISTERED_SELECTED)
                        {
                            skillSelected.value = it
                            isSkillSelected.value = SelectedSkillState.REGISTERED_SELECTED
                        }
                    }
                }
            }





            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, start = 10.dp, end = 10.dp)
                        .testTag("CreatedSkillsDivider"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Created Skills", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.DarkGray,
                    )

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
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                var list = skillModelsCreated.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }
                var listA : List<SkillModel>
                var listB : List<SkillModel>

                if(list.isNotEmpty()){
                    listA = list.subList(0, list.size/2)
                    listB = list.subList(list.size/2, list.size)

                    item{
                        Row(){
                            Column(
                                Modifier
                                    .width(windowInfo.screenWidth.div(2))
                                    .weight(1f)){
                                listB.map{
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

                            Column(
                                Modifier
                                    .width(windowInfo.screenWidth.div(2))
                                    .weight(1f)){
                                listA.map{
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
                        }
                    }
                }
            }
            else{
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


            // ONLINE SKILLS

            if(loadPublic.value){
                filteredOnlineFetchedSkills.value = onlineFetchedSkills.value.filter { (skillTitleEditText in it.titleSkill) && (it !in skillModelsCreated.value) }.sortedByDescending { ZonedDateTime.parse(it.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME) }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp, start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Public Skills", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = Color.DarkGray,
                        )

                        Text(
                            text = filteredOnlineFetchedSkills.value.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }
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
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                    var list = onlineFetchedSkills.value.filter { (skillTitleEditText in it.titleSkill)
                            && (it !in skillModelsCreated.value) }.sortedByDescending { ZonedDateTime.parse(it.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME) }
                    var listA : List<SkillModel>
                    var listB : List<SkillModel>

                    if(list.isNotEmpty()){
                        listA = list.subList(0, list.size/2)
                        listB = list.subList(list.size/2, list.size)

                        item{
                            Row(){
                                Column(
                                    Modifier
                                        .width(windowInfo.screenWidth.div(2))
                                        .weight(1f)){
                                    listB.map{
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

                                Column(
                                    Modifier
                                        .width(windowInfo.screenWidth.div(2))
                                        .weight(1f)){
                                    listA.map{
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
                            }
                        }
                    }
                }
                else{
                    items( filteredOnlineFetchedSkills.value
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
            }
            else{
                item {

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, start = 10.dp, end = 10.dp),
                        contentAlignment = Alignment.Center){
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                            onClick = {loadPublic.value = true},
                            shape = RoundedCornerShape(10.dp),
                        ) {
                            Text(text = "Search Online", fontSize = 20.sp, modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .padding(vertical = 3.dp), textAlign = TextAlign.Center)

                        }
                    }


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

                        val indexOfTime = currentUserSkillSubs.value.registeredSkillsIDs.indexOf(skillSelected.value.id)

                        var registeredSkillIds  = currentUserSkillSubs.value.registeredSkillsIDs - skillSelected.value.id
                        var timesRegisterSkills = currentUserSkillSubs.value.timeRegistered.toMutableList()

                        if(indexOfTime >= 0){
                            timesRegisterSkills.removeAt(indexOfTime)
                        }


                        var skillCustomOrdering = currentUserSkillSubs.value.customOrdering.toMutableList()
                        skillCustomOrdering.add(0, skillProgression.skillId)

                        currentUserSkillSubs.value = currentUserSkillSubs.value.copy(
                            startedSkillsIDs = startedSkillIds,
                            registeredSkillsIDs = registeredSkillIds,
                            customOrdering = skillCustomOrdering,
                            timeRegistered = timesRegisterSkills,
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
                    val timeRegistered = currentUserSkillSubs.value.timeRegistered
                    val timeToAdd = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

                    currentUserSkillSubs.value =
                        currentUserSkillSubs.value.copy(registeredSkillsIDs = regSkillIds + skillSelected.value.id, timeRegistered = timeRegistered + timeToAdd)

                    sharedViewModel.updateUserSub(currentUserSkillSubs.value, currentContext)

                    skillModelsRegistered.value += skillSelected.value

                    isSkillSelected.value = SelectedSkillState.REGISTERED_SELECTED
                },
                {skill ->
                    //TAKE CARE OF THE DB
                    sharedViewModel.unPublishSkill(skill, currentContext)
                    //TAKE CARE OF THE LOCAL
                    skillModelsRegistered.value = skillModelsRegistered.value.toMutableList().map {
                        if(it.id == skill.id) skill.copy(isPublic = false) else it
                    }.toList()

                    skillModelsCreated.value = skillModelsCreated.value.toMutableList().map {
                        if(it.id == skill.id) skill.copy(isPublic = true) else it
                    }.toList()

                    skillSelected.value = skillSelected.value.copy(isPublic = false)
                },
                {skill ->
                    sharedViewModel.publishSkill(skill, currentContext)

                    skillModelsRegistered.value = skillModelsRegistered.value.toMutableList().map {
                        if(it.id == skill.id) skill.copy(isPublic = true) else it
                    }

                    skillModelsCreated.value = skillModelsCreated.value.toMutableList().map {
                        if(it.id == skill.id) skill.copy(isPublic = true) else it
                    }.toList()

                    skillSelected.value = skillSelected.value.copy(isPublic = true)
                },
                {

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

                },
                {skillProgression ->
                    sharedViewModel.StopSkillProgressionSS(
                        sharedViewModel,
                        skillProgression,
                        skillModelsStarted,
                        onlineFetchedSkills,
                        skillProgressions,
                        isSkillSelected,
                        currentUserSkillSubs,
                        currentContext
                    )
                },
                {skill ->
                    //TAKE CARE OF THE DB
                    sharedViewModel.unPublishSkill(skill, currentContext)
                    //TAKE CARE OF THE LOCAL
                    skillModelsStarted.value = skillModelsStarted.value.toMutableList().map {
                        if(it.id == skill.id) skill.copy(isPublic = false) else it
                    }.toList()

                    skillModelsCreated.value = skillModelsCreated.value.toMutableList().map {
                        if(it.id == skill.id) skill.copy(isPublic = false) else it
                    }.toList()

                    skillSelected.value = skillSelected.value.copy(isPublic = false)
                },
                {skill ->
                    sharedViewModel.publishSkill(skill, currentContext)

                    skillModelsStarted.value = skillModelsStarted.value.toMutableList().map {
                        if(it.id == skill.id) skill.copy(isPublic = true) else it
                    }

                    skillModelsCreated.value = skillModelsCreated.value.toMutableList().map {
                        if(it.id == skill.id) skill.copy(isPublic = true) else it
                    }.toList()

                    skillSelected.value = skillSelected.value.copy(isPublic = true)
                },
                {

                }
            )
        }
    }


    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}


