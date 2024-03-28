package com.example.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SkillProgressionModel (var userId:String = "",
                             var skillId: String = "",
                             var currentSectionId: String = "",
                             var mapNonCompletedTasks: Map<String, Int> = mutableMapOf()): Parcelable
