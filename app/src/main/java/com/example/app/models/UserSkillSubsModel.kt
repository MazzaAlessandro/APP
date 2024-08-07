package com.example.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSkillSubsModel(
    var userEmail: String = "",

    //the skill that he started even if they are finished
    var startedSkillsIDs: List<String> = emptyList(),

    //the skills he registered for without starting
    var registeredSkillsIDs: List<String> = emptyList(),
    var timeRegistered: List<String> = emptyList(),

    //the skills he finished
    var finishedSkills: List<String> = emptyList(),
    var timeFinishedFirstTime: List<String> = emptyList(),

    //the skills he created but didn't register nor start
    var createdSkillsId: List<String> = emptyList(),

    //the created badges
    var createdBadges: List<String> = emptyList(),

    //the obtained badges
    var badgesObtained: List<String> = emptyList(),
    var timeBadgeObtained: List<String> = emptyList(),

    //the custom ordering of the skills
    var customOrdering: List<String> = emptyList(),

) : Parcelable