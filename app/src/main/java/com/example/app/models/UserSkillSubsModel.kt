package com.example.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSkillSubsModel(
    var userEmail: String = "",
    var startedSkillsIDs: List<String> = emptyList(),
    var registeredSkillsIDs: List<String> = emptyList()
) : Parcelable {
}