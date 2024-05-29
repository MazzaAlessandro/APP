package com.example.app.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

class UserRepository{
    private var currentUserMail: MutableState<String> = mutableStateOf("")
    var userMail: State<String> = currentUserMail

    private var currentUserData: MutableState<UserDataModel> = mutableStateOf(UserDataModel())
    var userData: State<UserDataModel> = currentUserData


    fun saveData(
        userData: UserDataModel,
        context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("user")
            .document(userData.mail)

        try {

            fireStoreRef.set(userData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data!", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateData(
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
    }

    fun retrieveData(
        mail : String,
        context : Context,
        data: (UserDataModel) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("user")
            .document(mail)

        try{
            fireStoreRef.get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val userData = it.toObject<UserDataModel>()!!
                        data(userData)
                        currentUserData.value = userData
                    } else {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}
