package com.example.app.screens

import android.content.Context
import android.icu.text.CaseMap.Title
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.util.SharedViewModel
import com.google.firebase.firestore.FirebaseFirestore

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
                    TextField(value = skill.titleSkill,
                        onValueChange = onTitleChange,
                        modifier = Modifier.padding(10.dp),
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
                TextField(value = skill.skillDescription,
                    onValueChange = onDescriptionChange,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun SectionBox(id:Int, section:SkillSectionModel,
               listTasks: List<SkillTaskModel>,
               onTitleChange: (String) -> Unit,
               onDescriptionChange: (String) -> Unit,
               onAddTask: () -> Unit,
               onChangeTaskDescription: (Int, String) -> Unit,
               onChangeTaskAmount: (Int, Int) -> Unit){

    var sectionCounter by rememberSaveable {
        mutableStateOf(0)
    }

    Box(modifier = Modifier
        .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(10.dp))
        .padding(15.dp)
    ){
        Column{
            Row (verticalAlignment = Alignment.CenterVertically)
            {

                Text(text = "Title section $id:",
                    fontSize = 19.sp,
                    modifier = Modifier.padding(2.dp))


                Box(
                    modifier = Modifier
                        .background(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(10))
                ){
                    TextField(value = section.titleSection,
                        onValueChange = onTitleChange,
                        modifier = Modifier.padding(10.dp),
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
                TextField(value = section.descriptionSection,
                    onValueChange = onDescriptionChange,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier
                .size(400.dp, 15.dp)
            )

            listTasks.forEachIndexed{index, task ->
                
                TaskBox(id = index, task = task, onDescriptionChange = { onChangeTaskDescription(index, it) }, onAmountChange = { onChangeTaskAmount(index, it.toInt()) })

                Spacer(modifier = Modifier
                    .height(10.dp)
                )
                
            }


            Button(onClick = onAddTask) {
                Text(text = "ADD TASK +")
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBox(id:Int, task:SkillTaskModel, onDescriptionChange: (String) -> Unit, onAmountChange: (String) -> Unit){
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
                val containerColor = Color(0xFFF0F0F0)
                TextField(value = task.taskDescription,
                    onValueChange = onDescriptionChange,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = containerColor,
                        unfocusedContainerColor = containerColor,
                        disabledContainerColor = containerColor,
                    )
                )
            }

            Text(text = "Required amount:",
                fontSize = 15.sp,
                modifier = Modifier.padding(5.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
            ){
                val containerColor = Color(0xFFF0F0F0)
                TextField(value =

                    if(task.requiredAmount != 0){
                        task.requiredAmount.toString()
                    }else{
                        ""
                    },
                    onValueChange = {
                        try{
                            val value = it.toLong()
                            if(value < Int.MAX_VALUE && value > 0){
                                onAmountChange(it)
                            }
                        } catch (e: NumberFormatException){
                            onAmountChange((0).toString())
                        }

                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = containerColor,
                        unfocusedContainerColor = containerColor,
                        disabledContainerColor = containerColor,
                    )
                )
            }
        }
    }
}

fun GenerateNewSkillID(): String{
    val db = FirebaseFirestore.getInstance()
    val newSkillRef = db.collection("skill").document()
    return newSkillRef.id
}


fun SaveEverything(refId: String, sharedViewModel: SharedViewModel, context: Context, skill: SkillModel, sections: List<SkillSectionModel>, tasks: Map<String, List<SkillTaskModel>>){

    val db = FirebaseFirestore.getInstance()
    val skillRef = db.collection("skill").document(refId)
    skillRef.set(skill.copy(skillSectionsList = sections.map{
        it.id
    }))

    sections.forEach{

        val taskList = (tasks[it.id]?.toMutableList() ?: mutableListOf()).map{
            it.id
        }

        sharedViewModel.saveSkillSection(it.copy(skillTasksList = taskList), context)
    }

    tasks.values.flatten().forEach{
        sharedViewModel.saveSkillTask(it, context)
    }

    //val skillProg = SkillProgressionModel("aaaaaa", skill.id, "0", )

}


//TODO ADD THE SKILL PROGRESSIONS
//TODO MAKE SURE THAT EVERYTHING GETS SAVED CORRECTLY
//TODO SOLVE PROBLEM IN SAVING TASKS
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){

    val skillID by rememberSaveable {
        mutableStateOf(GenerateNewSkillID())
    }

    var skill : MutableState<SkillModel> = rememberSaveable {
        mutableStateOf(SkillModel(id = skillID))
    }

    var skillSections: MutableState<List<SkillSectionModel>> = rememberSaveable {
        mutableStateOf(mutableListOf())
    }

    var skillTasks: MutableState<Map<String, MutableList<SkillTaskModel>>> = rememberSaveable {
        mutableStateOf(mapOf())
    }

    val context = LocalContext.current;


    Scaffold(
        topBar = { AppToolBar(title = "Create a new Skill", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ){innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)

        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {

                item {
                    Text(text = "Create a skill", fontSize = 32.sp)


                    //GeneralInfoBox(sharedViewModel, title = title, {title = it}, description, {description = it})
                    GeneralInfoBox(skill.value, {skill.value = skill.value.copy(titleSkill = it)}, {skill.value = skill.value.copy(skillDescription = it)})
                }

                itemsIndexed(skillSections.value){index, section ->
                    SectionBox(id = index, section = section, skillTasks.value.get(section.id)?.toList() ?: mutableListOf(),
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
                            updatedList.add(SkillTaskModel(updatedList.size.toString(), section.id, skillID, "", 0))

                            val updatedMap = skillTasks.value.toMutableMap()
                            updatedMap[section.id] = updatedList

                            skillTasks.value = updatedMap
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
                        })
                }

                item {
                    //TaskBox(1, SkillTaskModel("ddee", "dddd", skillID, "assad dadad dedurica", 2), {}, {})

                    Button(onClick = {
                        skillSections.value = skillSections.value + SkillSectionModel(id = skillSections.value.size.toString(), idSkill = skillID, titleSection = "", skillTasksList = mutableListOf())}) {
                        Text(text = "ADD SECTION +")
                    }

                    Button(onClick = {
                        SaveEverything(skillID, sharedViewModel, context, skill.value, skillSections.value, skillTasks.value)
                    }) {
                        Text(text = "SAVE")
                    }
                }

            }
        }
    }

    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}