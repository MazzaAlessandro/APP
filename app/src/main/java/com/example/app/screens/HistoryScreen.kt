package com.example.app.screens

import android.content.Context
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.additionalUI.BadgeBanner
import com.example.app.additionalUI.BadgeCard
import com.example.app.additionalUI.BadgeColor
import com.example.app.additionalUI.BadgeIcon
import com.example.app.additionalUI.BannerSize
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserSkillSubsModel
import com.example.app.ui.theme.greenColor
import com.example.app.ui.theme.yellowColor
import com.example.app.util.SharedViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


enum class EVEN_TYPE{
    SKILLCREATED, SKILLFINISHEDFT, BADGEGOTTEN
}


@Composable
fun HistBan_Badge(
    badge: BadgeDataModel,
    dateTime: ZonedDateTime,
    size: BannerSize = BannerSize.MEDIUM,
    onClick: () -> Unit
){

    val colorCircle = MaterialTheme.colorScheme.primary
    var iconSize: Dp = 60.dp
    var fontSize = 15

    if (size == BannerSize.SMALL){
        iconSize = 45.dp
        fontSize = 12
    }

    if (size == BannerSize.LARGE){
        iconSize = 75.dp
        fontSize = 18
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp, 2.dp)
        .clip(shape = RoundedCornerShape(10.dp))
        .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
        .background(Color.Gray.copy(alpha = 0.2f))
        .clickable {
            onClick()
        },
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically){
            
            Column(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .border(1.dp, colorCircle, RoundedCornerShape(10.dp))
                    .padding(horizontal = 5.dp, vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    color = colorCircle
                )
                Text(
                    text = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    color = colorCircle
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Badge Obtained", color = Color.Cyan, fontSize = (fontSize + 3).sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, Color.Cyan, RoundedCornerShape(10.dp))
                        .padding(vertical = 2.dp, horizontal = 5.dp)
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )


                Row {
                    BadgeIcon(badge.badgeColor, iconSize, true)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp, 0.dp, 0.dp, 5.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = badge.badgeName, fontSize = (fontSize+3).sp, fontWeight = FontWeight.Bold)

                        Text(text = badge.description, fontSize = (fontSize + 1).sp, lineHeight = (fontSize + 1).sp)
                    }
                }
            }

        }
    }
}

@Composable
fun HistBan_SkillFin(
    skillModel: SkillModel,
    dateTime: ZonedDateTime,
    size: BannerSize = BannerSize.MEDIUM,
    onClick: () -> Unit
){

    val colorCircle = MaterialTheme.colorScheme.primary
    var iconSize: Dp = 60.dp
    var fontSize = 15

    if (size == BannerSize.SMALL){
        iconSize = 45.dp
        fontSize = 12
    }

    if (size == BannerSize.LARGE){
        iconSize = 75.dp
        fontSize = 18
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp, 2.dp)
        .clip(shape = RoundedCornerShape(10.dp))
        .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
        .background(Color.Gray.copy(alpha = 0.2f))
        .clickable {
            onClick()
        },
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically){

            Column(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .border(1.dp, colorCircle, RoundedCornerShape(10.dp))
                    .padding(horizontal = 5.dp, vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    color = colorCircle
                )
                Text(
                    text = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    color = colorCircle
                )
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Skill Finished:",
                    color = Color.Green,
                    fontSize = (fontSize + 3).sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, Color.Green, RoundedCornerShape(10.dp))
                        .padding(vertical = 2.dp, horizontal = 5.dp)
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )

                Row {

                    Box(
                        modifier = Modifier
                            .size(iconSize) // Set the size of the circle
                            .background(
                                colorCircle,
                                shape = CircleShape
                            ) // Use the color of the circle in your image
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp, 0.dp, 0.dp, 5.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = skillModel.titleSkill, fontSize = (fontSize+3).sp, fontWeight = FontWeight.Bold)

                        Text(text = skillModel.skillDescription, fontSize = (fontSize + 1).sp, lineHeight = (fontSize + 1).sp)

                        //Text(modifier = Modifier.padding(top = 5.dp), text = "Skill Creator: " + skillModel.creatorUserName, fontSize = (fontSize -1).sp, lineHeight = (fontSize - 1).sp)
                    }
                }
            }

        }
    }
}

