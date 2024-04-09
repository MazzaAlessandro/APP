package com.example.app.models

import android.os.Parcelable
import com.example.app.additionalUI.BadgeColor
import kotlinx.parcelize.Parcelize

@Parcelize
data class BadgeData(
    val badge: BadgeColor,
    val skillName : String,
    val description : String,
    val date : String = "",
    val done : Boolean = true
) : Parcelable