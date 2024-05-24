package com.example.app.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.app.additionalUI.BadgeColor
import com.example.app.additionalUI.BadgeIcon
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillCompleteStructureModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserSkillSubsModel
import com.example.app.util.SharedViewModel
import com.example.app.util.relative
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max


enum class SortingType{
    DateAsc, DateDesc, Custom
}

@Composable
fun SkillTitleBlock(skillCompleteStructureModel: SkillCompleteStructureModel, badge : BadgeDataModel) {
    val colorCircle = MaterialTheme.colorScheme.primary

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
        BadgeIcon(badge = badge.badgeColor, size = relative(65.dp), filled = badge.skillId != "")
        Spacer(Modifier.width(10.dp)) // Space between the circle and the text
        Column(modifier = Modifier.weight(1f)) {
            Text(
                skillCompleteStructureModel.skillSection.titleSection,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(skillCompleteStructureModel.skillSection.descriptionSection, fontSize = 16.sp)
        }
        val completedAmount: Int =
            skillCompleteStructureModel.skillTasks.values.map { pair -> pair.first }.sum()
        val requiredAmount: Int =
            skillCompleteStructureModel.skillTasks.values.map { pair -> pair.second }.sum()
        Text(text = "$completedAmount/$requiredAmount", fontSize = 20.sp)
    }
}

@Composable
fun SkillListElement(
    skillCompleteStructureModel: SkillCompleteStructureModel,
    index: Int,
    sortingType: SortingType,
    sharedViewModel: SharedViewModel,
    onClickTask: (Int, SkillTaskModel) -> Unit,
    onValidateSkill: (Int) -> Unit,
    onOpenMenu: (Int) -> Unit,
    onSwapRequest: (Int, Boolean) -> Unit,
    onBadgeAchieved: (BadgeDataModel) -> Unit
) {
    val currentContext: Context = LocalContext.current

    val badge : MutableState<BadgeDataModel> = remember {
        mutableStateOf(BadgeDataModel())
    }

    val done : MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    sharedViewModel.retrieveBadge(
        skillCompleteStructureModel.skill.id,
        skillCompleteStructureModel.skillSection.id,
        currentContext
    ){
        badge.value = it
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0XFFD9D9D9), RoundedCornerShape(10.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        if(sortingType == SortingType.Custom){
            Row (modifier = Modifier.fillMaxWidth()){
                IconButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = { onSwapRequest(index, true) }
                )
                {
                    Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = "")
                }

                IconButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = { onSwapRequest(index, false) }
                )
                {
                    Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "")
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                skillCompleteStructureModel.skill.titleSkill,
                modifier = Modifier.weight(1.0f),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    onOpenMenu(index)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            }


        }


        SkillTitleBlock(skillCompleteStructureModel, badge.value)

        Column(
            modifier = Modifier
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

            var completedTasks = 0

            skillCompleteStructureModel.skillTasks.forEach {
                TaskListElement(
                    progression = it.value.first,
                    task = it.key
                ) { onClickTask(index, it.key) }

                if(it.value.first == it.key.requiredAmount)
                    completedTasks++
            }

            if(completedTasks==skillCompleteStructureModel.skillTasks.size && !done.value){
                onBadgeAchieved(badge.value)
                done.value = true
            }
            else if (!skillCompleteStructureModel.skillProgression.isFinished)
                done.value = false

            if (skillCompleteStructureModel.skillProgression.isFinished) {

                Button(
                    onClick = { onValidateSkill(index) },
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        fontSize = 20.sp,
                        color = Color.White,
                        text = "Finish"
                    )
                }
            }
        }

    }
}

@Composable
fun TaskListElement(progression: Int, task: SkillTaskModel, onClickTask: () -> Unit) {

    CustomProgressIndicator(
        description = task.taskDescription,
        amount = progression,
        required = task.requiredAmount,
        height = 40.dp,
        onClickTask = onClickTask
    )


}

@Composable
fun SkillListBlock(
    listSkills: List<SkillCompleteStructureModel>,
    sortingType: SortingType,
    sharedViewModel: SharedViewModel,
    onClickTask: (Int, SkillTaskModel) -> Unit,
    onValidateSkill: (Int) -> Unit,
    onOpenMenu: (Int) -> Unit,
    onSwapRequest: (Int, Boolean) -> Unit,
    onBadgeAchieved: (BadgeDataModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp, 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listSkills.forEachIndexed { index, skillStructure ->
            SkillListElement(
                skillCompleteStructureModel = skillStructure,
                index,
                sortingType,
                sharedViewModel,
                onClickTask,
                onValidateSkill,
                onOpenMenu,
                onSwapRequest,
                onBadgeAchieved
            )
        }
    }
}

