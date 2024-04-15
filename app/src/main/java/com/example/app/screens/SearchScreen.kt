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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import com.example.app.util.SharedViewModel


//TODO PROBLEMS WITH PROG SAVE

//TODO DEFINE A SEARCH OBJECT UI
//TODO PUT A BUTTON TO ENROLL TO THAT SKILL
//TODO THINK ABOUT THE ACTIONS THAT SHOULD BE DONE WHEN CLICKING ON A SKILL

//TODO CHANGE THE ON ADD PROGRESSION SINCE THINGS ARE ALREADY COMPUTED

//TODO SAVE FROM CREATE AND DELETE ON SAVE SYSTEM PROPERTIES


//TODO CALLS VIEWMODEL

enum class SelectedSkillState{
    NOT_SELECTED, NEW_SELECTED, REG_SELECTED
}

@Composable
fun SectionElementBlock(section: SkillSectionModel, amount: Int, required: Int, sharedViewModel: SharedViewModel){

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp),
        contentAlignment = Alignment.Center
    ){
        Box(modifier = Modifier
            .fillMaxSize()
        ){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(10)))
            Box(modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFFD3E9FF), RoundedCornerShape(10))
                .fillMaxWidth(amount.toFloat() / required.toFloat()))
        }

        Row (modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically){
            Text(text = section.titleSection,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.0f))

            Text(text = amount.toString() + "/" + required.toString(),
                modifier = Modifier.padding(horizontal = 20.dp))
        }
    }

}

@Composable
fun SkillSearchBlock(skill: SkillModel, isInProgress: Boolean, onClick: () -> Unit){
    val colorCircle = MaterialTheme.colorScheme.primary;
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0XFFF0F0F0),
                RoundedCornerShape(10)
            ) // Use the color of the background in your image
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .clickable { onClick() }
        ,

        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp) // Set the size of the circle
                .background(colorCircle, shape = CircleShape) // Use the color of the circle in your image
        )
        Spacer(Modifier.width(25.dp)) // Space between the circle and the text
        Column(modifier = Modifier.weight(1f)) {
            Text(skill.titleSkill, fontSize = 25.sp)

            if(isInProgress){
                Text(text = "Already in progress", fontSize = 12.sp)
            }else{
                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    ) {
                    Text(text = "Not started", fontSize = 12.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1.0f))

                    val sectionAmount = skill.skillSectionsList.size

                    Text(text = sectionAmount.toString() + " section" + if(sectionAmount > 1) "s" else "", fontSize = 12.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1.0f))
                }
            }
        }
    }
}