@Composable
fun HistBan_SkillCrea(
    skillModel: SkillModel,
    dateTime: ZonedDateTime,
    size: BannerSize = BannerSize.MEDIUM,
    onClick: () -> Unit
){

    val colorCircle = MaterialTheme.colorScheme.primary
    var iconSize: Dp = 60.dp
    var fontSize = 15

    if (size == BannerSize.SMALL){
        iconSize = 45.dp
        fontSize = 12
    }

    if (size == BannerSize.LARGE){
        iconSize = 75.dp
        fontSize = 18
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp, 2.dp)
        .clip(shape = RoundedCornerShape(10.dp))
        .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
        .background(Color.Gray.copy(alpha = 0.2f))
        .clickable {
            onClick()
        },
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically){

            Column(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .border(1.dp, colorCircle, RoundedCornerShape(10.dp))
                    .padding(horizontal = 5.dp, vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    color = colorCircle
                )
                Text(
                    text = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    color = colorCircle
                )
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Skill Created",
                    color = Color.Magenta,
                    fontSize = (fontSize + 3).sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, Color.Magenta, RoundedCornerShape(10.dp))
                        .padding(vertical = 2.dp, horizontal = 5.dp)
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )

                Row {

                    Box(
                        modifier = Modifier
                            .size(iconSize) // Set the size of the circle
                            .background(
                                colorCircle,
                                shape = CircleShape
                            ) // Use the color of the circle in your image
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp, 0.dp, 0.dp, 5.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = skillModel.titleSkill, fontSize = (fontSize+3).sp, fontWeight = FontWeight.Bold)

                        Text(text = skillModel.skillDescription, fontSize = (fontSize + 1).sp, lineHeight = (fontSize + 1).sp)
                    }
                }
            }


        }
    }
}

@Composable
fun EventCard(eventType: EVEN_TYPE, skillModel: SkillModel, badge: BadgeDataModel, dateTime: ZonedDateTime, onClick: (EVEN_TYPE) -> Unit){
    var introText = "";
    var titleText = "";
    if(eventType == EVEN_TYPE.BADGEGOTTEN){
        introText = "Badge Gotten: "
        HistBan_Badge(
            badge,
            dateTime,
            BannerSize.MEDIUM,
        )
        {
            onClick(EVEN_TYPE.BADGEGOTTEN)
        }
    }else if(eventType == EVEN_TYPE.SKILLCREATED){
        HistBan_SkillCrea(
            skillModel,
            dateTime,
            BannerSize.MEDIUM,
        )
        {
            onClick(EVEN_TYPE.SKILLCREATED)
        }
        introText = "Skill Created: "
        titleText = skillModel.titleSkill
    }else{
        HistBan_SkillFin(
            skillModel,
            dateTime,
            BannerSize.MEDIUM,
        )
        {
            onClick(EVEN_TYPE.SKILLFINISHEDFT)
        }
        introText = "Skill Finished: "
        titleText = skillModel.titleSkill
    }
}


