package com.example.app.models

class SkillCompleteStructureModel(
    var skillProgression: SkillProgressionModel,
    var skill: SkillModel,
    var skillSection: SkillSectionModel,
    var skillTasks: Map<SkillTaskModel, Pair<Int, Int>>
)