@Composable
fun SkillInfoPopUp_STARTED(sharedViewModel: SharedViewModel, skill: SkillModel, skillProgression: SkillProgressionModel, onCloseClick: () -> Unit) {
    val currentContext = LocalContext.current

    var sectionsList: MutableState<List<SkillSectionModel>> = remember {
        mutableStateOf(emptyList())
    }

    var tasksMap: MutableState<Map<String, List<SkillTaskModel>>> = remember {
        mutableStateOf(emptyMap())
    }

    var completeStructure: MutableState<SkillCompleteStructureModel> = remember { mutableStateOf(SkillCompleteStructureModel(
        SkillProgressionModel(), SkillModel(), SkillSectionModel(), emptyMap())
    )}

    LaunchedEffect(skill) {
        sharedViewModel.retrieveAllSkillSection(skill.id, currentContext) { sectionModels ->
            sectionsList.value = sectionModels.sortedWith{a, b ->
                if(skill.skillSectionsList.indexOf(a.id) < skill.skillSectionsList.indexOf(b.id)) -1 else 1
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
                        sectionsList.value?.find { it.id == skillProgression.currentSectionId } ?: sectionsList.value.get(0),
                        tasksMap.value.get(skillProgression.currentSectionId)?.associate {


                            var progressionNumer: Int = if (it.id in skillProgression.mapNonCompletedTasks.keys) skillProgression.mapNonCompletedTasks?.get(it.id) ?: 0 else it.requiredAmount

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
    ){
        Popup(
            alignment = Alignment.Center,

            properties = PopupProperties(
                excludeFromSystemGesture = true,
            ),

            // to dismiss on click outside
            onDismissRequest = { onCloseClick() }
        ){
            Box(
                Modifier
                    .width(375.dp)
                    .height(525.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState()),
            ) {
                IconButton(
                    onClick = {
                        onCloseClick()
                    },
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {


                    SkillTitleBlock(completeStructure.value)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
                    ){
                        Text(text = skill.skillDescription,
                            modifier = Modifier.padding(15.dp),
                            fontSize = 12.sp,
                            color = Color.Black)
                    }

                    sectionsList.value.forEach{section ->
                        val indexOfCurrent = skill.skillSectionsList.indexOf(section.id)
                        val indexOfProg = skill.skillSectionsList.indexOf(skillProgression.currentSectionId)

                        if (indexOfCurrent < indexOfProg){
                            SectionElementBlock(section, 1, 1, sharedViewModel)
                        }else if(indexOfCurrent == indexOfProg){


                            SectionElementBlock(section, 1, 2, sharedViewModel)

                        }else{
                            SectionElementBlock(
                                section = section,
                                amount = 0,
                                required = 1,
                                sharedViewModel = sharedViewModel
                            )
                        }
                    }

                }
            }
        }
    }

}

@Composable
fun SkillInfoPopUp_UNSTARTED(skill: SkillModel, sharedViewModel: SharedViewModel, onAddProgression: () -> Unit, onCloseClick: () -> Unit) {
    val currentContext = LocalContext.current

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
    ){
        Popup(
            alignment = Alignment.Center,

            properties = PopupProperties(
                excludeFromSystemGesture = true,
            ),

            // to dismiss on click outside
            onDismissRequest = { onCloseClick() }
        ){
            Box(
                Modifier
                    .width(375.dp)
                    .height(525.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState()),
            ) {
                IconButton(
                    onClick = {
                        onCloseClick()
                    },
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {


                    val colorCircle = MaterialTheme.colorScheme.primary;
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0XFFF0F0F0),
                                RoundedCornerShape(10)
                            ) // Use the color of the background in your image
                            .padding(horizontal = 20.dp, vertical = 15.dp),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp) // Set the size of the circle
                                .background(colorCircle, shape = CircleShape) // Use the color of the circle in your image
                        )
                        Spacer(Modifier.width(25.dp)) // Space between the circle and the text
                        Column(modifier = Modifier.weight(1f)) {
                            Text(skill.titleSkill, fontSize = 25.sp)
                            Text(skill.skillSectionsList.count().toString() + " sections",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .background(color = colorCircle, RoundedCornerShape(20))
                                    .padding(vertical = 5.dp, horizontal = 7.dp)
                            )
                        }
                        Text(text = "02/11/2024", fontSize = 20.sp)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
                    ){
                        Text(text = skill.skillDescription,
                            modifier = Modifier.padding(15.dp),
                            fontSize = 12.sp,
                            color = Color.Black)
                    }

                    sectionsList.value.forEach{section ->
                        SectionElementBlock(
                            section = section,
                            amount = 0,
                            required = 1,
                            sharedViewModel = sharedViewModel
                        )
                    }
                    
                    Button(onClick = { onAddProgression() }) {
                        Text(text = "START SKILL +")
                    }

                }
            }
        }
    }
    
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController,
                 sharedViewModel: SharedViewModel,
                 openDialog: MutableState<Boolean>,
                 pendingRoute: MutableState<String?>
){

    val currentContext = LocalContext.current

    var skillTitleEditText by remember {mutableStateOf("")}
    var active by remember {mutableStateOf(false)}

    var skillModelsSearchBar: List<SkillModel> = remember {
        mutableListOf()
    }

    var skillModelsRegistered: List<SkillModel> = remember {
        mutableListOf()
    }

    var skillProgressions: List<SkillProgressionModel> = remember {
        mutableListOf()
    }

    var isSkillSelected: MutableState<SelectedSkillState> = remember {
        mutableStateOf(SelectedSkillState.NOT_SELECTED)
    }

    var skillSelected: MutableState<SkillModel> = remember {
        mutableStateOf(SkillModel())
    }

    var currentUserSkillSubs: MutableState<UserSkillSubsModel> = remember {
        mutableStateOf(UserSkillSubsModel())
    }

    LaunchedEffect(Unit) {
        sharedViewModel.retrieveUserSkillProgressionList(
            sharedViewModel.getCurrentUserMail(),
            currentContext,
        ){
            skillProgressions = it
        }

        sharedViewModel.retrieveAllUserSkill(
            sharedViewModel.getCurrentUserMail(),
            currentContext
        ){
            skillModelsSearchBar = it
        }

        sharedViewModel.retrieveUserSkillSub(
            sharedViewModel.getCurrentUserMail(),
            currentContext,
        ){
            currentUserSkillSubs.value = it

            sharedViewModel.retrieveSkillsFromList(currentContext, currentUserSkillSubs.value.registeredSkillsIDs){
                skillModelsRegistered = it
            }
        }
    }


    Scaffold(
        topBar = { AppToolBar(title = "Search a Skill", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController, openDialog, pendingRoute)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
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
                }) {
                skillModelsSearchBar.filter { skillTitleEditText.lowercase() in it.titleSkill.lowercase() }.forEach{
                    SkillSearchBlock(it, it.id in skillProgressions.map { it.skillId })
                    {
                        skillSelected.value = it
                        isSkillSelected.value = if (it.id in skillProgressions.map { it.skillId }) SelectedSkillState.REG_SELECTED else SelectedSkillState.NEW_SELECTED
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            skillModelsRegistered.forEach{
                SkillSearchBlock(skill = it, isInProgress = false) {
                    
                }
            }
        }




        if(isSkillSelected.value == SelectedSkillState.NEW_SELECTED){
            Box(){
                SkillInfoPopUp_UNSTARTED(skillSelected.value, sharedViewModel,
                    {
                        sharedViewModel.retrieveSkillSection(
                            skillSelected.value.id,
                            skillSelected.value.skillSectionsList.get(0),
                            currentContext,
                        ){section ->
                            val mapNonCompletedTasks: Map<String, Int> = section.skillTasksList.associateWith { 0 }

                            val skillProgression = SkillProgressionModel(sharedViewModel.getCurrentUserMail(), skillSelected.value.id, skillSelected.value.skillSectionsList.get(0),mapNonCompletedTasks)
                            skillProgressions += skillProgression

                            sharedViewModel.saveSkillProgression(skillProgression, currentContext)

                            isSkillSelected.value = SelectedSkillState.REG_SELECTED
                        }

                    },

                    {
                        isSkillSelected.value = SelectedSkillState.NOT_SELECTED
                    },
                )
            }
        }else if(isSkillSelected.value == SelectedSkillState.REG_SELECTED){
            Box(){

                SkillInfoPopUp_STARTED(sharedViewModel, skillSelected.value, skillProgressions?.find { it.skillId == skillSelected.value.id } ?: SkillProgressionModel(), {                        isSkillSelected.value = SelectedSkillState.NOT_SELECTED
                })
            }
        }

    }




    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}


