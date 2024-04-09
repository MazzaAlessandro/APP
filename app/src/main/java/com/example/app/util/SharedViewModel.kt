package com.example.app.util

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel(private val userRepository: UserRepository, private val skillRepository: SkillRepository): ViewModel() {

    var dialogShown by mutableStateOf(false)
        private set

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

    fun saveSkill(
        skillData: SkillModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        skillRepository.saveSkill(skillData, context);

    }

    fun saveSkillSection(
        skillSectionData: SkillSectionModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        skillRepository.saveSkillSection(skillSectionData, context);

    }


    fun saveSkillTask(
        skillTaskData: SkillTaskModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        skillRepository.saveSkillTask(skillTaskData, context);

    }

    fun saveSkillProgression(
        skillProgressionData: SkillProgressionModel,
        context: Context
    )= CoroutineScope(Dispatchers.IO).launch{

        skillRepository.saveSkillProgression(skillProgressionData, context);
    }

    fun updateUserData(
        mail : String,
        context: Context,
        userData: UserDataModel
    ) = CoroutineScope(Dispatchers.IO).launch{

        userRepository.updateData(mail, context, userData);

    }

    fun retrieveUserData(
        mail : String,
        context : Context,
        data: (UserDataModel) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        userRepository.retrieveData(mail, context, data);
    }

    fun retrieveSkillProgression(
        userEmail: String,
        skillId: String,
        context: Context,
        data: (SkillProgressionModel) -> Unit
    ){
        skillRepository.retrieveSkillProgression(skillId, userEmail, context, data)
    }

    fun updateSkillProgression(
        userEmail: String,
        skillId: String,
        skillProgressionData: SkillProgressionModel,
        context: Context,
        ){

        skillRepository.updateSkillProgression(userEmail, skillId, skillProgressionData, context);

    }

    fun retrieveUserSkillProgressionList(
        userEmail: String,
        context: Context,
        data: (List<SkillProgressionModel>) -> Unit

    ){
        skillRepository.retrieveSkillPogressionList(userEmail, context, data)
    }

    fun retrieveSkill(
        skillId: String,
        context: Context,
        data: (SkillModel) -> Unit
    ){
        skillRepository.retrieveSkill(skillId, context, data)
    }

    fun retrieveAllUserSkill(
        userEmail: String,
        context: Context,
        data: (List<SkillModel>) -> Unit
    ){
        skillRepository.retrieveAllUserSkill(userEmail, context, data)
    }

    fun retrieveSkillSection(
        skillId: String,
        sectionId: String,
        context: Context,
        data: (SkillSectionModel) -> Unit
    ){
        skillRepository.retrieveSkillSection(skillId, sectionId, context, data)
    }

    fun retrieveSkillTask(
        skillId: String,
        sectionId: String,
        taskId: String,
        context: Context,
        data: (SkillTaskModel) -> Unit
    ){
        skillRepository.retrieveSkillTask(skillId, sectionId, taskId, context, data)
    }

    fun retrieveAllSkillTasks(
        skillId: String,
        sectionId: String,
        taskIds: List<String>,
        context: Context,
        data: (List<SkillTaskModel>) -> Unit
    ){
        skillRepository.retrieveAllSkillTasks(skillId, sectionId, taskIds, context, data)
    }

    fun popUpOn(){
        dialogShown = true
    }

    fun popUpOff(){
        dialogShown = false
    }
}