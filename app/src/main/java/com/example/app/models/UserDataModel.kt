package com.example.app.models

data class UserDataModel(
    var id : String = "",
    var username : String = "",
    var mail : String = "",
    var pfpUri : String = "",
    var listSkillProgressions: List<String> = mutableListOf(),
    var badgeCounter : List<Int> = mutableListOf(0, 0, 0)
)