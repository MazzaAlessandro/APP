package com.example.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class SkillProgressionModel (
    var userMail:String = "",
    var skillId: String = "",
    var currentSectionId: String = "",
    var mapNonCompletedTasks: Map<String, Int> = mutableMapOf(),
    var isFinished: Boolean = mapNonCompletedTasks.isEmpty(),
    var dateTime: String = ""
): Parcelable
