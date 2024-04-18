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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.example.app.R
import com.example.app.models.BadgeDataModel

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
                Text(text = skillName, fontSize = fontSize.sp, fontWeight = FontWeight.Bold)

                Text(text = description, fontSize = (fontSize + 1).sp, lineHeight = (fontSize + 1).sp)

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
                    BadgeIcon(badge.badgeColor, 100.dp, badge.done)

                    Text(
                        text = badge.badgeName,
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

enum class BannerSize(){
    SMALL,
    MEDIUM,
    LARGE
}

enum class BadgeColor(val color : Color){
    BRONZE(Color(0xFFCD7F32)),
    SILVER(Color(0xFFC0C0C0)),
    GOLD(Color(0xFFFFD700))
}
