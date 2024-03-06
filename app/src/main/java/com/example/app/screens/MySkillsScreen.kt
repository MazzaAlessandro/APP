package com.example.app.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.util.SharedViewModel

@Composable
fun SkillListElement(sharedViewModel: SharedViewModel, skillProgression: SkillProgressionModel){
    //var skillObject: SkillModel = sharedViewModel.retrieveSkill(skillProgression.skillId)

    Box(modifier = Modifier
        .clip(RoundedCornerShape(8))
        .background(color = Color.Red)
        .padding(20.dp)){
        Row {
            Column {
                Row {

                }
            }
        }
    }
}

@Composable
fun SkillListBlock(sharedViewModel: SharedViewModel){
    SkillListElement(sharedViewModel, skillProgression = SkillProgressionModel("aaaa", "aaaaaa", 0, mutableMapOf(
        Pair<String, Int>("0", 1),
        Pair<String, Int>("1", 0))))
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MySkillsScreen(navController: NavHostController,
                  sharedViewModel: SharedViewModel
){
    Scaffold(
        topBar = { AppToolBar(title = "My Skills", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            )
            {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8))
                            .background(color = Color.Green)
                            .padding(20.dp)
                    ) {
                        Text(text = "My Skills", fontSize = 32.sp)
                    }

                    SkillListBlock(sharedViewModel);
                }
            }
        }
    }

}

