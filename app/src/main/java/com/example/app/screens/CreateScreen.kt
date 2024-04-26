package com.example.app.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.additionalUI.BadgeBanner
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.util.SharedViewModel
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldString(value: String, onValueChange: (String) -> Unit, isSingleLine: Boolean){
    val containerColor = Color(0xFFF0F0F0)
    val lineColor = Color(0xFFDEDEDE)

    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        singleLine = isSingleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            unfocusedIndicatorColor = containerColor,
            focusedIndicatorColor = lineColor,
        ),
        trailingIcon = {
            IconButton(onClick = { keyboardController?.hide() }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardHide,
                    contentDescription = "Close keyboard"
                )
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldInt(value: Int, onValueChange: (String) -> Unit){
    val containerColor = Color(0xFFF0F0F0)
    val lineColor = Color(0xFFDEDEDE)

    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(value =

        if(value != 0){
            value.toString()
        }else{
            ""
        },
        onValueChange = {
            try{
                val value = it.toLong()
                if(value < Int.MAX_VALUE && value > 0){
                    onValueChange(it)
                }
            } catch (e: NumberFormatException){
                if(it == "") onValueChange((0).toString())
            }

        },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            unfocusedIndicatorColor = containerColor,
            focusedIndicatorColor = lineColor,
        ),
        trailingIcon = {
            IconButton(onClick = { keyboardController?.hide() }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardHide,
                    contentDescription = "Close keyboard"
                )
            }
        }
    )
}

@Composable
fun GeneralInfoBox(skill:SkillModel, onTitleChange: (String) -> Unit, onDescriptionChange: (String) -> Unit){

    Box(modifier = Modifier
        .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(10.dp))
        .padding(15.dp)
    ){
        Column {
            Row (verticalAlignment = Alignment.CenterVertically)
            {

                Text(text = "Title:",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(2.dp))


                Box(
                    modifier = Modifier
                        .background(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(10))
                ){
                    TextFieldString(
                        value = skill.titleSkill,
                        onValueChange = onTitleChange,
                        isSingleLine = true
                    )
                }
            }

            Text(text = "Description:",
                fontSize = 15.sp,
                modifier = Modifier.padding(2.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
            ){
                TextFieldString(value = skill.skillDescription, onValueChange = onDescriptionChange, false)
            }
        }
    }
}