@Composable
fun CustomProgressIndicator(
    description: String,
    amount: Int,
    required: Int,
    height: Dp,
    isUnstarted: Boolean = false,
    isClickable: Boolean = true,
    isLongPressable: Boolean = false,
    onClickTask: () -> Unit = {}
) {

    val isDoneText =
        if (amount == required) "Done" else if (isUnstarted) "Unstarted" else amount.toString() + "/" + required.toString()

    val colorNum = amount.toFloat() / required

    var colorRed = 255 + (163 - 255) * colorNum
    var colorGreen = 130 + (255 - 130) * colorNum
    var colorBlue: Float = 136 + (130 - 136) * colorNum

    val color = Color(colorRed / 255.0f, colorGreen / 255.0f, colorBlue / 255.0f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(height)
            .then(
                if (isLongPressable) {
                    Modifier.clickable { onClickTask() }
                } else if (isClickable) {
                    Modifier.clickable { onClickTask() }
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(15))
                    .border(1.dp, Color.Black, RoundedCornerShape(15))
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color, RoundedCornerShape(15))
                    .border(1.dp, Color.Black, RoundedCornerShape(15))
                    .fillMaxWidth(amount.toFloat() / required.toFloat())
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = description,
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

fun ComputeListTaskAdd1(
    context: Context,
    index: Int,
    task: SkillTaskModel,
    listCompleteStructures: MutableList<SkillCompleteStructureModel>
): MutableList<SkillCompleteStructureModel> {
    var updatedList = listCompleteStructures
    var updatedStructure = updatedList.get(index)

    val basedNumber = updatedStructure.skillTasks[task]!!.first
    var updatedMap = updatedStructure.skillTasks.toMutableMap()
    //Toast.makeText(context, minOf(basedNumber + 1, task.requiredAmount).toString(), Toast.LENGTH_SHORT).show()
    updatedMap.set(task, Pair(minOf(basedNumber + 1, task.requiredAmount), task.requiredAmount))

    var updatedProgression = updatedStructure.skillProgression
    var updatedProgMap = updatedProgression.mapNonCompletedTasks.toMutableMap()

    if (basedNumber + 1 >= task.requiredAmount) {
        updatedProgMap.remove(task.id)
    } else {
        updatedProgMap.set(task.id, minOf(basedNumber + 1, task.requiredAmount))
    }

    if (updatedStructure.skill.skillSectionsList.indexOf(updatedStructure.skillProgression.currentSectionId) == updatedStructure.skill.skillSectionsList.size - 1 && updatedProgMap.isEmpty()) {
        updatedProgression = updatedProgression.copy(isFinished = true)
    }

    updatedProgression = updatedProgression.copy(mapNonCompletedTasks = updatedProgMap)
    updatedStructure =
        updatedStructure.copy(skillProgression = updatedProgression, skillTasks = updatedMap)

    updatedList.set(index, updatedStructure)

    return updatedList
}

fun ComputeListTaskSub1(
    context: Context,
    task: SkillTaskModel,
    skillProgressionModel: SkillProgressionModel
): SkillProgressionModel {
    var result = skillProgressionModel

    var updatedMap = result.mapNonCompletedTasks.toMutableMap()

    var newAmount = 0

    if (task.id in result.mapNonCompletedTasks.keys) {
        Toast.makeText(context, result.mapNonCompletedTasks.get(task.id).toString(), Toast.LENGTH_SHORT).show()
        newAmount = max((result.mapNonCompletedTasks.get(task.id) ?: 0) - 1, 0)
    } else {
        newAmount = task.requiredAmount - 1
    }

    updatedMap[task.id] = newAmount

    result = result.copy(mapNonCompletedTasks = updatedMap.toMap(), isFinished = false)

    return result
}


fun ComputeSkipSection(
    index: Int,
    listCompleteStructures: MutableList<SkillCompleteStructureModel>
): Boolean {

    var updatedList = listCompleteStructures
    var updatedStructure = updatedList.get(index)

    val indexOfSection =
        updatedStructure.skill.skillSectionsList.indexOf(updatedStructure.skillProgression.currentSectionId)

    val mustSkipSection: Boolean = updatedStructure.skillTasks.all {
        it.value.first == it.value.second
    } && (indexOfSection != updatedStructure.skill.skillSectionsList.size - 1)


    return mustSkipSection
}

fun FinishSkill(
    sharedViewModel: SharedViewModel,
    listCompleteStructures: MutableState<List<SkillCompleteStructureModel>>,
    userSkillSub: MutableState<UserSkillSubsModel>,
    currentStructureIndex: MutableState<Int>,
    currentContext: Context
) {
    var updatedList = listCompleteStructures.value.toMutableList()
    var updatedStructure = updatedList.get(currentStructureIndex.value)


    var updatedProgression = updatedStructure.skillProgression

    val indexOfSection =
        updatedStructure.skill.skillSectionsList.indexOf(updatedProgression.currentSectionId)

    updatedProgression = updatedProgression.copy(isFinished = true)

    val it = userSkillSub.value

    val skillSection =
        listCompleteStructures.value.get(currentStructureIndex.value).skillSection

    var updatedBadges = it.badgesObtained
    var updatedTimeBadge = it.timeBadgeObtained
    var updatedFinishedSkills = it.finishedSkills
    var updatedTimesFinishSkills = it.timeFinishedFirstTime
    var updatedStartedSkills = it.startedSkillsIDs
    var updatedCustomOrder = it.customOrdering

    if (skillSection.hasBadge && !(skillSection.badgeID in updatedBadges)) {
        updatedBadges = updatedBadges + skillSection.badgeID
        updatedTimeBadge = updatedTimeBadge + ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    if (updatedStructure.skill.id !in updatedFinishedSkills) {
        updatedFinishedSkills += updatedStructure.skill.id
        updatedTimesFinishSkills += ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    if (updatedStructure.skill.id in updatedStartedSkills) {
        updatedStartedSkills -= updatedStructure.skill.id
    }

    if (updatedStructure.skill.id in updatedCustomOrder) {
        updatedCustomOrder -= updatedStructure.skill.id
    }

    userSkillSub.value = it.copy(
        badgesObtained = updatedBadges,
        timeBadgeObtained = updatedTimeBadge,
        finishedSkills = updatedFinishedSkills,
        timeFinishedFirstTime = updatedTimesFinishSkills,
        startedSkillsIDs = updatedStartedSkills,
        customOrdering = updatedCustomOrder,
    )

    sharedViewModel.saveUserSub(
        userSkillSub.value,
        context = currentContext
    )

    val skillProgressionModel =
        listCompleteStructures.value.get(currentStructureIndex.value).skillProgression

    sharedViewModel.removeSkillProgression(skillProgressionModel, currentContext)
    listCompleteStructures.value -= listCompleteStructures.value.get(currentStructureIndex.value)

}


//TODO SOLVE
fun StopSkillProgressionMS(
    sharedViewModel: SharedViewModel,
    listCompleteStructures: MutableState<List<SkillCompleteStructureModel>>,
    userSkillSub: MutableState<UserSkillSubsModel>,
    currentStructureIndex: MutableState<Int>,
    currentContext: Context
) {
    var updatedList = listCompleteStructures.value.toMutableList()
    var updatedStructure = updatedList.get(currentStructureIndex.value)


    var updatedProgression = updatedStructure.skillProgression

    val indexOfSection =
        updatedStructure.skill.skillSectionsList.indexOf(updatedProgression.currentSectionId)

    val it = userSkillSub.value

    val skillSection =
        listCompleteStructures.value.get(currentStructureIndex.value).skillSection

    var updatedStartedSkills = it.startedSkillsIDs
    var updatedCustomOrder = it.customOrdering

    if (updatedStructure.skill.id in updatedStartedSkills) {
        updatedStartedSkills -= updatedStructure.skill.id
    }

    if (updatedStructure.skill.id in updatedCustomOrder) {
        updatedCustomOrder -= updatedStructure.skill.id
    }

    userSkillSub.value = it.copy(
        startedSkillsIDs = updatedStartedSkills,
        customOrdering = updatedCustomOrder,
    )

    sharedViewModel.saveUserSub(
        userSkillSub.value,
        context = currentContext
    )

    val skillProgressionModel =
        listCompleteStructures.value.get(currentStructureIndex.value).skillProgression

    sharedViewModel.removeSkillProgression(skillProgressionModel, currentContext)
    listCompleteStructures.value -= listCompleteStructures.value.get(currentStructureIndex.value)

}

fun RecomputeList(listCompleteStructures: List<SkillCompleteStructureModel>, sortingType: SortingType, customOrdering: List<String>): List<SkillCompleteStructureModel>{
     val result = listCompleteStructures.sortedWith(Comparator { a, b ->
        when(sortingType) {
            SortingType.Custom -> {
                val indexA = customOrdering.indexOf(a.skill.id)
                val indexB = customOrdering.indexOf(b.skill.id)
                indexA.compareTo(indexB)
            }
            SortingType.DateAsc -> {
                val dateA = ZonedDateTime.parse(a.skillProgression.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                val dateB = ZonedDateTime.parse(b.skillProgression.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                dateA.compareTo(dateB)
            }
            SortingType.DateDesc -> {
                val dateA = ZonedDateTime.parse(a.skillProgression.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                val dateB = ZonedDateTime.parse(b.skillProgression.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                dateB.compareTo(dateA)  // Reverse the comparison for descending order
            }
        }
    })

    return result
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MySkillsScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    openDialog: MutableState<Boolean>,
    pendingRoute: MutableState<String?>

) {

    var loadTrigger: MutableState<Boolean> = remember { mutableStateOf(false) }

    var userSkillSub: MutableState<UserSkillSubsModel> = remember {
        mutableStateOf(UserSkillSubsModel())
    }

    val listCompleteStructures: MutableState<List<SkillCompleteStructureModel>> = remember {
        mutableStateOf(listOf())
    }

    val currentContext: Context = LocalContext.current

    var isStartRun: MutableState<Boolean> = remember {
        mutableStateOf(true)
    }

    var currentStructureIndex: MutableState<Int> = remember {
        mutableStateOf(0)
    }

    var triggerSectionSkip: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    // Pop up variables
    var isPopUpOpen: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    var isBadgePopUpOpen: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    var badgeObtained : MutableState<BadgeDataModel> = remember {
        mutableStateOf(BadgeDataModel())
    }

    val sortingType: MutableState<SortingType> = remember {
        mutableStateOf(SortingType.Custom)
    }

    val sortingMenuExp: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }


    LaunchedEffect(loadTrigger.value) {

        sharedViewModel.retrieveUserSkillSub(
            sharedViewModel.getCurrentUserMail(),
            currentContext
        ){
            userSkillSub.value = it
            sharedViewModel.retrieveUserSkillProgressionList(
                sharedViewModel.getCurrentUserMail(),
                context = currentContext
            ) { skillProgList ->

                skillProgList.forEach { skillProg ->
                    var skill: SkillModel
                    var skillSection: SkillSectionModel
                    var structure: SkillCompleteStructureModel

                    sharedViewModel.retrieveSkill(skillProg.skillId, currentContext) { data ->
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

                                if (listCompleteStructures.value.map { it.skill }
                                        .contains(structure.skill)) {
                                    val updatedList = listCompleteStructures.value.toMutableList()

                                    val index =
                                        listCompleteStructures.value.indexOf(listCompleteStructures.value.find { it.skill == structure.skill })

                                    updatedList.set(index, structure)

                                    listCompleteStructures.value = updatedList.toList()

                                } else {
                                    listCompleteStructures.value += structure
                                }

                                listCompleteStructures.value = RecomputeList(
                                    listCompleteStructures.value,
                                    sortingType.value,
                                    userSkillSub.value.customOrdering
                                )

                            }
                        }
                    }
                }
            }
        }

    }

    LaunchedEffect(triggerSectionSkip.value) {

        if (isStartRun.value) {
            return@LaunchedEffect
        }


        var updatedList = listCompleteStructures.value.toMutableList()
        var updatedStructure = updatedList.get(currentStructureIndex.value)


        var updatedProgression = updatedStructure.skillProgression

        val indexOfSection =
            updatedStructure.skill.skillSectionsList.indexOf(updatedProgression.currentSectionId)


        //FIRST WE ADD THE BADGE
        sharedViewModel.retrieveUserSkillSub(sharedViewModel.getCurrentUserMail(), currentContext) {


            val skillSection =
                listCompleteStructures.value.get(currentStructureIndex.value).skillSection

            var updatedBadges = it.badgesObtained
            var updatedTimeBadge = it.timeBadgeObtained

            if (skillSection.hasBadge && !(skillSection.badgeID in updatedBadges)) {
                updatedBadges = updatedBadges + skillSection.badgeID
                updatedTimeBadge += ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            }
            userSkillSub.value = it.copy(badgesObtained = updatedBadges, timeBadgeObtained = updatedTimeBadge)

            sharedViewModel.saveUserSub(
                userSkillSub.value,
                context = currentContext
            )

        }

        //SECOND WE UPDATE THE STRUCT
        updatedProgression.currentSectionId =
            updatedStructure.skill.skillSectionsList.get(indexOfSection + 1)

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

                updatedProgression =
                    updatedProgression.copy(mapNonCompletedTasks = listTasks.associate {
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
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {


            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {


                Box(modifier = Modifier.clickable { /*onAddBadgeProcess(id.toString())*/ sortingMenuExp.value =
                    !sortingMenuExp.value
                }) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .zIndex(11F),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "badge"
                    )

                    DropdownMenu(
                        expanded = sortingMenuExp.value,
                        onDismissRequest = { sortingMenuExp.value = false }
                    ) {
                        DropdownMenuItem(text = { Text(text = "Custom") }, onClick = {
                            sortingMenuExp.value = false
                            sortingType.value = SortingType.Custom
                        })

                        DropdownMenuItem(text = { Text(text = "Date Asc") }, onClick = {
                            sortingMenuExp.value = false
                            sortingType.value = SortingType.DateAsc
                        })
                        DropdownMenuItem(text = { Text(text = "Date Desc") }, onClick = {
                            sortingMenuExp.value = false
                            sortingType.value = SortingType.DateDesc
                        })
                    }

                }
            }

            

            SkillListBlock(listSkills = listCompleteStructures.value,
                sortingType.value,
                sharedViewModel,
                { index, task ->

                    val updatedList = ComputeListTaskAdd1(
                        currentContext,
                        index,
                        task,
                        listCompleteStructures.value.toMutableList()
                    )

                    val mustSkipSection = ComputeSkipSection(index, updatedList)
                    if (mustSkipSection) {
                        currentStructureIndex.value = index

                        isStartRun.value = false
                        triggerSectionSkip.value = !triggerSectionSkip.value
                    } else {
                        sharedViewModel.saveSkillProgression(
                            updatedList.get(index).skillProgression,
                            currentContext
                        )
                    }

                    listCompleteStructures.value = updatedList
                },
                { index ->

                    currentStructureIndex.value = index
                    isStartRun.value = false

                    FinishSkill(
                        sharedViewModel,
                        listCompleteStructures,
                        userSkillSub,
                        currentStructureIndex,
                        currentContext
                    )

                },
                { index ->
                    currentStructureIndex.value = index
                    isPopUpOpen.value = true
                },
                { index, isUpwards ->
                    if(index == 0 && isUpwards) return@SkillListBlock
                    if((index == listCompleteStructures.value.size - 1) && !isUpwards) return@SkillListBlock

                    val offset = if(isUpwards) -1 else 1

                    var updatedList = userSkillSub.value.customOrdering.toMutableList()

                    val temp = updatedList.get(index + offset)

                    updatedList[index + offset] = updatedList.get(index)
                    updatedList[index] = temp

                    userSkillSub.value = userSkillSub.value.copy(customOrdering = updatedList.toList())
                    sharedViewModel.updateUserSub(userSkillSub.value, currentContext)

                    listCompleteStructures.value = RecomputeList(listCompleteStructures.value, sortingType.value, userSkillSub.value.customOrdering)
                },
                { badge ->
                    badgeObtained.value = badge
                    isBadgePopUpOpen.value = true
                }
            )
        }

        if (isPopUpOpen.value) {

            var listCompleteStructureModel = listCompleteStructures.value
            var skillProgInterested =
                listCompleteStructureModel.get(currentStructureIndex.value).skillProgression


            SkillInfoPopUp_STARTED(
                sharedViewModel = sharedViewModel,
                skill = listCompleteStructures.value.get(currentStructureIndex.value).skill,
                skillProgression = skillProgInterested,
                {task, completeStructure ->
                    val baseStructure =
                        completeStructure

                    var updatedProgression = baseStructure.skillProgression
                    //val task = baseStructure.skillTasks.keys.find { entry -> entry.id == taskId } ?: SkillTaskModel()
                    //listCompleteStructures.value = listCompleteStructures.value.toMutableList().set()


                    sharedViewModel.updateSkillProgression(
                        sharedViewModel.getCurrentUserMail(),
                        baseStructure.skill.id,
                        updatedProgression,
                        currentContext
                    )
                },
                {
                    val baseStructure =
                        listCompleteStructures.value.get(currentStructureIndex.value)

                    var updatedProgression = baseStructure.skillProgression
                    var newSection = baseStructure.skill.skillSectionsList.get(0)

                    sharedViewModel.retrieveSkillSection(
                        baseStructure.skill.id,
                        newSection,
                        currentContext,
                    ) {
                        var newTasks = it.skillTasksList.associateWith { 0 }

                        updatedProgression = updatedProgression.copy(
                            currentSectionId = newSection,
                            isFinished = false,
                            mapNonCompletedTasks = newTasks
                        )

                        sharedViewModel.updateSkillProgression(
                            sharedViewModel.getCurrentUserMail(),
                            baseStructure.skill.id,
                            updatedProgression,
                            currentContext
                        )
                        loadTrigger.value = !loadTrigger.value
                    }
                },
                {
                    isPopUpOpen.value = false
                    loadTrigger.value = !loadTrigger.value
                },
                {section ->
                    val baseStructure =
                        listCompleteStructures.value.get(currentStructureIndex.value)

                    var updatedProgression = baseStructure.skillProgression
                    var newSection = section.id

                    sharedViewModel.retrieveSkillSection(
                        baseStructure.skill.id,
                        newSection,
                        currentContext,
                    ) {
                        var newTasks = it.skillTasksList.associateWith { 0 }

                        updatedProgression = updatedProgression.copy(
                            currentSectionId = newSection,
                            isFinished = false,
                            mapNonCompletedTasks = newTasks
                        )

                        sharedViewModel.updateSkillProgression(
                            sharedViewModel.getCurrentUserMail(),
                            baseStructure.skill.id,
                            updatedProgression,
                            currentContext
                        )
                        loadTrigger.value = !loadTrigger.value
                    }
                },
                {skillProgression ->

                    isStartRun.value = false
                    isPopUpOpen.value = false

                    StopSkillProgressionMS(
                        sharedViewModel,
                        listCompleteStructures,
                        userSkillSub,
                        currentStructureIndex,
                        currentContext
                    )
                },
                {skill ->
                    sharedViewModel.unPublishSkill(skill, currentContext)

                    listCompleteStructures.value = listCompleteStructures.value.map { if(it.skill.id == skill.id) it.copy(skill = it.skill.copy(isPublic = false)) else it}
                },
                {skill ->
                    sharedViewModel.publishSkill(skill, currentContext)

                    listCompleteStructures.value = listCompleteStructures.value.map { if(it.skill.id == skill.id) it.copy(skill = it.skill.copy(isPublic = true)) else it}
                },
                {

                }
            )
        }

        if(isBadgePopUpOpen.value){
            BadgeCompletedPopUp(badge = badgeObtained.value) {
                isBadgePopUpOpen.value = false
            }
        }
    }

}

@Composable
fun BadgeCompletedPopUp(
    badge : BadgeDataModel,
    onCloseClick: () -> Unit,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.7f))
            .zIndex(10F)
            .clickable { },
        contentAlignment = Alignment.Center
    ){
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                excludeFromSystemGesture = true,
            ),
            onDismissRequest = { onCloseClick() }
        ){
            Box(
                Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.6f)
                    .clip(shape = RoundedCornerShape(25.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surface),
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
                        .padding(0.dp, 0.dp, 0.dp, 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("CONGRATULATIONS!", fontSize = 30.sp, fontWeight = FontWeight.Bold)

                    BadgeIcon(badge = badge.badgeColor, size = relative(150.dp))

                    Text("You just obtained a", fontSize = 24.sp)

                    var color : String = "BRONZE MEDAL"

                    if(badge.badgeColor == BadgeColor.SILVER)
                        color = "SILVER MEDAL"

                    if(badge.badgeColor == BadgeColor.GOLD)
                        color = "GOLD MEDAL"

                    Text(text = color, fontSize = 27.sp)

                    Text(badge.badgeName, fontSize = 20.sp)

                    Text(badge.description, fontSize = 20.sp)
                }
            }
        }
    }
}
