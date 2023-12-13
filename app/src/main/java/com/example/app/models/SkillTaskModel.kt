package com.example.app.models

class SkillTaskModel {

    var requiredAmount:Int = 0
    var currentAmount:Int = 0

    fun isCompleted():Boolean{
        return currentAmount <= requiredAmount
    }
}