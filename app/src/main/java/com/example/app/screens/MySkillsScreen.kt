package com.example.app.screens

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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
import com.example.app.ui.theme.PrimaryColor
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
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
            .padding(10.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        BadgeIcon(badge = badge.badgeColor, size = relative(65.dp), filled = badge.skillId != "")
        Spacer(Modifier.width(10.dp)) // Space between the circle and the text
        Column(modifier = Modifier.weight(1f)) {
            Text(
                color = Color.DarkGray,
                text = skillCompleteStructureModel.skillSection.titleSection,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(skillCompleteStructureModel.skillSection.descriptionSection, fontSize = 16.sp,
                color = Color.DarkGray,
            )
        }
        val completedAmount: Int =
            skillCompleteStructureModel.skillTasks.values.map { pair -> pair.first }.sum()
        val requiredAmount: Int =
            skillCompleteStructureModel.skillTasks.values.map { pair -> pair.second }.sum()
        Text(text = "$completedAmount/$requiredAmount", fontSize = 20.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
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
/*
    val done : MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
*/
    if(skillCompleteStructureModel.skill.id!=""){
        sharedViewModel.retrieveBadge(
            skillCompleteStructureModel.skill.id,
            skillCompleteStructureModel.skillSection.id,
            currentContext
        ){
            badge.value = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .padding(10.dp)
            .testTag("SkillListBlock"),
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
                    Icon(imageVector = Icons.Default.ArrowUpward, tint = Color.DarkGray, contentDescription = "ArrowUp")
                }

                IconButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = { onSwapRequest(index, false) }
                )
                {
                    Icon(imageVector = Icons.Default.ArrowDownward, tint = Color.DarkGray, contentDescription = "ArrowDown")
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                skillCompleteStructureModel.skill.titleSkill,
                modifier = Modifier.weight(1.0f).padding(start = 20.dp),
                color = Color.DarkGray,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Button(
                modifier = Modifier.padding(end = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(
                    8.dp
                ),
                onClick = {
                    onOpenMenu(index)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "MenuButton"
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
                    color = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                )

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Tasks",
                    color = Color.DarkGray,
                    fontSize = 18.sp
                )

                Divider(
                    color = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 10.dp),
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
/*
            if(completedTasks==skillCompleteStructureModel.skillTasks.size && !done.value){
                onBadgeAchieved(badge.value)
                done.value = true
            }
            else if (!skillCompleteStructureModel.skillProgression.isFinished)
                done.value = false

 */

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
            .padding(vertical = 5.dp)
            .height(IntrinsicSize.Min)
            .then(
                if (isLongPressable) {
                    Modifier.background(Color.White, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                        .clickable { onClickTask() }
                } else if (isClickable) {
                    Modifier.background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                        .clickable { onClickTask() }
                } else {
                    Modifier.background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(amount.toFloat() / required.toFloat())
                .then(
                    if (isLongPressable){
                        Modifier
                            .background(color, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                        //.border(1.dp, Color.DarkGray, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                    }else{
                        Modifier
                            .background(color, RoundedCornerShape(10.dp))
                        //.border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                    }
                )
                //.border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                /*.then(
                    if (isLongPressable){
                        Modifier
                            .background(color, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                    }else{
                        Modifier
                            .background(color, RoundedCornerShape(10.dp))
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                    }
                )
                .fillMaxWidth(amount.toFloat() / required.toFloat())
                .fillMaxHeight()*/
                .padding(0.dp, 1.dp)
                .fillMaxHeight()

        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = description,
                color = Color.DarkGray,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1.0f).padding(start = 40.dp)
            )

            Text(
                text = isDoneText,
                color = Color.DarkGray,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

        }
        /*Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (isLongPressable){
                            Modifier
                                .background(Color.White, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                        }else{
                            Modifier
                                .background(Color.White, RoundedCornerShape(10.dp))
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                        }
                    )
                    .padding(vertical = 5.dp)

            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .then(
                        if (isLongPressable){
                            Modifier
                                .background(color, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                        }else{
                            Modifier
                                .background(color, RoundedCornerShape(10.dp))
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                        }
                    )
                    .fillMaxWidth(amount.toFloat() / required.toFloat())
                    .padding(vertical = 5.dp)

            )
        }*/
    }
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

    var canSkipSection: MutableState<Boolean> = remember {
        mutableStateOf(true)
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

    if(sharedViewModel.getCurrentUserMail()!=""){
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

                                    listCompleteStructures.value = sharedViewModel.RecomputeList(
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


            sharedViewModel.LoadMySkills(
                listCompleteStructures,
                currentStructureIndex,
                currentContext,
                badgeObtained,
                isBadgePopUpOpen,
                userSkillSub,
                canSkipSection
            )


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
                .verticalScroll(rememberScrollState())
                .testTag("MySkillsScreen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 5.dp)
                .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .padding(vertical = 15.dp),
                contentAlignment = Alignment.Center
            )
            {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    var order = "Custom"
                    if(sortingType.value == SortingType.Custom)
                        order = "Custom"
                    if(sortingType.value == SortingType.DateAsc)
                        order = "Date Asc"
                    if(sortingType.value == SortingType.DateDesc)
                        order = "Date Desc"


                    Row(modifier = Modifier.clickable { /*onAddBadgeProcess(id.toString())*/ sortingMenuExp.value =
                        !sortingMenuExp.value
                        },
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text("Order by: $order",
                            modifier = Modifier.padding(start = 10.dp),
                            fontSize = 20.sp,
                            color = Color.DarkGray,
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            tint = Color.DarkGray,
                            contentDescription = "OrderingArrow",
                        )

                        DropdownMenu(
                            modifier = Modifier.padding(top = 10.dp),
                            expanded = sortingMenuExp.value,
                            onDismissRequest = { sortingMenuExp.value = false }
                        ) {
                            DropdownMenuItem(text = { Text(text = "Custom") }, onClick = {
                                sortingMenuExp.value = false
                                sortingType.value = SortingType.Custom
                                listCompleteStructures.value = sharedViewModel.RecomputeList(listCompleteStructures.value, sortingType.value, userSkillSub.value.customOrdering)
                            })

                            DropdownMenuItem(text = { Text(text = "Starting Date Asc") }, onClick = {
                                sortingMenuExp.value = false
                                sortingType.value = SortingType.DateAsc
                                listCompleteStructures.value = sharedViewModel.RecomputeList(listCompleteStructures.value, sortingType.value, userSkillSub.value.customOrdering)
                            })
                            DropdownMenuItem(text = { Text(text = "Starting Date Desc") }, onClick = {
                                sortingMenuExp.value = false
                                sortingType.value = SortingType.DateDesc
                                listCompleteStructures.value = sharedViewModel.RecomputeList(listCompleteStructures.value, sortingType.value, userSkillSub.value.customOrdering)
                            })
                        }
                    }

                }



            }

            SkillListBlock(listSkills = listCompleteStructures.value,
                sortingType.value,
                sharedViewModel,
                { index, task ->

                    val updatedList = sharedViewModel.ComputeListTaskAdd1(
                        currentContext,
                        index,
                        task,
                        listCompleteStructures.value.toMutableList()
                    )

                    val mustSkipSection = sharedViewModel.ComputeSkipSection(index, updatedList) && canSkipSection.value
                    if (mustSkipSection) {
                        currentStructureIndex.value = index

                        isStartRun.value = false
                        triggerSectionSkip.value = !triggerSectionSkip.value
                        canSkipSection.value = false
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

                    sharedViewModel.FinishSkill(
                        sharedViewModel,
                        listCompleteStructures,
                        userSkillSub,
                        currentStructureIndex,
                        badgeObtained,
                        isBadgePopUpOpen,
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

                    listCompleteStructures.value = sharedViewModel.RecomputeList(listCompleteStructures.value, sortingType.value, userSkillSub.value.customOrdering)
                },
                { badge ->
                    /*
                    badgeObtained.value = badge
                    isBadgePopUpOpen.value = true

                     */
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

                    sharedViewModel.StopSkillProgressionMS(
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
                    .clip(shape = RoundedCornerShape(10.dp))
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
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
                    Icon(imageVector = Icons.Default.Close, tint = Color.DarkGray, contentDescription = "close")
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 0.dp, 0.dp, 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("CONGRATULATIONS!", fontSize = 30.sp, fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                    )

                    BadgeIcon(badge = badge.badgeColor, size = relative(150.dp))

                    Text("You just obtained a", fontSize = 24.sp,
                        color = Color.DarkGray,
                    )

                    var color : String = "BRONZE MEDAL"

                    if(badge.badgeColor == BadgeColor.SILVER)
                        color = "SILVER MEDAL"

                    if(badge.badgeColor == BadgeColor.GOLD)
                        color = "GOLD MEDAL"

                    Text(text = color, fontSize = 27.sp,
                        color = Color.DarkGray,
                    )

                    Text(badge.badgeName, fontSize = 20.sp,
                        color = Color.DarkGray,
                    )

                    Text(badge.description, fontSize = 20.sp,
                        color = Color.DarkGray,
                    )
                }
            }
        }
    }
}
