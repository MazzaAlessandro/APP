package com.example.app.util

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.UserDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel(private val userRepository: UserRepository, private val skillRepository: SkillRepository): ViewModel() {

    private val currentUserMail: StateFlow<String> = userRepository.userMail;
    private val currentUserData : StateFlow<UserDataModel> = userRepository.userData;

    private var userData : MutableStateFlow<UserDataModel> = MutableStateFlow(UserDataModel());
    private var userMail : MutableStateFlow<String> = MutableStateFlow("");

    private val currentUserSkillProgressions: StateFlow<List<SkillProgressionModel>> = skillRepository.skillListProgression;

    fun setCurrentUserMail(
        mail : String
    ){
        userMail.value = mail
    }

    fun resetSession(){
        setCurrentUserMail("")
        currentUserData.value
    }

    fun getCurrentUserMail(): String {
        return userMail.value
    }

    fun getCurrentUsername(): String {
        return currentUserData.value.username
    }

    fun getCurrentUserPfpUri(): String {
        return  currentUserData.value.pfpUri
    }

    fun saveUserData(
        userData: UserDataModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        userRepository.saveData(userData, context);

    }

    fun updateUserData(
        mail : String,
        context: Context,
        userData: UserDataModel
    ) = CoroutineScope(Dispatchers.IO).launch{

        userRepository.updateData(mail, context, userData);

    }

    // The result is in currentUserData
    fun retrieveUserData(
        mail : String,
        context : Context,
        data: (UserDataModel) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        userRepository.retrieveData(mail, context, data);
    }

    fun retrieveUserSkillProgressionList(
        userId: String,
        context: Context,
        data: (List<SkillProgressionModel>) -> Unit

    ){
        skillRepository.retrieveSkillPogressionList(userId, context, data)
    }

    fun retrieveSkill(
        skillId: String,
        context: Context,
        data: (SkillModel) -> Unit
    ){
        skillRepository.retrieveSkill(skillId, context, data)
    }
}