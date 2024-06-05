package com.example.app.util

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillCompleteStructureModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserDataModel
import com.example.app.models.UserSkillSubsModel
import com.example.app.screens.EVEN_TYPE
import com.example.app.screens.SelectedSkillState
import com.example.app.screens.SortingType
import com.example.app.screens.ThreeGroup
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max

class SharedViewModel(private val userRepository: UserRepository, private val skillRepository: SkillRepository): ViewModel() {

    var dialogShown by mutableStateOf(false)
        private set

    private val currentUserMail: State<String> = userRepository.userMail
    private val currentUserData : State<UserDataModel> = userRepository.userData

    private var userData : MutableState<UserDataModel> = mutableStateOf(UserDataModel())
    private var userMail : MutableState<String> = mutableStateOf("")

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
    ){

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
        userRepository.updateUserSub(userSub, context)
    }

    fun saveUserSub(
        userSub: UserSkillSubsModel,
        context: Context,
    ){
        userRepository.saveUserSkillSub(userSub, context)
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
        userRepository.retrieveUserSkillSub(userEmail, context, data)
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

    fun ComputeListTaskSub1(
        context: Context,
        task: SkillTaskModel,
        skillProgressionModel: SkillProgressionModel
    ): SkillProgressionModel {
        var result = skillProgressionModel

        var updatedMap = result.mapNonCompletedTasks.toMutableMap()

        var newAmount = 0

        if (task.id in result.mapNonCompletedTasks.keys) {
            newAmount = max((result.mapNonCompletedTasks.get(task.id) ?: 0) - 1, 0)
        } else {
            newAmount = task.requiredAmount - 1
        }

        updatedMap[task.id] = newAmount

        result = result.copy(mapNonCompletedTasks = updatedMap.toMap(), isFinished = false)

        return result
    }

    fun ComputeSkipSection(
        index: Int,
        listCompleteStructures: MutableList<SkillCompleteStructureModel>
    ): Boolean {

        var updatedList = listCompleteStructures
        var updatedStructure = updatedList.get(index)

        val indexOfSection =
            updatedStructure.skill.skillSectionsList.indexOf(updatedStructure.skillProgression.currentSectionId)

        val mustSkipSection: Boolean = updatedStructure.skillTasks.all {
            it.value.first == it.value.second
        } && (indexOfSection != updatedStructure.skill.skillSectionsList.size - 1)


        return mustSkipSection
    }


    fun ComputeListTaskAdd1(
        context: Context,
        index: Int,
        task: SkillTaskModel,
        listCompleteStructures: MutableList<SkillCompleteStructureModel>
    ): MutableList<SkillCompleteStructureModel> {
        var updatedList = listCompleteStructures
        var updatedStructure = updatedList.get(index)

        val basedNumber = updatedStructure.skillTasks[task]!!.first
        var updatedMap = updatedStructure.skillTasks.toMutableMap()

        updatedMap.set(task, Pair(minOf(basedNumber + 1, task.requiredAmount), task.requiredAmount))

        var updatedProgression = updatedStructure.skillProgression
        var updatedProgMap = updatedProgression.mapNonCompletedTasks.toMutableMap()

        if (basedNumber + 1 >= task.requiredAmount) {
            updatedProgMap.remove(task.id)
        } else {
            updatedProgMap.set(task.id, minOf(basedNumber + 1, task.requiredAmount))
        }

        if (updatedStructure.skill.skillSectionsList.indexOf(updatedStructure.skillProgression.currentSectionId) == updatedStructure.skill.skillSectionsList.size - 1 && updatedProgMap.isEmpty()) {
            updatedProgression = updatedProgression.copy(isFinished = true)
        }

        updatedProgression = updatedProgression.copy(mapNonCompletedTasks = updatedProgMap)
        updatedStructure =
            updatedStructure.copy(skillProgression = updatedProgression, skillTasks = updatedMap)

        updatedList.set(index, updatedStructure)

        return updatedList
    }

    fun FinishSkill(
        sharedViewModel: SharedViewModel,
        listCompleteStructures: MutableState<List<SkillCompleteStructureModel>>,
        userSkillSub: MutableState<UserSkillSubsModel>,
        currentStructureIndex: MutableState<Int>,
        badgeObtained: MutableState<BadgeDataModel>,
        isBadgePopUpOpen: MutableState<Boolean>,
        currentContext: Context
    ) {
        var updatedList = listCompleteStructures.value.toMutableList()
        var updatedStructure = updatedList.get(currentStructureIndex.value)


        var updatedProgression = updatedStructure.skillProgression

        val indexOfSection =
            updatedStructure.skill.skillSectionsList.indexOf(updatedProgression.currentSectionId)

        updatedProgression = updatedProgression.copy(isFinished = true)

        val it = userSkillSub.value

        val skillSection =
            listCompleteStructures.value.get(currentStructureIndex.value).skillSection

        var updatedBadges = it.badgesObtained
        var updatedTimeBadge = it.timeBadgeObtained
        var updatedFinishedSkills = it.finishedSkills
        var updatedTimesFinishSkills = it.timeFinishedFirstTime
        var updatedStartedSkills = it.startedSkillsIDs
        var updatedCustomOrder = it.customOrdering

        if (skillSection.hasBadge && !(skillSection.badgeID in updatedBadges)) {
            updatedBadges = updatedBadges + skillSection.badgeID
            updatedTimeBadge = updatedTimeBadge + ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            sharedViewModel.retrieveBadge(
                skillId = listCompleteStructures.value.get(currentStructureIndex.value).skill.id,
                sectionId = skillSection.id,
                currentContext,
            ){
                badgeObtained.value = it
                isBadgePopUpOpen.value = true
            }
        }

        if (updatedStructure.skill.id !in updatedFinishedSkills) {
            updatedFinishedSkills += updatedStructure.skill.id
            updatedTimesFinishSkills += ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }

        if (updatedStructure.skill.id in updatedStartedSkills) {
            updatedStartedSkills -= updatedStructure.skill.id
        }

        if (updatedStructure.skill.id in updatedCustomOrder) {
            updatedCustomOrder -= updatedStructure.skill.id
        }

        userSkillSub.value = it.copy(
            badgesObtained = updatedBadges,
            timeBadgeObtained = updatedTimeBadge,
            finishedSkills = updatedFinishedSkills,
            timeFinishedFirstTime = updatedTimesFinishSkills,
            startedSkillsIDs = updatedStartedSkills,
            customOrdering = updatedCustomOrder,
        )

        sharedViewModel.saveUserSub(
            userSkillSub.value,
            context = currentContext
        )

        val skillProgressionModel =
            listCompleteStructures.value.get(currentStructureIndex.value).skillProgression

        sharedViewModel.removeSkillProgression(skillProgressionModel, currentContext)
        listCompleteStructures.value -= listCompleteStructures.value.get(currentStructureIndex.value)

    }


    fun StopSkillProgressionMS(
        sharedViewModel: SharedViewModel,
        listCompleteStructures: MutableState<List<SkillCompleteStructureModel>>,
        userSkillSub: MutableState<UserSkillSubsModel>,
        currentStructureIndex: MutableState<Int>,
        currentContext: Context
    ) {
        var updatedList = listCompleteStructures.value.toMutableList()
        var updatedStructure = updatedList.get(currentStructureIndex.value)


        var updatedProgression = updatedStructure.skillProgression

        val indexOfSection =
            updatedStructure.skill.skillSectionsList.indexOf(updatedProgression.currentSectionId)

        val it = userSkillSub.value

        val skillSection =
            listCompleteStructures.value.get(currentStructureIndex.value).skillSection

        var updatedStartedSkills = it.startedSkillsIDs
        var updatedCustomOrder = it.customOrdering

        if (updatedStructure.skill.id in updatedStartedSkills) {
            updatedStartedSkills -= updatedStructure.skill.id
        }

        if (updatedStructure.skill.id in updatedCustomOrder) {
            updatedCustomOrder -= updatedStructure.skill.id
        }

        userSkillSub.value = it.copy(
            startedSkillsIDs = updatedStartedSkills,
            customOrdering = updatedCustomOrder,
        )

        sharedViewModel.saveUserSub(
            userSkillSub.value,
            context = currentContext
        )

        val skillProgressionModel =
            listCompleteStructures.value.get(currentStructureIndex.value).skillProgression

        sharedViewModel.removeSkillProgression(skillProgressionModel, currentContext)
        listCompleteStructures.value -= listCompleteStructures.value.get(currentStructureIndex.value)

    }

    fun RecomputeList(listCompleteStructures: List<SkillCompleteStructureModel>, sortingType: SortingType, customOrdering: List<String>): List<SkillCompleteStructureModel>{
        val result = listCompleteStructures.sortedWith(Comparator { a, b ->
            when(sortingType) {
                SortingType.Custom -> {
                    val indexA = customOrdering.indexOf(a.skill.id)
                    val indexB = customOrdering.indexOf(b.skill.id)
                    indexA.compareTo(indexB)
                }
                SortingType.DateAsc -> {
                    val dateA = ZonedDateTime.parse(a.skillProgression.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    val dateB = ZonedDateTime.parse(b.skillProgression.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    dateA.compareTo(dateB)
                }
                SortingType.DateDesc -> {
                    val dateA = ZonedDateTime.parse(a.skillProgression.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    val dateB = ZonedDateTime.parse(b.skillProgression.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    dateB.compareTo(dateA)  // Reverse the comparison for descending order
                }
            }
        })

        return result
    }
    fun StopSkillProgressionSS(
        sharedViewModel: SharedViewModel,
        skillProgression: SkillProgressionModel,
        skillModelsStarted: MutableState<List<SkillModel>>,
        onlineFetchedSkills: MutableState<List<SkillModel>>,
        skillProgressions: MutableState<List<SkillProgressionModel>>,
        isSkillSelected: MutableState<SelectedSkillState>,
        currentUserSkillSubs: MutableState<UserSkillSubsModel>,
        context: Context,
    ){
        sharedViewModel.removeSkillProgression(skillProgression, context)

        skillModelsStarted.value = skillModelsStarted.value.filter { it.id != skillProgression.skillId }

        onlineFetchedSkills.value = onlineFetchedSkills.value.filter { it.id != skillProgression.skillId }

        skillProgressions.value -= skillProgression

        isSkillSelected.value = SelectedSkillState.NOT_SELECTED

        var updatedStartedSkills = skillModelsStarted.value.map { it.id }
        var updatedCustomOrdering = currentUserSkillSubs.value.customOrdering.toMutableList()
        updatedCustomOrdering.remove(skillProgression.skillId)

        currentUserSkillSubs.value = currentUserSkillSubs.value.copy(startedSkillsIDs = updatedStartedSkills, customOrdering = updatedCustomOrdering)

        sharedViewModel.saveUserSub(currentUserSkillSubs.value, context)
    }

    fun GenerateNewSkillID(): String{
        val db = FirebaseFirestore.getInstance()
        val newSkillRef = db.collection("skill").document()
        return newSkillRef.id
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun SaveEverything(refId: String, sharedViewModel: SharedViewModel, context: Context, user: UserDataModel, skill: SkillModel, sections: List<SkillSectionModel>, tasks: Map<String, List<SkillTaskModel>>, badgeList: Map<String, BadgeDataModel>){

        val db = FirebaseFirestore.getInstance()
        val skillRef = db.collection("skill").document(refId)
        skillRef.set(skill.copy(creatorEmail = sharedViewModel.getCurrentUserMail(),
            skillDescription = skill.skillDescription.trim(),
            titleSkill = skill.titleSkill.trim(),
            dateTime = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            creatorUserName = user.username,
            skillSectionsList = sections.map{
                it.id
            }))

        badgeList.forEach{entry ->

            val section = sections.find { it.id == entry.key }

            val badgeEditted = entry.value.copy(
                badgeName = skill.titleSkill.trim() + " - " + section!!.titleSection.trim(),
                description = section!!.descriptionSection.trim()
            )

            sharedViewModel.saveBadgeData(badgeEditted, context)

        }

        sections.forEach{

            val taskList = (tasks[it.id]?.toMutableList() ?: mutableListOf()).map{
                it.id
            }

            val badgeID = badgeList.getOrDefault(it.id, BadgeDataModel())
            val hasBadge = badgeID.sectionId == it.id

            sharedViewModel.saveSkillSection(it.copy(skillTasksList = taskList, titleSection = it.titleSection.trim(), descriptionSection = it.descriptionSection.trim(), badgeID = badgeID.skillId + badgeID.sectionId, hasBadge = hasBadge), context)
        }

        tasks.values.flatten().forEach{
            sharedViewModel.saveSkillTask(it.copy(taskDescription = it.taskDescription.trim()), context)
        }


        sharedViewModel.retrieveUserSkillSub(sharedViewModel.getCurrentUserMail(), context){

            Toast.makeText(context, it.createdSkillsId.count().toString(), Toast.LENGTH_SHORT).show()

            val createdSkillsIdList = it.createdSkillsId + skill.id
            val createdBadgesIdList = it.createdBadges + badgeList.values.filter { it != BadgeDataModel() }.map { it.skillId + it.sectionId }
            sharedViewModel.updateUserSub(it.copy(createdSkillsId = createdSkillsIdList, createdBadges = createdBadgesIdList), context)
        }

    }

    fun ComputeList(userSkillSubsModel: UserSkillSubsModel, listCreatedSkills: List<SkillModel>, subString: String): List<ThreeGroup>{

        var listCreatedEvents = userSkillSubsModel.createdSkillsId.filter {str -> subString in listCreatedSkills.find { it.id == str }!!.titleSkill }.map { skillId ->
            ThreeGroup(
                EVEN_TYPE.SKILLCREATED,
                ZonedDateTime.parse(listCreatedSkills.find { it.id == skillId }!!.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                skillId
            )
        }

        var listBadgesEvent = userSkillSubsModel.badgesObtained.mapIndexed{ index, badgeId ->
            ThreeGroup(
                EVEN_TYPE.BADGEGOTTEN,
                ZonedDateTime.parse(userSkillSubsModel.timeBadgeObtained.get(index), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                badgeId
            )
        }

        var listSkillsFinishedFT = userSkillSubsModel.finishedSkills.mapIndexed{ index, skillId ->
            ThreeGroup(
                EVEN_TYPE.SKILLFINISHEDFT,
                ZonedDateTime.parse(userSkillSubsModel.timeFinishedFirstTime.get(index), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                skillId
            )
        }

        var result: List<ThreeGroup> = listCreatedEvents + listBadgesEvent + listSkillsFinishedFT;
        result = result.sortedByDescending { it.time }

        return result
    }


    fun LoadBadgeScreen(
        currentContext: Context,
        currentUserSkillSub: MutableState<UserSkillSubsModel>,
        badgeList: MutableState<List<BadgeDataModel>>
    ) {
        retrieveUserSkillSub(
            getCurrentUserMail(),
            currentContext,
        ) { userSkillSub ->

            currentUserSkillSub.value = userSkillSub

            retrieveAllBadges(
                userSkillSub.badgesObtained,
                currentContext,
            ) { badges ->
                badgeList.value = badges
            }

        }
    }

    fun LoadHistory(
        currentContext: Context,
        userSkillSub: MutableState<UserSkillSubsModel>,
        listCreatedSkills: MutableState<List<SkillModel>>,
        listFinishedSkills: MutableState<List<SkillModel>>,
        listObtainedBadges: MutableState<List<BadgeDataModel>>,
        listEvents: MutableState<List<ThreeGroup>>,
        eventTitleEditText: MutableState<String>
    ) {
        retrieveUserSkillSub(
            getCurrentUserMail(),
            currentContext
        ) { currUserSkillSub ->
            userSkillSub.value = currUserSkillSub

            retrieveSkillsFromList(
                currentContext,
                userSkillSub.value.createdSkillsId
            ) {
                listCreatedSkills.value = it
                retrieveSkillsFromList(
                    currentContext,
                    userSkillSub.value.finishedSkills
                ) {
                    listFinishedSkills.value = it
                    retrieveAllBadges(
                        userSkillSub.value.badgesObtained,
                        currentContext,
                    ) {
                        listObtainedBadges.value = it

                        listEvents.value = ComputeList(
                            currUserSkillSub,
                            listCreatedSkills.value,
                            eventTitleEditText.value
                        )
                    }
                }
            }


        }
    }

    fun LoadMySkills(
        listCompleteStructures: MutableState<List<SkillCompleteStructureModel>>,
        currentStructureIndex: MutableState<Int>,
        currentContext: Context,
        badgeObtained: MutableState<BadgeDataModel>,
        isBadgePopUpOpen: MutableState<Boolean>,
        userSkillSub: MutableState<UserSkillSubsModel>,
        canSkipSection: MutableState<Boolean>
    ) {
        var updatedList = listCompleteStructures.value.toMutableList()
        var updatedStructure = updatedList.get(currentStructureIndex.value)


        var updatedProgression = updatedStructure.skillProgression

        val indexOfSection =
            updatedStructure.skill.skillSectionsList.indexOf(updatedProgression.currentSectionId)

        val skillSection =
            listCompleteStructures.value.get(currentStructureIndex.value).skillSection

        val skill = listCompleteStructures.value.get(currentStructureIndex.value).skill.id


        //FIRST WE ADD THE BADGE
        retrieveUserSkillSub(getCurrentUserMail(), currentContext) {

            var updatedBadges = it.badgesObtained
            var updatedTimeBadge = it.timeBadgeObtained

            if (skillSection.hasBadge && !(skillSection.badgeID in updatedBadges)) {
                updatedBadges = updatedBadges + skillSection.badgeID
                updatedTimeBadge += ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

                retrieveBadge(
                    skillId = skill,
                    sectionId = skillSection.id,
                    currentContext,
                ) {
                    badgeObtained.value = it
                    isBadgePopUpOpen.value = true
                }

            }
            userSkillSub.value =
                it.copy(badgesObtained = updatedBadges, timeBadgeObtained = updatedTimeBadge)

            saveUserSub(
                userSkillSub.value,
                context = currentContext
            )

        }

        //SECOND WE UPDATE THE STRUCT
        updatedProgression.currentSectionId =
            updatedStructure.skill.skillSectionsList.get(indexOfSection + 1)

        retrieveSkillSection(
            updatedProgression.skillId,
            updatedProgression.currentSectionId,
            currentContext,
        ) { data ->
            updatedStructure.skillSection = data

            retrieveAllSkillTasks(
                updatedStructure.skill.id,
                updatedStructure.skillSection.id,
                updatedStructure.skillSection.skillTasksList,
                currentContext
            ) { listTasks ->

                updatedStructure = updatedStructure.copy(skillTasks = listTasks.associate {
                    Pair(it, Pair(0, it.requiredAmount))
                })

                updatedProgression =
                    updatedProgression.copy(mapNonCompletedTasks = listTasks.associate {
                        Pair(it.id, 0)
                    })

                saveSkillProgression(updatedProgression, currentContext)

                updatedStructure = updatedStructure.copy(skillProgression = updatedProgression)
                updatedList.set(currentStructureIndex.value, updatedStructure)
                listCompleteStructures.value = updatedList

                canSkipSection.value = true
            }
        }
    }

    fun LoadProfileScreen(
        context: Context,
        userData: UserDataModel,
        mail: String,
        userSkillSubsModel: MutableState<UserSkillSubsModel>,
        badges: MutableState<List<BadgeDataModel>>,
        skillProgressionList: MutableState<MutableList<SkillProgressionModel>>,
        lastSkillStarted: MutableState<SkillModel>,
        lastTimeStarted: MutableState<ZonedDateTime>
    ) {
        var userData1 = userData
        retrieveUserData(
            getCurrentUserMail(),
            context
        ) {
            userData1 = it

            retrieveUserSkillSub(
                mail,
                context
            ) {
                userSkillSubsModel.value = it

                retrieveAllBadges(
                    it.badgesObtained,
                    context
                ) {
                    badges.value = it
                }

                retrieveUserSkillProgressionList(
                    mail,
                    context
                ) {
                    skillProgressionList.value = it.toMutableList()

                    if (!it.isEmpty()) {
                        val id = skillProgressionList.value.minBy {
                            ZonedDateTime.parse(
                                it.dateTime,
                                DateTimeFormatter.ISO_OFFSET_DATE_TIME
                            )
                        }

                        retrieveSkill(id.skillId, context) {
                            lastSkillStarted.value = it
                            lastTimeStarted.value =
                                ZonedDateTime.parse(skillProgressionList.value.map { it.dateTime }
                                    .min(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        }
                    }
                }

            }

        }
    }


    fun LoadSearchScreen(
        currentContext: Context,
        skillProgressions: MutableState<List<SkillProgressionModel>>,
        skillModelsStarted: MutableState<List<SkillModel>>,
        currentUserSkillSubs: MutableState<UserSkillSubsModel>,
        skillModelsRegistered: MutableState<List<SkillModel>>,
        skillModelsCreated: MutableState<List<SkillModel>>
    ) {
        retrieveUserSkillProgressionList(
            getCurrentUserMail(),
            currentContext,
        ) {
            skillProgressions.value = it.sortedByDescending {
                ZonedDateTime.parse(it.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            }

            retrieveSkillsFromList(
                currentContext,
                it.map { it.skillId }
            ) {
                skillModelsStarted.value =
                    it.sortedBy { skillProgressions.value.map { it.skillId }.indexOf(it.id) }
            }

        }

        retrieveUserSkillSub(
            getCurrentUserMail(),
            currentContext,
        ) {
            currentUserSkillSubs.value = it



            retrieveSkillsFromList(
                currentContext,
                currentUserSkillSubs.value.registeredSkillsIDs
            ) {
                skillModelsRegistered.value = it.sortedByDescending {
                    ZonedDateTime.parse(
                        it.dateTime,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    )
                }
            }

            retrieveSkillsFromList(
                currentContext,
                currentUserSkillSubs.value.createdSkillsId
            ) {
                skillModelsCreated.value = it.sortedByDescending {
                    ZonedDateTime.parse(
                        it.dateTime,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    )
                }
            }
        }
    }


}