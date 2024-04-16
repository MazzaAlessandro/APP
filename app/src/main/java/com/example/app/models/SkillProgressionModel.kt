package com.example.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class  SkillProgressionModel (var userMail:String = "",
                             var skillId: String = "",
                             var currentSectionId: String = "",
                             var mapNonCompletedTasks: Map<String, Int> = mutableMapOf(),
                             var isFinished: Boolean = mapNonCompletedTasks.isEmpty()): Parcelable
