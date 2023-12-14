package com.example.app.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app.models.SkillModel

class SkillViewModel {
    private val _skillList = MutableLiveData<List<SkillModel>>()
    val skillList: LiveData<List<SkillModel>> = _skillList
}