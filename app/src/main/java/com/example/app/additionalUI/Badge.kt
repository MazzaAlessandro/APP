package com.example.app.additionalUI

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.example.app.screens.BadgeData

//This is the round icon of a Badge that can be used on its own
@Composable
fun BadgeIcon(
    badge : BadgeColor,
    size : Dp,
    filled : Boolean = true
){
    if(filled){
        Box(
            modifier = Modifier
                .size(size)
                .border(1.dp, Color.DarkGray.copy(alpha = 0.75f), CircleShape)
                .clip(CircleShape)
                .background(badge.color)
        )
    }
    else{
        Box(
            modifier = Modifier
                .size(size)
                .border(1.dp, Color.DarkGray.copy(alpha = 0.75f),CircleShape)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
        )
    }
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
    onClick: () -> Unit
){
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
            BadgeIcon(badge, 60.dp, done)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp, 0.dp, 5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = skillName, fontSize = 15.sp, fontWeight = FontWeight.Bold)

                Text(text = description, lineHeight = 15.sp)

                Spacer(modifier = Modifier.fillMaxHeight())

                if(done && date.isNotBlank()){
                    Text("Achieved on: $date", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
    badge: BadgeData,
    onCloseClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.5f))
            .zIndex(10F)
            .clickable {  },
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
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
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
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    BadgeIcon(badge.badge, 100.dp, badge.done)

                    Text(
                        text = badge.skillName,
                        modifier = Modifier
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = badge.description,
                        modifier = Modifier
                            .padding(15.dp, 0.dp, 15.dp, 10.dp),
                        textAlign = TextAlign.Center
                    )

                    if(badge.done){
                        val date = badge.date
                        Text(
                            "Achieved on: $date",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(15.dp, 10.dp, 15.dp, 20.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    else{
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                                .width(120.dp)
                        ) {
                            Text("START", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}
