package com.example.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SkillTaskModel (
    var id: String = "",
    var idSection: String = "",
    var idSkill: String = "",

    var taskDescription: String = "",

    var requiredAmount:Int = 1,
): Parcelable