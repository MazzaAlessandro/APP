package com.example.app.util

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.models.UserDataModel
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

    private var currentSkillListProgression: MutableStateFlow<List<SkillProgressionModel>> = MutableStateFlow(emptyList())
    var skillListProgression: StateFlow<List<SkillProgressionModel>> = currentSkillListProgression.asStateFlow();

    private var currentSkill: MutableStateFlow<SkillModel> = MutableStateFlow(SkillModel())
    var skill: StateFlow<SkillModel> = currentSkill.asStateFlow();

    private var currentSection: MutableStateFlow<SkillSectionModel> = MutableStateFlow(SkillSectionModel())
    var section: StateFlow<SkillSectionModel> = currentSection.asStateFlow();

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
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
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
        userId : String,
        context : Context,
        data: (SkillProgressionModel) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skillprogression")
            .document(userId + skillId)

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

    fun retrieveSkillPogressionList(
        userId : String,
        context : Context,
        data: (List<SkillProgressionModel>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("skillprogression")
            .whereEqualTo("userId", userId);

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

                                Toast.makeText(context, "WAWAWAWAAW", Toast.LENGTH_SHORT).show()
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
            .document(skillProgression.userId + skillProgression.skillId)

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







}