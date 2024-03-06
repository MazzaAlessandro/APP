package com.example.app.util

import android.content.Context
import android.widget.Toast
import com.example.app.models.SkillProgressionModel
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
                            data(currentSkillListProg)
                        }

                        currentSkillListProgression.value = currentSkillListProg
                    } else {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                    }
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





}