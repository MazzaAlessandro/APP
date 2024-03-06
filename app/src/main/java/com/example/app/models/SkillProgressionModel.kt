package com.example.app.models

class SkillProgressionModel (var userId:String = "",
                             var skillId: String = "",
                             var currentSectionNumber:Int = 0,
                             var mapNonCompletedTasks: Map<String, Int> = mutableMapOf())
