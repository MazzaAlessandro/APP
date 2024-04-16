package com.example.app.models

data class UserDataModel(
    var id : String = "",
    var username : String = "",
    var mail : String = "",
    var pfpUri : String = "",
    var badgeCounter : List<Int> = mutableListOf(0, 0, 0)
)