package com.example.app.additionalUI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BadgeIcon(
    badge : BadgeColor,
    size : Dp
){
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.Gray.copy(alpha = 0.75f))
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .clip(CircleShape)
                .background(badge.color)
        )
    }
}

@Composable
fun BadgeBanner(
    badge : BadgeColor,
    skillName : String,
    description : String,
    date : String = ""
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp, 2.dp)
        .clip(shape = RoundedCornerShape(10.dp))
        .background(Color.Gray.copy(alpha = 0.5f))){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically){
            BadgeIcon(badge, 60.dp)
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

                Text("Achieved on: $date", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
