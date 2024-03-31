package com.example.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SkillSectionModel(
    var id: String = "",
    var idSkill: String = "",
    var titleSection: String = "",
    var descriptionSection: String = "",

    //var imageBadgeStep: Bitmap,

    var skillTasksList:List<String> = mutableListOf()
): Parcelable
