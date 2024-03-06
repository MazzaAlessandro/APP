package com.example.app.models

import android.graphics.Bitmap

class SkillStepModel {
    var titleStep: String = "Title step"
    var descriptionStep: String = "Title step"

    lateinit var imageBadgeStep: Bitmap;

    var taskList:List<SkillTaskModel> = mutableListOf();
}
