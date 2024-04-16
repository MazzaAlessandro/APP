package com.example.app.models

data class UserDataModel(
    var id : String = "",
    var username : String = "",
    var mail : String = "",
    var pfpUri : String = "",
    var listSkillProgressions: List<Int> = mutableListOf(0, 0, 0),
    var badgeCounter : List<Int> = mutableListOf(0, 0, 0)
)