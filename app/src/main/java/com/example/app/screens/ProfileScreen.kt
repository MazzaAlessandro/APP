package com.example.app.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.ProfileImage
import com.example.app.Routes
import com.example.app.additionalUI.AnimatedPieChart
import com.example.app.additionalUI.BadgeColor
import com.example.app.additionalUI.BadgeIcon
import com.example.app.additionalUI.PieChartData
import com.example.app.additionalUI.StatData
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.SkillProgressionModel
import com.example.app.models.UserDataModel
import com.example.app.models.UserSkillSubsModel
import com.example.app.util.SharedViewModel
import com.example.app.util.relative
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(navController: NavHostController,
                  sharedViewModel: SharedViewModel,
                  openDialog: MutableState<Boolean>,
                  pendingRoute: MutableState<String?>
){
    val context = LocalContext.current

    /*
    * TODO: connect to Firebase in order to get actual data from the Database
    *  This is just hard coded
    * */


    if(FirebaseAuth.getInstance().currentUser != null && sharedViewModel.getCurrentUserMail().isBlank()){
        val currMail = FirebaseAuth.getInstance().currentUser?.email
        if (currMail!=null){
            sharedViewModel.setCurrentUserMail(currMail)
        }
    }

    //val userId = FirebaseAuth.getInstance().currentUser?.uid
    val mail = sharedViewModel.getCurrentUserMail()
    var userData by remember(mail){
        mutableStateOf(UserDataModel())
    }

    var skillProgressionList : MutableState<MutableList<SkillProgressionModel>> = remember{
        mutableStateOf(mutableListOf())
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    val userSkillSubsModel: MutableState<UserSkillSubsModel> = remember {
        mutableStateOf(UserSkillSubsModel())
    }

    LaunchedEffect(userData) {
        sharedViewModel.retrieveUserData(
            mail,
            context
        ){
            userData = it

            sharedViewModel.retrieveUserSkillSub(
                mail,
                context
            ){
                userSkillSubsModel.value = it

                sharedViewModel.retrieveUserSkillProgressionList(
                    mail,
                    context
                ){
                    skillProgressionList.value = it.toMutableList()
                }

            }

        }
    }

    /*
    sharedViewModel.retrieveUserSkillProgressionList(
            mail,
            context = context
    ){skillProgList ->
        var tmp0 = 0
        var tmp1 = 0
        var tmp2 = 0

        skillProgList.forEach { skillProg ->

            if (skillProg.isFinished)
                tmp0++

            else{
                if(skillProg.currentSectionId == "0")
                    tmp2++
                else
                    tmp1++
            }
        }

        println(tmp0)
        println(tmp1)
        println(tmp2)
        userData.listSkillProgressions = mutableListOf(tmp0, tmp1, tmp2)

        sharedViewModel.updateUserData(userData, context)
    }

     */

    val stat = listOf(
        StatData("Total days using the app:", 356),
        //StatData("Consecutive days using the app:", 21),
        StatData("Badges obtained:", userData.badgeCounter.sum()),
        StatData("Total Skills:", skillProgressionList.value.filter { it.isFinished }.count() +
                skillProgressionList.value.filter { !it.isFinished }.count() +
                userSkillSubsModel.value.registeredSkillsIDs.count())
    )

    Scaffold(
        topBar = { AppToolBar(title = "Profile", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController, openDialog, pendingRoute)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImage(userData.pfpUri, false, relative(100.dp)){
                }

                Text(text = userData.username, fontWeight = FontWeight.W600, style = TextStyle(fontSize = 35.sp))

                IconButton(
                    onClick = { navController.navigate(Routes.Update.route) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit"
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .padding(10.dp, 0.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            Column(
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Badges.route)
                },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Badges earned", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        BadgeIcon(BadgeColor.GOLD, relative(90.dp))

                        Spacer(modifier = Modifier.height(relative(5.dp)))

                        Text(userData.badgeCounter[0].toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                    }

                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        BadgeIcon(BadgeColor.SILVER, relative(90.dp))

                        Spacer(modifier = Modifier.height(relative(5.dp)))

                        Text(userData.badgeCounter[1].toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                    }

                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        BadgeIcon(BadgeColor.BRONZE, relative(90.dp))

                        Spacer(modifier = Modifier.height(relative(5.dp)))

                        Text(userData.badgeCounter[2].toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                    }

                }
            }

            Divider(
                modifier = Modifier
                    .padding(10.dp, 0.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            Text(text = "Current progression", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                var pieData = mutableListOf(
                    PieChartData("Completed Skills: ", skillProgressionList.value.filter { it.isFinished }.count(), primaryColor),
                    PieChartData("Skills In progress: ", skillProgressionList.value.filter { !it.isFinished }.count(), primaryColor.copy(alpha = 0.5f)),
                    PieChartData("Unstarted Skills: ", userSkillSubsModel.value.registeredSkillsIDs.count(), color = Color.Gray.copy(alpha = 0.5f))
                )
                AnimatedPieChart(
                    modifier = Modifier
                        .padding(10.dp),
                    pieDataPoints = pieData
                )

                Column (
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ){
                    pieData.map{
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier
                                .size(relative(15.dp))
                                .clip(RoundedCornerShape(5.dp))
                                .background(it.color))
                            Text(text = it.label, fontWeight = FontWeight.W600)
                            Text(text = it.value.toString())
                        }
                        Spacer(modifier = Modifier.height(relative(2.dp)))
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .padding(10.dp, 0.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp, 0.dp, 10.dp, 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Statistics", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))

                stat.map {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = it.statName,
                            color = Color.Black
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            color = Color.Gray,
                            thickness = 1.dp
                        )

                        Text(
                            text = it.statValue.toString(),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
