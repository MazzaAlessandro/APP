package com.example.app.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RadioButtonChecked
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.UserDataModel
import com.example.app.models.UserSkillSubsModel
import com.example.app.ui.theme.PrimaryColor
import com.example.app.ui.theme.greenColor
import com.example.app.ui.theme.redColor
import com.example.app.ui.theme.yellowColor
import com.example.app.util.SharedViewModel
import com.example.app.util.WindowInfo
import com.example.app.util.relative
import com.example.app.util.rememberWindowInfo
import com.google.firebase.auth.FirebaseAuth
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Composable
fun SkillCard(
    skill: SkillModel,
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 4.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(5.dp)
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

                    Text(
                        skill.titleSkill,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontSize = 25.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val sectionAmount = skill.skillSectionsList.size

                    Row(horizontalArrangement = Arrangement.Start) {
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
            }
        }
    }
}

@Composable
fun SkillCardEmpty(
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 4.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(5.dp)
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
                Icon(imageVector = Icons.Filled.RadioButtonChecked, "SkillLogo",  modifier = Modifier.fillMaxSize(), tint = Color.Gray)
            }
            Spacer(Modifier.width(25.dp))

            Text(
                "No skill started yet.",
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                fontSize = 30.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(navController: NavHostController,
                  sharedViewModel: SharedViewModel,
                  openDialog: MutableState<Boolean>,
                  pendingRoute: MutableState<String?>
){
    val context = LocalContext.current

    val windowInfo = rememberWindowInfo()

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

    val userSkillSubsModel: MutableState<UserSkillSubsModel> = remember {
        mutableStateOf(UserSkillSubsModel())
    }

    val badges : MutableState<List<BadgeDataModel>> = remember {
        mutableStateOf(listOf())
    }

    val lastSkillStarted : MutableState<SkillModel> = remember {
        mutableStateOf(SkillModel())
    }

    val lastTimeStarted : MutableState<ZonedDateTime> = remember {
        mutableStateOf(ZonedDateTime.now())
    }

    if(sharedViewModel.getCurrentUserMail()!=""){
        LaunchedEffect(userData) {
            sharedViewModel.retrieveUserData(
                sharedViewModel.getCurrentUserMail(),
                context
            ){
                userData = it

                sharedViewModel.retrieveUserSkillSub(
                    mail,
                    context
                ){
                    userSkillSubsModel.value = it

                    sharedViewModel.retrieveAllBadges(
                        it.badgesObtained,
                        context
                    ){
                        badges.value = it
                    }

                    sharedViewModel.retrieveUserSkillProgressionList(
                        mail,
                        context
                    ){
                        skillProgressionList.value = it.toMutableList()

                        if(!it.isEmpty()){
                            val id = skillProgressionList.value.minBy { ZonedDateTime.parse(it.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME) }

                            sharedViewModel.retrieveSkill(id.skillId, context){
                                lastSkillStarted.value = it
                                lastTimeStarted.value = ZonedDateTime.parse(skillProgressionList.value.map { it.dateTime }.min(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                            }
                        }
                    }

                }

            }
        }
    }

    val stat = listOf(
        StatData("Total days using the app:", 356),
        //StatData("Consecutive days using the app:", 21),
        StatData("Badges obtained:", badges.value.count()),
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
                .padding(innerPadding)
                .testTag("ProfileScreen"),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .testTag("ProfileInfo"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(modifier = Modifier.weight(1.0f), contentAlignment = Alignment.Center){
                        ProfileImage(
                            userData.pfpUri, false, relative(150.dp)){
                        }

                    }


                    Column(modifier = Modifier
                        .weight(2.0f)
                        .height(relative(150.dp))
                        .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                            Text(text = userData.username, fontWeight = FontWeight.W600, color = Color.DarkGray, fontSize = 60.sp)

                            IconButton(
                                modifier = Modifier.padding(start = 20.dp),
                                onClick = { navController.navigate(Routes.Update.route) }
                            ) {
                                Icon(
                                    modifier = Modifier.size(60.dp),
                                    tint = Color.DarkGray,
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit"
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(10.dp))
/*
                        Column(verticalArrangement = Arrangement.spacedBy(relative(size = 4.dp))){
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp, 0.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(text = "Last Skill Started", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                                        color = Color.DarkGray,
                                    )

                                }

                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 0.dp, end = 10.dp),
                                    color = Color.DarkGray
                                )
                            }


                            if(lastSkillStarted.value.id != ""){
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(1.0f)
                                ) {
                                    SkillCard(lastSkillStarted.value, {})
                                }
                            }else{
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.weight(1.0f)
                                ) {
                                    SkillCardEmpty({navController.navigate(Routes.Search.route)})
                                }
                            }

                        }*/

                    }
                }



                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable {
                            navController.navigate(Routes.Badges.route)
                        }
                        .testTag("Badges"),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp, 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(text = "Badges Obtained", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                                    color = Color.DarkGray,
                                )

                            }

                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 0.dp, end = 10.dp),
                                color = Color.DarkGray
                            )
                        }

                        Column(
                            Modifier.weight(1.0f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column (
                                Modifier.weight(1.0f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                BadgeIcon(BadgeColor.GOLD, relative(100.dp))

                                Spacer(modifier = Modifier.height(relative(5.dp)))

                                Text(badges.value.filter { it.badgeColor == BadgeColor.GOLD }.count().toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                            }

                            Column (
                                Modifier.weight(1.0f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                BadgeIcon(BadgeColor.SILVER, relative(100.dp))

                                Spacer(modifier = Modifier.height(relative(5.dp)))

                                Text(badges.value.filter { it.badgeColor == BadgeColor.SILVER }.count().toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                            }

                            Column (
                                Modifier.weight(1.0f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                BadgeIcon(BadgeColor.BRONZE, relative(100.dp))

                                Spacer(modifier = Modifier.height(relative(5.dp)))

                                Text(badges.value.filter { it.badgeColor == BadgeColor.BRONZE }.count().toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                            }
                        }

                    }

                    Divider(
                        color = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp),
                        thickness = 1.dp
                    )

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f)
                        .clickable {
                            navController.navigate(Routes.History.route)
                        }
                        .testTag("pieChart"),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp, 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(text = "Current Progression", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    color = Color.DarkGray,
                                )

                            }

                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 20.dp),
                                color = Color.DarkGray
                            )
                        }
                        var pieData = mutableListOf(
                            PieChartData("Completed Skills: ", userSkillSubsModel.value.finishedSkills.count(), PrimaryColor),
                            PieChartData("Skills In progress: ", skillProgressionList.value.filter { !it.isFinished }.count(), PrimaryColor.copy(alpha = 0.5f)),
                            PieChartData("Unstarted Skills: ", userSkillSubsModel.value.registeredSkillsIDs.count(), color = Color.Gray.copy(alpha = 0.5f))
                        )

                        Column(
                            Modifier.weight(1.0f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AnimatedPieChart(
                                modifier = Modifier
                                    .padding(10.dp),
                                    pieDataPoints = pieData,
                                relative(250.dp),
                                50f
                            )

                            Spacer(modifier = Modifier.height(40.dp))

                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                            ){
                                pieData.map{
                                    Row(modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(modifier = Modifier
                                            .size(relative(20.dp))
                                            .clip(RoundedCornerShape(5.dp))
                                            .background(it.color))
                                        Text(text = it.label, fontSize = 18.sp,fontWeight = FontWeight.W600)
                                        Text(text = it.value.toString(), fontSize = 18.sp)
                                    }
                                    Spacer(modifier = Modifier.height(relative(2.dp)))
                                }
                            }
                        }

                        Column(){
                            Column(Modifier.padding(bottom = 20.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp, 0.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(text = "Last Skill Started", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        color = Color.DarkGray,
                                    )

                                }

                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp, end = 10.dp),
                                    color = Color.DarkGray
                                )
                            }


                            if(lastSkillStarted.value.id != ""){
                                Box(
                                    Modifier.padding(bottom = 20.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    SkillCard(lastSkillStarted.value, {})
                                }
                            }else{
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(bottom = 20.dp),
                                ) {
                                    SkillCardEmpty({navController.navigate(Routes.Search.route)})
                                }
                            }

                        }

                    }

                }

            }
            else{
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .testTag("ProfileInfo"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileImage(
                        userData.pfpUri, false, relative(90.dp)){
                    }

                    Text(text = userData.username, fontWeight = FontWeight.W600, color = Color.DarkGray, style = TextStyle(fontSize = 35.sp))

                    IconButton(
                        onClick = { navController.navigate(Routes.Update.route) }
                    ) {
                        Icon(
                            tint = Color.DarkGray,
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Routes.Badges.route)
                        }
                        .testTag("Badges"),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = "Badges Obtained", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = Color.DarkGray,
                        )

                        Text(
                            text = badges.value
                                .count().toString() + " badges",
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
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column (horizontalAlignment = Alignment.CenterHorizontally){
                            BadgeIcon(BadgeColor.GOLD, relative(90.dp))

                            Spacer(modifier = Modifier.height(relative(5.dp)))

                            Text(badges.value.filter { it.badgeColor == BadgeColor.GOLD }.count().toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                        }

                        Column (horizontalAlignment = Alignment.CenterHorizontally){
                            BadgeIcon(BadgeColor.SILVER, relative(90.dp))

                            Spacer(modifier = Modifier.height(relative(5.dp)))

                            Text(badges.value.filter { it.badgeColor == BadgeColor.SILVER }.count().toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                        }

                        Column (horizontalAlignment = Alignment.CenterHorizontally){
                            BadgeIcon(BadgeColor.BRONZE, relative(90.dp))

                            Spacer(modifier = Modifier.height(relative(5.dp)))

                            Text(badges.value.filter { it.badgeColor == BadgeColor.BRONZE }.count().toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                        }

                    }
                }

                Column(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Routes.History.route)
                        }
                        .testTag("pieChart"),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = "Current Progression", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = Color.DarkGray,
                        )

                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 2.dp),
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        var pieData = mutableListOf(
                            PieChartData("Completed Skills: ", userSkillSubsModel.value.finishedSkills.count(), PrimaryColor),
                            PieChartData("Skills In progress: ", skillProgressionList.value.filter { !it.isFinished }.count(), PrimaryColor.copy(alpha = 0.5f)),
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
                }


                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp, 0.dp, 10.dp, 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(text = "Last Started Skill", fontSize = 25.sp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                                color = Color.DarkGray,
                            )

                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 2.dp),
                            color = Color.DarkGray
                        )

                        if(lastSkillStarted.value.id != ""){
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.weight(1.0f)
                            ) {
                                SkillCard(lastSkillStarted.value, {})
                            }
                        }


                    }



                }
            }
        }
    }
}
