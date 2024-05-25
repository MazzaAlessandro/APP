package com.example.app.util

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserDataModel
import com.example.app.models.UserSkillSubsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel(private val userRepository: UserRepository, private val skillRepository: SkillRepository): ViewModel() {

    var dialogShown by mutableStateOf(false)
        private set

    private val currentUserMail: StateFlow<String> = userRepository.userMail
    private val currentUserData : StateFlow<UserDataModel> = userRepository.userData

    private var userData : MutableStateFlow<UserDataModel> = MutableStateFlow(UserDataModel())
    private var userMail : MutableStateFlow<String> = MutableStateFlow("")

    private val currentUserSkillProgressions: StateFlow<List<SkillProgressionModel>> = skillRepository.skillListProgression

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

        userRepository.saveData(userData, context)

    }

    fun saveSkill(
        skillData: SkillModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        skillRepository.saveSkill(skillData, context)

    }

    fun saveSkillSection(
        skillSectionData: SkillSectionModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        skillRepository.saveSkillSection(skillSectionData, context)

    }


    fun saveSkillTask(
        skillTaskData: SkillTaskModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        skillRepository.saveSkillTask(skillTaskData, context)

    }

    fun saveSkillProgression(
        skillProgressionData: SkillProgressionModel,
        context: Context
    )= CoroutineScope(Dispatchers.IO).launch{

        skillRepository.saveSkillProgression(skillProgressionData, context)
    }

    fun updateUserData(
        mail : String,
        context: Context,
        userData: UserDataModel
    ) = CoroutineScope(Dispatchers.IO).launch{

        userRepository.updateData(mail, context, userData)

    }

    fun retrieveUserData(
        mail : String,
        context : Context,
        data: (UserDataModel) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        userRepository.retrieveData(mail, context, data)
    }

    fun retrieveProfileStats(
        mail : String,
        context : Context,
        data: (UserDataModel) -> Unit
    ){

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

        skillRepository.updateSkillProgression(userEmail, skillId, skillProgressionData, context)

    }

    fun updateUserSub(
        userSub: UserSkillSubsModel,
        context: Context
    ){
        skillRepository.updateUserSub(userSub, context)
    }

    fun saveUserSub(
        userSub: UserSkillSubsModel,
        context: Context,
    ){
        skillRepository.saveUserSkillSub(userSub, context)
    }

    fun saveBadgeData(
        badge: BadgeDataModel,
        context: Context
    ){
        skillRepository.saveBadgeData(badge, context)
    }

    fun retrieveUserSkillProgressionList(
        userEmail: String,
        context: Context,
        data: (List<SkillProgressionModel>) -> Unit

    ){
        skillRepository.retrieveSkillPogressionList(userEmail, context, data)
    }

    fun updateUserData(
        user: UserDataModel,
        context: Context,
    ){
        userRepository.saveData(user, context)
    }

    fun retrieveSkill(
        skillId: String,
        context: Context,
        data: (SkillModel) -> Unit
    ){
        skillRepository.retrieveSkill(skillId, context, data)
    }

    fun retrieveSkillsFromList(
        context: Context,
        listSkills: List<String>,
        data: (List<SkillModel>) -> Unit
    ){
        skillRepository.retrieveSkillsFromList(context, listSkills, data)
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

    fun retrieveAllSkillSection(
        skillId: String,
        context: Context,
        data: (List<SkillSectionModel>) -> Unit
    ){
        skillRepository.retrieveAllSkillSection(skillId, context, data)
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

    fun retrieveUserSkillSub(
        userEmail : String,
        context : Context,
        data: (UserSkillSubsModel) -> Unit
    ){
        skillRepository.retrieveUserSkillSub(userEmail, context, data)
    }

    fun retrieveBadge(
        skillId: String,
        sectionId: String,
        context: Context,
        data: (BadgeDataModel) -> Unit
    ){
        skillRepository.retrieveBadge(skillId, sectionId, context, data)
    }

    fun retrieveAllBadges(
        badgesList : List<String>,
        context: Context,
        data: (List<BadgeDataModel>) -> Unit
    ){
        skillRepository.retrieveAllBadges(badgesList, context, data)
    }




    fun unPublishSkill(
        skill: SkillModel,
        context: Context
    ){
        skillRepository.unPublishSkillModel(skill, context)
    }

    fun publishSkill(
        skill: SkillModel,
        context: Context
    ){
        skillRepository.publishSkillModel(skill, context)
    }



    fun removeSkillProgression(
        skillProgression: SkillProgressionModel,
        context: Context
    ){
        skillRepository.removeSkillProgression(skillProgression, context)
    }

    fun isSkillUnpublished(
        skill: SkillModel,
    ): Boolean{
        return skill.isPublic && (getCurrentUserMail() == skill.creatorEmail)
    }

    fun retrieveOnlineSkills(
        context: Context,
        data: (List<SkillModel>) -> Unit
    ){
        skillRepository.retrieveOnlineSkills(context, data)
    }

    fun isMySkill(
        skill: SkillModel
    ): Boolean{
        return skill.creatorEmail == getCurrentUserMail()
    }

    fun popUpOn(){
        dialogShown = true
    }

    fun popUpOff(){
        dialogShown = false
    }
}