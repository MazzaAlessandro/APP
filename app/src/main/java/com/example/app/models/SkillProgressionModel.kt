package com.example.app.models

class SkillProgressionModel (var userId:String = "",
                             var skillId: String = "",
                             var currentSectionId: String = "",
                             var mapNonCompletedTasks: Map<String, Int> = mutableMapOf())