@Composable
fun SectionBox(id:Int, section:SkillSectionModel,
               listTasks: List<SkillTaskModel>,
               badge: BadgeDataModel?,
               onTitleChange: (String) -> Unit,
               onDescriptionChange: (String) -> Unit,
               onAddTask: (Int) -> Unit,
               onDeleteSection: (Int) -> Unit,
               onChangeTaskDescription: (Int, String) -> Unit,
               onChangeTaskAmount: (Int, Int) -> Unit,
               onDeleteTask: (String, Int) -> Unit,
               onAddBadgeProcess: (String) -> Unit) {

    Box(modifier = Modifier
        .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(10.dp))
        .padding(15.dp)
    ){
        Column{
            
            Button(onClick = { onAddBadgeProcess(id.toString())  }) {
                Text(text = "Add Badge")
            }

            if(badge != null){
                Text(text = badge.badgeName)
            }
            
            Row (verticalAlignment = Alignment.CenterVertically)
            {

                Text(text = "Title section $id:",
                    fontSize = 19.sp,
                    modifier = Modifier.padding(2.dp))


                Box(
                    modifier = Modifier
                        .background(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(10))
                ){
                    TextFieldString(
                        value = section.titleSection,
                        onValueChange = onTitleChange,
                        isSingleLine = true
                    )
                }
            }

            Text(text = "Description:",
                fontSize = 15.sp,
                modifier = Modifier.padding(2.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
            ){
                TextFieldString(value = section.descriptionSection, onValueChange = onDescriptionChange, false)
            }

            Spacer(modifier = Modifier
                .size(400.dp, 15.dp)
            )

            listTasks.forEachIndexed{index, task ->

                Button(onClick = {onAddTask(index)}) {
                    Text(text = "ADD TASK +")
                }

                Spacer(modifier = Modifier
                    .height(10.dp)
                )
                
                TaskBox(id = index, task = task, onDescriptionChange = { onChangeTaskDescription(index, it) }, onAmountChange = { onChangeTaskAmount(index, it.toInt()) }, {
                    onDeleteTask(section.id, it)
                }
                    )

                Spacer(modifier = Modifier
                    .height(10.dp)
                )

            }

            Button(onClick = {onAddTask(listTasks.size)}) {
                Text(text = "ADD TASK +")
            }

            Spacer(modifier = Modifier
                .height(10.dp)
            )

            Button(onClick = {onDeleteSection(id)}) {
                Text(text = "DELETE SECTION -")
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TaskBox(id:Int, task:SkillTaskModel, onDescriptionChange: (String) -> Unit, onAmountChange: (String) -> Unit, onDeleteTask: (Int) -> Unit){

    Box(modifier = Modifier
        .background(color = Color(0xFFD3E9FF), shape = RoundedCornerShape(10.dp))
        .padding(15.dp)
    ){
        Column {
            Text(text = "Description task $id:",
                fontSize = 18.sp,
                modifier = Modifier.padding(2.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
            ){
                TextFieldString(task.taskDescription, onDescriptionChange, false)
            }

            Text(text = "Required amount:",
                fontSize = 15.sp,
                modifier = Modifier.padding(5.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
            ){
                val containerColor = Color(0xFFF0F0F0)
                TextFieldInt(task.requiredAmount, onAmountChange)
            }

            Button(onClick = {onDeleteTask(id)}) {
                Text(text = "DELETE TASK -")
            }
        }
    }
}

@Composable
fun BadgePopUp(sharedViewModel: SharedViewModel, badge: BadgeDataModel, onBadgeNameChange: (String) -> Unit, onBadgeDescriptionChange: (String) -> Unit, onAddBadge: () -> Unit, onCloseClick: () -> Unit) {

    val context = LocalContext.current


    var listBadges: MutableState<List<BadgeDataModel>> = remember {
        mutableStateOf(listOf())
    }

    LaunchedEffect(sharedViewModel.getCurrentUserMail()) {
        sharedViewModel.retrieveUserSkillSub(
            sharedViewModel.getCurrentUserMail(),
            context
        ){userSkillSub ->
            sharedViewModel.retrieveAllBadges(
                userSkillSub.createdBadges,
                context
            ){
                listBadges.value = it
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

        Dialog(
            onDismissRequest = onCloseClick,
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
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(text = "TITLE")

                    TextFieldString(value = badge.badgeName, onValueChange = onBadgeNameChange, isSingleLine = true)

                    Text(text = "DECRIPTION")

                    TextFieldString(
                        value = badge.description,
                        onValueChange = onBadgeDescriptionChange,
                        isSingleLine = false
                    )

                    Button(onClick = {onAddBadge()}) {
                        Text(text = "ADD +")
                    }

                    Toast.makeText(context, listBadges.value.count().toString(), Toast.LENGTH_SHORT).show()
                    listBadges.value.forEach{
                        Box(){
                            Text(text = it.badgeName)

                        }
                    }

                }
            }
        }
    }


}


fun GenerateNewSkillID(): String{
    val db = FirebaseFirestore.getInstance()
    val newSkillRef = db.collection("skill").document()
    return newSkillRef.id
}

fun SaveEverything(refId: String, sharedViewModel: SharedViewModel, context: Context, skill: SkillModel, sections: List<SkillSectionModel>, tasks: Map<String, List<SkillTaskModel>>, badgeList: Map<String, BadgeDataModel>){

    val db = FirebaseFirestore.getInstance()
    val skillRef = db.collection("skill").document(refId)
    skillRef.set(skill.copy(creatorEmail = sharedViewModel.getCurrentUserMail(), skillSectionsList = sections.map{
        it.id
    }))

    badgeList.forEach{entry ->

        sharedViewModel.saveBadgeData(entry.value, context)

    }

    sections.forEach{

        val taskList = (tasks[it.id]?.toMutableList() ?: mutableListOf()).map{
            it.id
        }

        val badgeID = badgeList.getOrDefault(it.id, BadgeDataModel())
        val hasBadge = badgeID.sectionId == it.id

        sharedViewModel.saveSkillSection(it.copy(skillTasksList = taskList, badgeID = badgeID.skillId + badgeID.sectionId, hasBadge = hasBadge), context)
    }

    tasks.values.flatten().forEach{
        sharedViewModel.saveSkillTask(it, context)
    }


    sharedViewModel.retrieveUserSkillSub(sharedViewModel.getCurrentUserMail(), context){

        Toast.makeText(context, it.createdSkillsId.count().toString(), Toast.LENGTH_SHORT).show()

        val createdSkillsIdList = it.createdSkillsId + skill.id
        val createdBadgesIdList = it.createdBadges + badgeList.values.filter { it != BadgeDataModel() }.map { it.skillId + it.sectionId }
        sharedViewModel.updateUserSub(it.copy(createdSkillsId = createdSkillsIdList, createdBadges = createdBadgesIdList), context)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    openDialog: MutableState<Boolean>,
    pendingRoute: MutableState<String?>
){

    val skillID by remember {
        mutableStateOf(GenerateNewSkillID())
    }

    var skill : MutableState<SkillModel> = remember {
        mutableStateOf(SkillModel(id = skillID))
    }

    var skillSections: MutableState<List<SkillSectionModel>> = remember {
        mutableStateOf(mutableListOf())
    }

    var skillTasks: MutableState<Map<String, MutableList<SkillTaskModel>>> = remember {
        mutableStateOf(mapOf())
    }

    var badges: MutableState<MutableMap<String, BadgeDataModel>> = remember {
        mutableStateOf(mutableMapOf())
    }

    var sectionIdCounter: MutableState<Int> = remember {
        mutableStateOf(0)
    }

    var taskIdCounter: MutableState<Int> = remember {
        mutableStateOf(0)
    }

    var currentBadge: MutableState<BadgeDataModel> = remember {
        mutableStateOf(BadgeDataModel())
    }

    var currentSectionBadge: MutableState<String> = remember {
        mutableStateOf("")
    }

    var isBadgeEditActive: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current;


    Scaffold(
        topBar = { AppToolBar(title = "Create a new Skill", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController, openDialog, pendingRoute)
        }
    ){innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {

                item {
                    //GeneralInfoBox(sharedViewModel, title = title, {title = it}, description, {description = it})
                    GeneralInfoBox(skill.value, {skill.value = skill.value.copy(titleSkill = it)}, {skill.value = skill.value.copy(skillDescription = it)})
                }

                itemsIndexed(skillSections.value){index, section ->
                    Button(onClick = {

                        var updatedSkillSections = skillSections.value.toMutableList()
                        updatedSkillSections.add(index, SkillSectionModel(id = sectionIdCounter.value.toString(), idSkill = skillID, titleSection = "", skillTasksList = mutableListOf()))

                        skillSections.value = updatedSkillSections
                        sectionIdCounter.value += 1
                    }) {
                        Text(text = "ADD SECTION +")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    SectionBox(id = index, section = section, skillTasks.value.get(section.id)?.toList() ?: mutableListOf(),
                        badges.value.get(index.toString()),
                        {skillSections.value = skillSections.value.toMutableList().apply {
                            set(index, section.copy(titleSection = it))
                        }
                        },
                        {skillSections.value = skillSections.value.toMutableList().apply {
                            set(index, section.copy(descriptionSection = it))
                        }
                        },
                        {
                            val updatedList = skillTasks.value[section.id]?.toMutableList() ?: mutableListOf()
                            updatedList.add(it, SkillTaskModel(taskIdCounter.value.toString(), section.id, skillID, "", 0))

                            taskIdCounter.value += 1;

                            val updatedMap = skillTasks.value.toMutableMap()
                            updatedMap[section.id] = updatedList

                            skillTasks.value = updatedMap
                        },
                        {
                            skillTasks.value = skillTasks.value - skillSections.value.get(it).id

                            skillSections.value = skillSections.value - skillSections.value.get(it)

                        },
                        {id, description ->
                            val updatedTask = skillTasks.value[section.id]?.get(id)?.copy(taskDescription = description) ?: SkillTaskModel()

                            val updatedList = skillTasks.value[section.id]?.toMutableList() ?: mutableListOf()
                            updatedList.set(id, updatedTask)

                            val updatedMap = skillTasks.value.toMutableMap()
                            updatedMap[section.id] = updatedList

                            skillTasks.value = updatedMap
                        },
                        {id, amount ->
                            val updatedTask = skillTasks.value[section.id]?.get(id)?.copy(requiredAmount = amount) ?: SkillTaskModel()

                            val updatedList = skillTasks.value[section.id]?.toMutableList() ?: mutableListOf()
                            updatedList.set(id, updatedTask)

                            val updatedMap = skillTasks.value.toMutableMap()
                            updatedMap[section.id] = updatedList

                            skillTasks.value = updatedMap
                        },
                        {idSection, idTask ->
                            val updatedListTasks = skillTasks.value.get(idSection)
                                ?.minus(skillTasks.value.get(idSection)!!.get(idTask))
                            var updatedMapTasks = skillTasks.value.toMutableMap()
                            updatedMapTasks.set(idSection, updatedListTasks!!.toMutableList())

                            skillTasks.value = updatedMapTasks
                        },
                        {currentBadgeSection ->
                            currentSectionBadge.value = currentBadgeSection
                            currentBadge.value = currentBadge.value.copy(sectionId = currentBadgeSection, skillId = skillID, badgeCreator = sharedViewModel.getCurrentUserMail())
                            isBadgeEditActive.value = true
                        }
                    )


                }

                item {
                    //TaskBox(1, SkillTaskModel("ddee", "dddd", skillID, "assad dadad dedurica", 2), {}, {})

                    Button(onClick = {
                        skillSections.value = skillSections.value + SkillSectionModel(id = sectionIdCounter.value.toString(), idSkill = skillID, titleSection = "", skillTasksList = mutableListOf())
                        sectionIdCounter.value += 1
                    }) {
                        Text(text = "ADD SECTION +")
                    }

                    Button(onClick = {

                        if(skillSections.value.isEmpty()){
                            Toast.makeText(context, "THERE ARE NO SECTIONS", Toast.LENGTH_SHORT).show()
                            return@Button
                        }else if(skillTasks.value.any { it.value.isEmpty() } || skillSections.value.any{ !skillTasks.value.keys.contains(it.id) }){
                            Toast.makeText(context, "THERE IS A SECTION WITHOUT ANY TASKS", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        SaveEverything(skillID, sharedViewModel, context, skill.value, skillSections.value, skillTasks.value, badges.value)

                        navController.navigate("MySkills")
                    }) {
                        Text(text = "SAVE")
                    }
                }

            }
        }


        if(isBadgeEditActive.value){

            currentBadge.value =
                if(currentBadge.value == BadgeDataModel()) badges.value.getOrDefault(currentSectionBadge.value, BadgeDataModel())
                else currentBadge.value

            BadgePopUp(
                sharedViewModel,
                currentBadge.value,
                {
                    currentBadge.value = currentBadge.value.copy(badgeName = it)
                },
                {
                    currentBadge.value = currentBadge.value.copy(description = it)
                },
                {
                    badges.value.put(currentSectionBadge.value, currentBadge.value)
                    currentBadge.value = BadgeDataModel()
                    isBadgeEditActive.value = false
                },
                {
                    currentBadge.value = BadgeDataModel()
                    isBadgeEditActive.value = false
                },
            )

        }

    }



    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}