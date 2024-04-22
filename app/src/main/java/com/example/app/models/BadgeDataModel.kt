package com.example.app.models

import android.os.Parcelable
import com.example.app.additionalUI.BadgeColor
import kotlinx.parcelize.Parcelize

@Parcelize
data class BadgeDataModel(
    val badgeColor: BadgeColor = BadgeColor.BRONZE,
    val badgeName: String = "",
    val skillId : String = "",
    val sectionId : String = "",
    val description : String = "",
    val date : String = "",
    val done : Boolean = true
) : Parcelable