package com.example.app.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.util.SkillViewModel


@Composable
fun SkillDisplayScreen(skillViewModel: SkillViewModel){

}
@Composable
fun SkillHeader(bitmap: Bitmap, skillTitle: String){
    Column {
        Row {
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Skill image")
            Text(text = skillTitle)
        }

        // PROGRESSION STATUS
    }
}

/*
@Composable
fun StepSkill(skillStepModel : SkillSectionModel){
    Column {
        Row {

            Image(bitmap = skillStepModel.imageBadgeStep.asImageBitmap(), contentDescription = "Image of the Badge")

            Column {
                Text(text = skillStepModel.titleStep)
                Text(text = skillStepModel.descriptionStep)
            }
        }
        Column {
            for(task in skillStepModel.taskList){
                SkillTaskItem(skillTaskModel = task)
            }
        }
    }
}*/

@Composable
fun SkillTaskItem(skillTaskModel: SkillTaskModel){

    Column {
        Text(text = skillTaskModel.currentAmount.toString() + " / " + skillTaskModel.requiredAmount.toString())
        val progress = skillTaskModel.currentAmount.toFloat() / skillTaskModel.requiredAmount

        LinearProgressIndicator(progress = progress,modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))
    }

}