package com.example.app.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SkillModel(var id: String = "",
                      var creatorEmail: String = "",
                      var titleSkill: String = " ",
                      var skillDescription: String = "",
                      var skillSectionsList:List<String> = mutableListOf()) : Parcelable
{
    //constructor(skill: SkillModel, title: String) : this(skill.id, title, skill)
}
