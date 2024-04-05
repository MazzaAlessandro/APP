package com.example.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SkillCompleteStructureModel(
    var skillProgression: SkillProgressionModel,
    var skill: SkillModel,
    var skillSection: SkillSectionModel,
    var skillTasks: Map<SkillTaskModel, Pair<Int, Int>>
) : Parcelable