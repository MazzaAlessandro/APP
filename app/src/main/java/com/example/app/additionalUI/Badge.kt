package com.example.app.additionalUI

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.example.app.R
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.util.SharedViewModel

//This is the round icon of a Badge that can be used on its own
@Composable
fun BadgeIcon(
    badge : BadgeColor,
    size : Dp,
    filled : Boolean = true
){
    var id = R.drawable.empty

    if(filled){
        id = when (badge) {
            BadgeColor.GOLD -> R.drawable.gold
            BadgeColor.SILVER -> R.drawable.silver
            BadgeColor.BRONZE -> R.drawable.bronze
        }
    }

    Image(
        modifier = Modifier
            .size(size),
        painter = painterResource(id = id),
        contentDescription = "gold"
    )
}

//This is a banner that includes the icon, the name of the skill, the description of the badge and, if completed, the date of completion
//They're used in the Badges Screen, but ideally we could use them again for the skill description
@Composable
fun BadgeBanner(
    badge : BadgeColor,
    skillName : String,
    description : String,
    date : String,
    done: Boolean = true,
    size: BannerSize = BannerSize.MEDIUM,
    onClick: () -> Unit
){
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
            BadgeIcon(badge, iconSize, done)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp, 0.dp, 5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = skillName,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)

                Text(text = description,
                    fontSize = (fontSize + 1).sp,
                    lineHeight = (fontSize + 1).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.fillMaxHeight())

                if(done && date.isNotBlank()){
                    Text("Achieved on: $date", fontSize = (fontSize - 3).sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

//This is a card that basically shows the same things as the banner, but larger
//The idea is that this should pop up whenever you click on a badge banner
//If you did not completed the badge, the card has a button that takes you to that tutorial
@Composable
fun BadgeCard(
    badge: BadgeDataModel,
    sharedViewModel: SharedViewModel,
    onCloseClick: () -> Unit
){
    val context = LocalContext.current

    var skillInfo: MutableState<SkillModel> = remember {
        mutableStateOf(SkillModel())
    }

    var sectionInfo: MutableState<SkillSectionModel> = remember {
        mutableStateOf(SkillSectionModel())
    }

    var taskList: MutableState<List<SkillTaskModel>> = remember {
        mutableStateOf(listOf())
    }

    LaunchedEffect(badge) {
        sharedViewModel.retrieveSkill(
            badge.skillId,
            context,
        ){
            skillInfo.value = it
        }

        sharedViewModel.retrieveSkillSection(
            badge.skillId,
            badge.sectionId,
            context
        ){
            sectionInfo.value = it

            sharedViewModel.retrieveAllSkillTasks(
                badge.skillId,
                badge.sectionId,
                it.skillTasksList,
                context
            ){
                taskList.value = it
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
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.6f)
                    .clip(shape = RoundedCornerShape(25.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(25.dp))
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
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 10.dp)
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
                        BadgeIcon(badge.badgeColor, 75.dp, badge.done)
                        /*Box(
                        modifier = Modifier
                            .size(50.dp) // Set the size of the circle
                            .background(colorCircle, shape = CircleShape) // Use the color of the circle in your image
                    )
                    Spacer(Modifier.width(25.dp))*/ // Space between the circle and the text
                        Column(modifier = Modifier
                            .weight(2f)
                            .padding(horizontal = 10.dp),
                            horizontalAlignment = Alignment.Start) {
                            Text(badge.badgeName, fontWeight = FontWeight.Bold, fontSize = 30.sp, textAlign = TextAlign.Start)
                            Text(
                                text = "Obtained on: 20/12/2024" + badge.date,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "Creator: " + skillInfo.value.creatorUserName,
                                color = Color.White,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .background(color = colorCircle, RoundedCornerShape(20))
                                    .padding(vertical = 3.dp, horizontal = 4.dp)
                            )
                        }
                    }

                    
                    Column(modifier = Modifier
                        .padding(top = 10.dp)) {

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
                                text = "Badge Description",
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
                                .fillMaxSize()
                                .padding(15.dp, 0.dp)
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(10)),
                            contentAlignment = Alignment.Center

                        ) {
                            Text(
                                text = badge.description,
                                modifier = Modifier.padding(15.dp),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        }
                    }

                    //DETAILS ON SUCCESS

                    Column{
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
                                text = "Success Details",
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

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .fillMaxWidth()
                                .background(
                                    Color(0XFFF0F0F0),
                                    RoundedCornerShape(10)
                                ) // Use the color of the background in your image
                                .border(1.dp, Color.Black, RoundedCornerShape(10))
                                .padding(horizontal = 40.dp, vertical = 17.dp),

                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Text(
                                    text = "Skill: ",
                                    textAlign = TextAlign.Start,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = skillInfo.value.titleSkill,
                                    modifier = Modifier
                                        .weight(1.0f)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row {
                                Text(
                                    text = "Section " + skillInfo.value.skillSectionsList.indexOf(sectionInfo.value.id) + ": ",
                                    textAlign = TextAlign.Start,
                                    fontSize = 18.sp,
                                )
                                Text(
                                    text = sectionInfo.value.titleSection,
                                    modifier = Modifier
                                        .weight(1.0f)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                    fontSize = 18.sp,
                                )
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

enum class BannerSize {
    SMALL,
    MEDIUM,
    LARGE
}

enum class BadgeColor(val color : Color){
    BRONZE(Color(0xFFCD7F32)),
    SILVER(Color(0xFFC0C0C0)),
    GOLD(Color(0xFFFFD700))
}
