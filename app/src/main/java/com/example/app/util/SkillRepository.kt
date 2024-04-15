package com.example.app.util

import android.content.Context
import android.widget.Toast
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserSkillSubsModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SkillRepository {

    private var currentSkillProgression: MutableStateFlow<SkillProgressionModel> = MutableStateFlow(SkillProgressionModel());
    var skillProgression: StateFlow<SkillProgressionModel> = currentSkillProgression.asStateFlow();

    private var currentUserSub: MutableStateFlow<UserSkillSubsModel> = MutableStateFlow(UserSkillSubsModel());
    var userSub: StateFlow<UserSkillSubsModel> = currentUserSub.asStateFlow();

    private var currentSkillListProgression: MutableStateFlow<List<SkillProgressionModel>> = MutableStateFlow(emptyList())
    var skillListProgression: StateFlow<List<SkillProgressionModel>> = currentSkillListProgression.asStateFlow();

    private var currentSkill: MutableStateFlow<SkillModel> = MutableStateFlow(SkillModel())
    var skill: StateFlow<SkillModel> = currentSkill.asStateFlow();

    private var currentSkillList: MutableStateFlow<List<SkillModel>> = MutableStateFlow(emptyList())
    var skillList: StateFlow<List<SkillModel>> = currentSkillList.asStateFlow();

    private var currentSection: MutableStateFlow<SkillSectionModel> = MutableStateFlow(SkillSectionModel())
    var section: StateFlow<SkillSectionModel> = currentSection.asStateFlow();

    private var currentSectionList: MutableStateFlow<List<SkillSectionModel>> = MutableStateFlow(
        emptyList()
    )
    var sectionList: StateFlow<List<SkillSectionModel>> = currentSectionList.asStateFlow();

    private var currentTask: MutableStateFlow<SkillTaskModel> = MutableStateFlow(SkillTaskModel())
    var task: StateFlow<SkillTaskModel> = currentTask.asStateFlow()

    private var currentSkillTaskList: MutableStateFlow<List<SkillTaskModel>> = MutableStateFlow(emptyList())
    var skillTaskList: StateFlow<List<SkillTaskModel>> = currentSkillTaskList.asStateFlow();


    fun retrieveSkill(
        skillId: String,
        context: Context,
        data: (SkillModel) -> Unit
    )= CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("skill")
            .document(skillId)

        try{
            fireStoreRef.get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val skillModelData = it.toObject<SkillModel>()!!
                        data(skillModelData)
                        currentSkill.value = skillModelData
                    } else {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveAllUserSkill(
        userEmail: String,
        context: Context,
        data: (List<SkillModel>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skill")
            .whereEqualTo("creatorEmail", userEmail);

        try{
            fireStoreRef.get()
                .addOnSuccessListener {documents ->
                    if (!documents.isEmpty){
                        val currentSkillListLoc : MutableList<SkillModel> = mutableListOf();

                        for(d in documents){

                            /*
                            d.toObject(SkillProgressionModel::class.java)?.let { skillprog ->
                                currentSkillListProgression.value += skillprog
                            }
                            */

                            currentSkillListLoc += d.toObject<SkillModel>()!!
                        }

                        data(currentSkillListLoc)
                        currentSkillList.value = currentSkillListLoc
                    } else {
                        Toast.makeText(context, "Data not HAHA found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    fun retrieveSkillsFromList(
        context: Context,
        listSkills: List<String>,
        data: (List<SkillModel>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skill")

        try{
            fireStoreRef.get()
                .addOnSuccessListener {documents ->
                    if (!documents.isEmpty){
                        val currentSkillListLoc : MutableList<SkillModel> = mutableListOf();

                        for(d in documents){

                            /*
                            d.toObject(SkillProgressionModel::class.java)?.let { skillprog ->
                                currentSkillListProgression.value += skillprog
                            }
                            */

                            if(d.toObject<SkillModel>()!!.id in listSkills){
                                currentSkillListLoc += d.toObject<SkillModel>()!!
                            }
                        }

                        data(currentSkillListLoc)
                        currentSkillList.value = currentSkillListLoc
                    } else {
                        Toast.makeText(context, "Data not HAHA found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

    }


        fun retrieveSkillSection(
        skillId: String,
        sectionId: String,
        context: Context,
        data: (SkillSectionModel) -> Unit
    )= CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("skillsection")
            .document(sectionId + skillId)

        try{
            fireStoreRef.get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val sectionModelData = it.toObject<SkillSectionModel>()!!
                        data(sectionModelData)
                        currentSection.value = sectionModelData
                    } else {
                        Toast.makeText(context, "Data not found SKILLSECT", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    fun retrieveAllSkillSection(
        skillId: String,
        context: Context,
        data: (List<SkillSectionModel>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skillsection")
            .whereEqualTo("idSkill", skillId);

        try{
            fireStoreRef.get()
                .addOnSuccessListener {documents ->
                    if (!documents.isEmpty){
                        val currentSkillSectionList : MutableList<SkillSectionModel> = mutableListOf();

                        for(d in documents){

                            /*
                            d.toObject(SkillProgressionModel::class.java)?.let { skillprog ->
                                currentSkillListProgression.value += skillprog
                            }
                            */

                            currentSkillSectionList += d.toObject<SkillSectionModel>()!!
                        }

                        data(currentSkillSectionList)
                        currentSectionList.value = currentSkillSectionList
                    } else {
                        Toast.makeText(context, "Data not SECT LIST found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    fun retrieveSkillTask(
        skillId: String,
        sectionId: String,
        taskId: String,
        context: Context,
        data: (SkillTaskModel) -> Unit
    )= CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("skilltask")
            .document(taskId + "_" + sectionId + skillId)

        try{
            fireStoreRef.get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val taskModelData = it.toObject<SkillTaskModel>()!!
                        data(taskModelData)
                        currentTask.value = taskModelData
                    } else {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveSkillProgression(
        skillId : String,
        userEmail : String,
        context : Context,
        data: (SkillProgressionModel) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skillprogression")
            .document(userEmail + skillId)

        try{
            fireStoreRef.get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val skillProgressionData = it.toObject<SkillProgressionModel>()!!
                        data(skillProgressionData)
                        currentSkillProgression.value = skillProgressionData
                    } else {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveUserSkillSub(
        userEmail : String,
        context : Context,
        data: (UserSkillSubsModel) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("usersub")
            .document(userEmail)

        try{
            fireStoreRef.get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val userSkillSub = it.toObject<UserSkillSubsModel>()!!
                        data(userSkillSub)
                        currentUserSub.value = userSkillSub
                    } else {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveSkillPogressionList(
        userEmail : String,
        context : Context,
        data: (List<SkillProgressionModel>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skillprogression")
            .whereEqualTo("userMail", userEmail);

        try{
            fireStoreRef.get()
                .addOnSuccessListener {documents ->
                    if (!documents.isEmpty){
                        val currentSkillListProg : MutableList<SkillProgressionModel> = mutableListOf();

                        for(d in documents){

                            /*
                            d.toObject(SkillProgressionModel::class.java)?.let { skillprog ->
                                currentSkillListProgression.value += skillprog
                            }
                            */

                            currentSkillListProg += d.toObject<SkillProgressionModel>()!!
                        }

                        data(currentSkillListProg)
                        currentSkillListProgression.value = currentSkillListProg
                    } else {
                        Toast.makeText(context, "Data not HAHA found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveAllSkillTasks(
        skillId: String,
        sectionId: String,
        taskIds: List<String>,
        context: Context,
        data: (List<SkillTaskModel>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skilltask")
            .whereEqualTo("idSkill", skillId)
            .whereEqualTo("idSection", sectionId)

        try{
            fireStoreRef.get()
                .addOnSuccessListener {documents ->
                    if (!documents.isEmpty){
                        val currentSkillTaskListF : MutableList<SkillTaskModel> = mutableListOf();

                        for(d in documents){

                            /*
                            d.toObject(SkillProgressionModel::class.java)?.let { skillprog ->
                                currentSkillListProgression.value += skillprog
                            }
                            */

                            if(taskIds.contains(d.toObject<SkillTaskModel>().id)){
                                currentSkillTaskListF += d.toObject<SkillTaskModel>()
                            }

                        }

                        data(currentSkillTaskListF)
                        currentSkillTaskList.value = currentSkillTaskListF
                    } else {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveSkill(
        skillData: SkillModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skill")
            .document(skillData.id)

        try {

            fireStoreRef.set(skillData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data!", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveUserSkillSub(
        userSub: UserSkillSubsModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("usersub")
            .document(userSub.userEmail)

        try {

            fireStoreRef.set(userSub)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data!", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveSkillSection(
        skillSectionData: SkillSectionModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skillsection")
            .document(skillSectionData.id + skillSectionData.idSkill)

        try {

            fireStoreRef.set(skillSectionData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data!", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveSkillTask(
        skillTaskData: SkillTaskModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skilltask")
            .document(skillTaskData.id + "_" +  skillTaskData.idSection + skillTaskData.idSkill)

        try {

            fireStoreRef.set(skillTaskData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data!", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveSkillProgression(
        skillProgression: SkillProgressionModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skillprogression")
            .document(skillProgression.userMail + skillProgression.skillId)

        try {

            fireStoreRef.set(skillProgression)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data!", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    /*
    fun updateSkillProgression(
        mail : String,
        context: Context,
        userData: UserDataModel
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("user")
            .document(mail)

        try{
            fireStoreRef.set(userData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data!", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }*/



    fun updateSkillProgression(userEmail: String,
                               skillId: String,
                               skillProgressionData: SkillProgressionModel,
                               context: Context
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("skillprogression")
            .document(userEmail + skillId)

        try{
            fireStoreRef.set(skillProgressionData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data!", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateUserSub(         userSub: UserSkillSubsModel,
                               context: Context
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("usersub")
            .document(userSub.userEmail)

        try{
            fireStoreRef.set(userSub)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data!", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }



}