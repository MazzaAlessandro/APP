package com.example.app.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.additionalUI.BadgeBanner
import com.example.app.additionalUI.BadgeColor
import com.example.app.additionalUI.BadgeIcon
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserDataModel
import com.example.app.ui.theme.Bronze
import com.example.app.ui.theme.greenColor
import com.example.app.ui.theme.redColor
import com.example.app.ui.theme.yellowColor
import com.example.app.util.SharedViewModel
import com.example.app.util.relative
import com.google.firebase.firestore.FirebaseFirestore
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
fun GeneralInfoBox(skill:SkillModel, onTitleChange: (String) -> Unit, onDescriptionChange: (String) -> Unit, onCheckChange: (Boolean) -> Unit){

    Box(modifier = Modifier
        .padding(15.dp, 10.dp)
    ){
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
                    text = "General Information",
                    fontSize = 20.sp
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = Color.Black,
                    thickness = 1.dp
                )
            }
            Row (verticalAlignment = Alignment.CenterVertically)
            {

                Text(text = "Title:",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(2.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    label = { Text(text = "Enter a Title") },
                    value = skill.titleSkill,
                    onValueChange = onTitleChange,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1
                )

                /*Box(
                    modifier = Modifier
                        .background(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(10))
                ){
                    TextFieldString(
                        value = skill.titleSkill,
                        onValueChange = onTitleChange,
                        isSingleLine = true
                    )
                }*/
            }

            Spacer(modifier = Modifier.size(relative(size = 10.dp)))
            
            Text(text = "Description:",
                fontSize = 15.sp,
                modifier = Modifier.padding(2.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                label = { Text(text = "Enter a Description") },
                value = skill.skillDescription,
                onValueChange = onDescriptionChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = false
            )
            /*Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
            ){
                TextFieldString(value = skill.skillDescription, onValueChange = onDescriptionChange, false)
            }*/
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = skill.isPublic, onCheckedChange = onCheckChange)
                Text(text = "Public Skill")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionBox(id:Int,
               section:SkillSectionModel,
               badge:BadgeDataModel,
               listTasks: List<SkillTaskModel>,
               onTitleChange: (String) -> Unit,
               onDescriptionChange: (String) -> Unit,
               onAddTask: (Int) -> Unit,
               onDeleteSection: (Int) -> Unit,
               onChangeTaskDescription: (Int, String) -> Unit,
               onChangeTaskAmount: (Int, Int) -> Unit,
               onDeleteTask: (String, Int) -> Unit,
               onBadgeUpdate: (BadgeColor) -> Unit) {

    var done: MutableState<Boolean> = remember{mutableStateOf(if(section.hasBadge) badge.done else false)}
    var filled: MutableState<Boolean> = remember { mutableStateOf(section.hasBadge) }
    var color: MutableState<BadgeColor> = remember { mutableStateOf(if(section.hasBadge) badge.badgeColor else BadgeColor.BRONZE) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .background(color = Color.Gray.copy(0.25f), shape = RoundedCornerShape(10.dp))
    ){
        /*IconButton(
            onClick = {
                onDeleteSection(id)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(relative(size = 50.dp))
                .zIndex(11F),
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "close", tint = redColor)
        }*/

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            
            /*Button(onClick = { onAddBadgeProcess(id.toString())  }) {
                Text(text = "Add Badge")
            }

            if(badge != null){

                BadgeBanner(
                    badge = badge.badgeColor,
                    skillName = badge.badgeName + " - " + "Skill name",
                    description = badge.description,
                    date = "01/20/5201"
                ) {

                }

            }*/
            val i = id + 1;
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Section $i",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){

                var expanded by remember { mutableStateOf(false) }

                Box(modifier = Modifier.clickable { /*onAddBadgeProcess(id.toString())*/ expanded = !expanded }){
                    Icon(modifier = Modifier
                        .align(Alignment.TopEnd)
                        .zIndex(11F),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "badge")

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ){
                        BadgeColor.values().forEachIndexed { index, badgeColor ->

                            DropdownMenuItem(text = { Text(badgeColor.toString()) }, onClick = {
                                color.value = badgeColor
                                expanded = false
                                filled.value = true
                                onBadgeUpdate(badgeColor)
                            }
                            )

                        }
                    }


                    BadgeIcon(badge = color.value, size = relative(100.dp), filled.value)

                }

                Column {
                    Text(text = "Name:",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(2.dp, 2.dp, 2.dp, 0.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp, 0.dp, 2.dp, 2.dp),
                        label = { Text(text = "Enter a Name") },
                        value = section.titleSection,
                        onValueChange = onTitleChange,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        enabled = !done.value,
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White)
                    )

                    Text(text = "Description:",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(2.dp, 2.dp, 2.dp, 0.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp, 0.dp, 2.dp, 2.dp),
                        label = { Text(text = "Enter a Description") },
                        value = section.descriptionSection,
                        onValueChange = onDescriptionChange,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        enabled = !done.value,
                        singleLine = false,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Tasks", fontSize = 20.sp)

                var num = listTasks.size

                Text(
                    text = "$num element" + if(num > 1) "s" else "",
                    fontSize = 15.sp,
                    color = Color.Gray
                )

                IconButton(onClick = { onAddTask(num) }, enabled = !done.value) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "AddTask", tint = greenColor)
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 2.dp),
                color = Color.Black
            )

            if(listTasks.isEmpty()){
                Text(
                    "You need at least one task",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
            else{
                listTasks.forEachIndexed{index, task ->
                    TaskBox(id = index,
                        task = task,
                        enabled = !done.value,
                        onDescriptionChange = { onChangeTaskDescription(index, it) },
                        onAmountChange = { onChangeTaskAmount(index, it.toInt()) },
                        onDeleteTask = {
                            onDeleteTask(section.id, it)
                        }
                    )

                }
            }

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround){

                Button(onClick = {onDeleteSection(id)},
                    colors = ButtonDefaults.buttonColors(containerColor = redColor)) {
                    Row{
                        Text(text = "DELETE", modifier = Modifier.padding(2.dp))
                        Icon(imageVector = Icons.Default.Close, contentDescription = "delete")
                    }
                }

                if(done.value){
                    Button(onClick = {done.value = false},
                        colors = ButtonDefaults.buttonColors(containerColor = yellowColor)) {
                        Row{
                            Text(text = "EDIT", modifier = Modifier.padding(2.dp))
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
                else{
                    Button(onClick = {done.value = true},
                        colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                        enabled = !(section.titleSection.isBlank() ||
                                section.descriptionSection.isBlank() ||
                                listTasks.isEmpty() ||
                                listTasks.any { it.taskDescription.isBlank() || it.requiredAmount == 0 })) {
                        Row{
                            Text(text = "DONE", modifier = Modifier.padding(2.dp))
                            Icon(imageVector = Icons.Default.Done, contentDescription = "done")
                        }
                    }
                }
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TaskBox(id:Int, task:SkillTaskModel, onDescriptionChange: (String) -> Unit, enabled : Boolean, onAmountChange: (String) -> Unit, onDeleteTask: (Int) -> Unit){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            OutlinedTextField(
                modifier = Modifier
                    .padding(2.dp)
                    .weight(4f, true),
                label = { Text(text = "Task Description") },
                value = task.taskDescription,
                onValueChange = onDescriptionChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                maxLines = 1,
                singleLine = true,
                enabled = enabled,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1.5f),
                label = { Text(text = "Times") },
                placeholder = { Text(text = "0") },
                value = if (task.requiredAmount != 0) task.requiredAmount.toString() else "",
                onValueChange = {
                    try{
                        val value = it.toLong()
                        if(value < Int.MAX_VALUE && value > 0){
                            onAmountChange(it)
                        }
                    } catch (e: NumberFormatException){
                        if(it == "") onAmountChange((0).toString())
                    }

                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                maxLines = 1,
                singleLine = true,
                enabled = enabled,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                )
            )

            IconButton(modifier = Modifier.weight(0.5f),
                onClick = { onDeleteTask(id) },
                enabled = enabled,) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "DeleteTask", tint = redColor)
            }


        /*Column {
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
        }*/
    }
}

@Composable
fun BadgePopUp(sharedViewModel: SharedViewModel, badge: BadgeDataModel, onBadgeNameChange: (String) -> Unit, onBadgeDescriptionChange: (String) -> Unit, onColorChange:(BadgeColor) -> Unit, onAddBadge: () -> Unit, onCloseClick: () -> Unit) {

    val context = LocalContext.current


    var listBadges: MutableState<List<BadgeDataModel>> = remember {
        mutableStateOf(listOf())
    }

    var isExpanded: MutableState<Boolean> = remember{
        mutableStateOf(true)
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
                    .fillMaxWidth()
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

                    DropdownMenu(expanded = isExpanded.value, onDismissRequest = { isExpanded.value = false }) {

                        BadgeColor.values().forEachIndexed { index, badgeColor ->
                            
                            DropdownMenuItem(text = { Text(badgeColor.toString()) }, onClick = {
                                isExpanded.value = false
                                onColorChange(badgeColor) }
                            )

                        }


                    }
                    
                    TextFieldString(value = badge.badgeName, onValueChange = onBadgeNameChange, isSingleLine = true)

                    Text(text = "DESCRIPTION")

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
                        
                        BadgeBanner(
                            badge = it.badgeColor,
                            skillName = it.badgeName,
                            description = it.description,
                            date = ""
                        ) {
                            onBadgeNameChange(it.badgeName)
                            onBadgeDescriptionChange(it.description)
                            onAddBadge()
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

@RequiresApi(Build.VERSION_CODES.O)
fun SaveEverything(refId: String, sharedViewModel: SharedViewModel, context: Context, user: UserDataModel, skill: SkillModel, sections: List<SkillSectionModel>, tasks: Map<String, List<SkillTaskModel>>, badgeList: Map<String, BadgeDataModel>){

    val db = FirebaseFirestore.getInstance()
    val skillRef = db.collection("skill").document(refId)
    skillRef.set(skill.copy(creatorEmail = sharedViewModel.getCurrentUserMail(),
        skillDescription = skill.skillDescription.trim(),
        titleSkill = skill.titleSkill.trim(),
        dateTime = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        creatorUserName = user.username,
        skillSectionsList = sections.map{
        it.id
    }))

    badgeList.forEach{entry ->

        val section = sections.find { it.id == entry.key }

        val badgeEditted = entry.value.copy(
            badgeName = skill.titleSkill.trim() + " - " + section!!.titleSection.trim(),
            description = section!!.descriptionSection.trim()
        )

        sharedViewModel.saveBadgeData(badgeEditted, context)

    }

    sections.forEach{

        val taskList = (tasks[it.id]?.toMutableList() ?: mutableListOf()).map{
            it.id
        }

        val badgeID = badgeList.getOrDefault(it.id, BadgeDataModel())
        val hasBadge = badgeID.sectionId == it.id

        sharedViewModel.saveSkillSection(it.copy(skillTasksList = taskList, titleSection = it.titleSection.trim(), descriptionSection = it.descriptionSection.trim(), badgeID = badgeID.skillId + badgeID.sectionId, hasBadge = hasBadge), context)
    }

    tasks.values.flatten().forEach{
        sharedViewModel.saveSkillTask(it.copy(taskDescription = it.taskDescription.trim()), context)
    }


    sharedViewModel.retrieveUserSkillSub(sharedViewModel.getCurrentUserMail(), context){

        Toast.makeText(context, it.createdSkillsId.count().toString(), Toast.LENGTH_SHORT).show()

        val createdSkillsIdList = it.createdSkillsId + skill.id
        val createdBadgesIdList = it.createdBadges + badgeList.values.filter { it != BadgeDataModel() }.map { it.skillId + it.sectionId }
        sharedViewModel.updateUserSub(it.copy(createdSkillsId = createdSkillsIdList, createdBadges = createdBadgesIdList), context)
    }

}


@RequiresApi(Build.VERSION_CODES.O)
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

    val currentUser: MutableState<UserDataModel> = remember {
        mutableStateOf(UserDataModel())
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

    val context = LocalContext.current

    //LOAD USER DATA IN MEMORY
    LaunchedEffect(sharedViewModel.getCurrentUserMail()) {
        sharedViewModel.retrieveUserData(
            sharedViewModel.getCurrentUserMail(),
            context,
        ){
            currentUser.value = it
        }
    }


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
                .testTag("CreateScreen")
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {

                item {
                    //GeneralInfoBox(sharedViewModel, title = title, {title = it}, description, {description = it})
                    GeneralInfoBox(skill.value,
                        {skill.value = skill.value.copy(titleSkill = it)
                        },
                        {skill.value = skill.value.copy(skillDescription = it)},
                        {skill.value = skill.value.copy(isPublic = it)}
                    )
                }

                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Sections", fontSize = 20.sp)

                        var num = skillSections.value.size

                        Text(
                            text = "$num element" + if(num > 1) "s" else "",
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 2.dp),
                        color = Color.Black
                    )
                    /*Row(
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
                            text = "Skill Sections",
                            fontSize = 20.sp
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            color = Color.Black,
                            thickness = 1.dp
                        )
                    }*/
                }

                if(skillSections.value.isEmpty()){
                    item {
                        Column(modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(relative(10.dp))) {

                            Text("You need at least one section to create a Skill",
                                fontSize = 18.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center)

                            Button(onClick = {

                                var updatedSkillSections = skillSections.value.toMutableList()
                                updatedSkillSections.add(0, SkillSectionModel(id = sectionIdCounter.value.toString(), idSkill = skillID, titleSection = "", skillTasksList = mutableListOf()))

                                skillSections.value = updatedSkillSections
                                sectionIdCounter.value += 1
                            }) {
                                Text(text = "ADD A SECTION")
                            }
                        }
                    }
                }
                else{
                    itemsIndexed(skillSections.value){index, section ->
                        /*Button(onClick = {

                            var updatedSkillSections = skillSections.value.toMutableList()
                            updatedSkillSections.add(index, SkillSectionModel(id = sectionIdCounter.value.toString(), idSkill = skillID, titleSection = "", skillTasksList = mutableListOf()))

                            skillSections.value = updatedSkillSections
                            sectionIdCounter.value += 1
                        }) {
                            Text(text = "ADD SECTION +")
                        }

                        Spacer(modifier = Modifier.height(16.dp))*/

                        SectionBox(id = index,
                            section = section,
                            badges.value.get(section.id) ?: BadgeDataModel(),
                            skillTasks.value.get(section.id)?.toList() ?: mutableListOf(),
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

                                taskIdCounter.value += 1

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
                            {currentBadge ->
                                //badges.value.get(index.toString())

                                var updatable = skillSections.value.toMutableList()
                                val skillSect = skillSections.value.get(index).copy(hasBadge = true)
                                updatable.set(index, skillSect)

                                skillSections.value = updatable

                                badges.value.put(section.id,
                                    BadgeDataModel(currentBadge,
                                        sharedViewModel.getCurrentUsername(),
                                        skill.value.titleSkill + " - " + section.descriptionSection,
                                        skill.value.id,
                                        section.id,
                                        section.descriptionSection))
                            }
                        )


                    }

                    item{
                        Button(onClick = {
                            skillSections.value = skillSections.value + SkillSectionModel(id = sectionIdCounter.value.toString(), idSkill = skillID, titleSection = "", skillTasksList = mutableListOf())
                            sectionIdCounter.value += 1
                        }) {
                            Text(text = "ADD A SECTION")
                        }
                    }
                }



                item {
                    //TaskBox(1, SkillTaskModel("ddee", "dddd", skillID, "assad dadad dedurica", 2), {}, {})

                    /*Button(onClick = {
                        skillSections.value = skillSections.value + SkillSectionModel(id = sectionIdCounter.value.toString(), idSkill = skillID, titleSection = "", skillTasksList = mutableListOf())
                        sectionIdCounter.value += 1
                    }) {
                        Text(text = "ADD SECTION +")
                    }*/

                    Button(onClick = {

                        if(skillSections.value.isEmpty()){
                            Toast.makeText(context, "THERE ARE NO SECTIONS", Toast.LENGTH_SHORT).show()
                            return@Button
                        }else if(skillTasks.value.any { it.value.isEmpty() } || skillSections.value.any{ !skillTasks.value.keys.contains(it.id) }){
                            Toast.makeText(context, "THERE IS A SECTION WITHOUT ANY TASKS", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        SaveEverything(skillID, sharedViewModel, context, currentUser.value, skill.value, skillSections.value, skillTasks.value, badges.value)

                        navController.navigate("Search")
                    },
                        colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                        enabled = !(skillSections.value.isEmpty() ||
                                skillTasks.value.any { it.value.isEmpty() ||
                                        it.value.any { it.taskDescription.isBlank() || it.requiredAmount == 0 } } ||
                                skillSections.value.any{ !skillTasks.value.keys.contains(it.id) } ||
                                skill.value.skillDescription.isBlank() ||
                                skill.value.titleSkill.isBlank()
                                )) {
                        Text(text = "DONE")
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
                    currentBadge.value = currentBadge.value.copy(badgeColor = it)
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