@Composable
fun HistoryScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    val currentContext = LocalContext.current

    val userSkillSub: MutableState<UserSkillSubsModel> = remember {
        mutableStateOf(UserSkillSubsModel())
    }

    val listCreatedSkills : MutableState<List<SkillModel>> = remember {
        mutableStateOf(listOf())
    }

    val listObtainedBadges : MutableState<List<BadgeDataModel>> = remember {
        mutableStateOf(listOf())
    }

    val listFinishedSkills : MutableState<List<SkillModel>> = remember {
        mutableStateOf(listOf())
    }

    val listEvents : MutableState<List<ThreeGroup>> = remember {
        mutableStateOf(listOf())
    }

    val isPopUpActive: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    val selectedThreeGroup: MutableState<ThreeGroup> = remember {
        mutableStateOf(ThreeGroup(EVEN_TYPE.BADGEGOTTEN, ZonedDateTime.now(), ""))
    }

    LaunchedEffect(sharedViewModel.getCurrentUserMail()) {
        sharedViewModel.retrieveUserSkillSub(
            sharedViewModel.getCurrentUserMail(),
            currentContext
        ){currUserSkillSub ->
            userSkillSub.value = currUserSkillSub

            sharedViewModel.retrieveSkillsFromList(
                currentContext,
                userSkillSub.value.createdSkillsId
            ){
                listCreatedSkills.value = it
                listEvents.value = ComputeList(currUserSkillSub, listCreatedSkills.value)
            }

            sharedViewModel.retrieveSkillsFromList(
                currentContext,
                userSkillSub.value.finishedSkills
            ){
                listFinishedSkills.value = it
            }

            sharedViewModel.retrieveAllBadges(
                userSkillSub.value.badgesObtained,
                currentContext,
            ){
                listObtainedBadges.value = it
            }
        }
    }


    Scaffold(
        topBar = { AppToolBar(title = "History", navController, sharedViewModel) },

    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                listEvents.value.forEachIndexed{index, event ->

                    Row {
                        if(event.type == EVEN_TYPE.BADGEGOTTEN){
                            EventCard(EVEN_TYPE.BADGEGOTTEN, SkillModel(), listObtainedBadges.value.find { it.skillId + it.sectionId == event.stringId }!!, event.time)
                            {
                                selectedThreeGroup.value = event
                                isPopUpActive.value = true
                            }
                        }
                        else if(event.type == EVEN_TYPE.SKILLCREATED){
                            EventCard(EVEN_TYPE.SKILLCREATED, listCreatedSkills.value.find { it.id == event.stringId }!!, BadgeDataModel(), event.time )
                            {
                                selectedThreeGroup.value = event
                                isPopUpActive.value = true
                            }
                        }
                        else{
                            EventCard(EVEN_TYPE.SKILLFINISHEDFT, listFinishedSkills.value.find { it.id == event.stringId }!!, BadgeDataModel(), event.time )
                            {
                                selectedThreeGroup.value = event
                                isPopUpActive.value = true
                            }
                        }

                    }

                }
            }


        }

        if (isPopUpActive.value){

            if(selectedThreeGroup.value.type == EVEN_TYPE.BADGEGOTTEN){

                val badge = listObtainedBadges.value.find{it.skillId + it.sectionId == selectedThreeGroup.value.stringId}!!

                BadgeCard(badge = badge, sharedViewModel = sharedViewModel) {
                    isPopUpActive.value = false
                }
            }

            else if(selectedThreeGroup.value.type == EVEN_TYPE.SKILLCREATED){
                val skill = listCreatedSkills.value.find{it.id == selectedThreeGroup.value.stringId}!!

                SkillInfoPopUp(skill, sharedViewModel){
                    isPopUpActive.value = false
                }
            }

            else if(selectedThreeGroup.value.type == EVEN_TYPE.SKILLFINISHEDFT){
                val skill = listFinishedSkills.value.find{it.id == selectedThreeGroup.value.stringId}!!

                SkillInfoPopUp(skill, sharedViewModel){
                    isPopUpActive.value = false
                }
            }

        }
    }
}


