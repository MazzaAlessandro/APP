package com.example.app.models

import android.os.Parcelable
import com.example.app.additionalUI.BadgeColor
import kotlinx.parcelize.Parcelize

@Parcelize
data class BadgeDataModel(
    val badge: BadgeColor = BadgeColor.BRONZE,
    val skillId : String = "",
    val sectionId : String = "",
    val description : String = "",
    val date : String = "",
    val done : Boolean = true
) : Parcelable