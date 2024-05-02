package com.example.app.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.type.DateTime
import kotlinx.android.parcel.Parcelize
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@Parcelize
data class SkillModel(
    var id: String = "",
    var creatorEmail: String = "",
    var titleSkill: String = " ",
    var skillDescription: String = "",
    var dateTime: String = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
    var skillSectionsList:List<String> = mutableListOf(),


) : Parcelable