fun ComputeList(userSkillSubsModel: UserSkillSubsModel, listCreatedSkills: List<SkillModel>): List<ThreeGroup>{

    var listCreatedEvents = userSkillSubsModel.createdSkillsId.map { skillId ->
        ThreeGroup(
            EVEN_TYPE.SKILLCREATED,
            ZonedDateTime.parse(listCreatedSkills.find { it.id == skillId }!!.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            skillId
        )
    }

    var listBadgesEvent = userSkillSubsModel.badgesObtained.mapIndexed{ index, badgeId ->
        ThreeGroup(
            EVEN_TYPE.BADGEGOTTEN,
            ZonedDateTime.parse(userSkillSubsModel.timeBadgeObtained.get(index), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            badgeId
        )
    }

    var listSkillsFinishedFT = userSkillSubsModel.finishedSkills.mapIndexed{ index, skillId ->
        ThreeGroup(
            EVEN_TYPE.SKILLFINISHEDFT,
            ZonedDateTime.parse(userSkillSubsModel.timeFinishedFirstTime.get(index), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            skillId
        )
    }

    var result: List<ThreeGroup> = listCreatedEvents + listBadgesEvent + listSkillsFinishedFT;
    result = result.sortedByDescending { it.time }

    return result
}

@Composable
fun SkillInfoPopUp(
    skill: SkillModel,
    sharedViewModel: SharedViewModel,
    onCloseClick: () -> Unit,
) {
    val currentContext = LocalContext.current

    val sectionsList: MutableState<List<SkillSectionModel>> = remember {
        mutableStateOf(emptyList())
    }

    var tasksMap: MutableState<Map<String, List<SkillTaskModel>>> = remember {
        mutableStateOf(emptyMap())
    }


    LaunchedEffect(skill) {
        sharedViewModel.retrieveAllSkillSection(skill.id, currentContext) { sectionModels ->
            sectionsList.value = sectionModels.sortedWith { a, b ->
                if (skill.skillSectionsList.indexOf(a.id) < skill.skillSectionsList.indexOf(b.id)) -1 else 1
            }
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
            .background(Color.Gray.copy(alpha = 0.7f))
            .zIndex(10F)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Popup(
            alignment = Alignment.Center,

            properties = PopupProperties(
                excludeFromSystemGesture = true,
            ),

            // to dismiss on click outside
            onDismissRequest = { onCloseClick() }
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 0.dp, 0.dp, 20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.SpaceBetween
                ) {


                    val colorCircle = MaterialTheme.colorScheme.primary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0XFFF0F0F0),
                                RoundedCornerShape(10)
                            ) // Use the color of the background in your image
                            .border(1.dp, Color.Black, RoundedCornerShape(10))
                            .padding(horizontal = 20.dp, vertical = 17.dp),

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        /*Box(
                            modifier = Modifier
                                .size(50.dp) // Set the size of the circle
                                .background(colorCircle, shape = CircleShape) // Use the color of the circle in your image
                        )
                        Spacer(Modifier.width(25.dp))*/ // Space between the circle and the text
                        Column(modifier = Modifier.weight(2f)) {
                            Text(skill.titleSkill, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                            Text(text = "Creator: " + skill.creatorUserName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Text(
                            skill.skillSectionsList.count().toString() + " section" + if (skill.skillSectionsList.count() > 1) "s" else "",
                            color = Color.White,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .background(color = colorCircle, RoundedCornerShape(20))
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .weight(1f)
                        )
                    }


                    Column(modifier = Modifier
                        .fillMaxWidth(),) {
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
                                text = "Skill Description",
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp, 0.dp)
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(10))
                        ) {
                            Text(
                                text = skill.skillDescription,
                                modifier = Modifier.padding(15.dp),
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }

                    Column(modifier = Modifier
                        .fillMaxWidth(),) {
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
                                text = "Sections",
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
                        //SECTION ELEMENT

                        sectionsList.value.forEachIndexed { index, section ->
                            val indexOfCurrent = skill.skillSectionsList.indexOf(section.id)

                            val total = (tasksMap.value.get(section.id)?.map { entry ->
                                entry.requiredAmount
                            } ?: listOf(1)).sum()

                            val required = if(total == 0) 1 else total

                            SectionElementBlock(section, index,  false, 0, required)
                            {}

                            // THE TASKS

                            Column(modifier = Modifier
                                .fillMaxWidth(),) {
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

                                tasksMap.value.get(section.id)?.forEach {task->

                                    val am = 0
                                    val req = task.requiredAmount

                                    CustomProgressIndicator(task.taskDescription, am, req, 40.dp, false, false, false){}

                                }

                                Spacer(modifier = Modifier.height(50.dp))

                            }

                        }
                    }
                    
                    Button(onClick = onCloseClick) {
                        Text(text = "Close")
                    }
                    
                }
            }
        }
    }

}



class ThreeGroup(
    val type:EVEN_TYPE,
    val time: ZonedDateTime,
    val stringId: String
)