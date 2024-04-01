package com.example.app.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillModel
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
fun SectionBox(id:Int, section:SkillSectionModel, onTitleChange: (String) -> Unit, onDescriptionChange: (String) -> Unit){
    Box(modifier = Modifier
        .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(10.dp))
        .padding(15.dp)
    ){
        Column {
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
                TextField(value = task.requiredAmount.toString(),
                    onValueChange = onAmountChange,
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
        }
    }
}

fun GenerateNewSkillID(): String{
    val db = FirebaseFirestore.getInstance()
    val newSkillRef = db.collection("skill").document()
    return newSkillRef.id
}



fun TestSkillSave(){
    val db = FirebaseFirestore.getInstance()
    val skill = SkillModel(titleSkill = "TITLE TEST", skillDescription = "WAWAWA DESCR", skillSectionsList = listOf())
    val newSkillRef = db.collection("skill").document()
    skill.id = newSkillRef.id
    newSkillRef.set(skill)
}


fun TestCreateSection(){
    val db = FirebaseFirestore.getInstance()
    val skill = SkillSectionModel()
    val newSkillRef = db.collection("section").document()
    skill.id = newSkillRef.id
    newSkillRef.set(skill)
}






// TODO TAKE CARE OF THE ID OF THE SKILL SECTIONS
// TODO CREATE THE LIST / MAP OF SKILL TASKS
// TODO PUT THE BUTTONS AND THE UIS
// TODO CHANGE TO LAZY COLUMNS AS SOON AS POSSIBLE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){

    var skill : MutableState<SkillModel> = rememberSaveable {
        mutableStateOf(SkillModel())
    }

    var skillSections: MutableState<List<SkillSectionModel>> = rememberSaveable {
        mutableStateOf(mutableListOf())
    }


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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Text(text = "Create a skill", fontSize = 32.sp)


                //GeneralInfoBox(sharedViewModel, title = title, {title = it}, description, {description = it})
                GeneralInfoBox(skill.value, {skill.value = skill.value.copy(titleSkill = it)}, {skill.value = skill.value.copy(skillDescription = it)})


                /*
                LazyColumn{
                    itemsIndexed(skillSections.value){index, section ->
                        SectionBox(id = index, section = section,
                            {skillSections.value = skillSections.value.toMutableList().apply {
                                set(index, section.copy(titleSection = it))
                            }
                            },
                            {skillSections.value = skillSections.value.toMutableList().apply {
                                set(index, section.copy(descriptionSection = it))
                            }
                            })
                    }
                }*/



                skillSections.value.forEachIndexed{index, section ->
                    SectionBox(id = index, section = section,
                        {skillSections.value = skillSections.value.toMutableList().apply {
                            set(index, section.copy(titleSection = it))
                        }
                        },
                        {skillSections.value = skillSections.value.toMutableList().apply {
                            set(index, section.copy(descriptionSection = it))
                        }
                        })
                }

                TaskBox(1, SkillTaskModel("ddee", "dddd", "ssss", "assad dadad dedurica", 2), {}, {})

                Button(onClick = {
                    skillSections.value = skillSections.value + SkillSectionModel(id = skillSections.value.size.toString(), titleSection = "", skillTasksList = mutableListOf())}) {
                    Text(text = "ADD SECTION +")
                }


            }
        }
    }

    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}