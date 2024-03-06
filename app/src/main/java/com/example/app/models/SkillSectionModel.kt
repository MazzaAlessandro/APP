package com.example.app.models

import android.graphics.Bitmap

class SkillSectionModel(
    var id: String = "",
    var idSkill: String = "",
    var titleStep: String = "Title step",
    var descriptionStep: String = "Title step",

    //var imageBadgeStep: Bitmap,

    var skillTasksList:List<String> = mutableListOf()
)
