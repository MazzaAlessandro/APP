package com.example.app.util

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SharedViewModel(): ViewModel() {

    private val currentUserMail = MutableStateFlow("")

    private val currentUserData = MutableStateFlow(UserData())

    fun setCurrentUserMail(
        mail : String
    ){
        currentUserMail.value = mail
    }

    fun resetSession(){
        setCurrentUserMail("")
        currentUserData.value
    }

    fun getCurrentUserMail(): String {
        return currentUserMail.value
    }

    fun getCurrentUsername(): String {
        return currentUserData.value.username
    }

    fun getCurrentUserPfpUri(): String {
        return  currentUserData.value.pfpUri
    }

    fun saveData(
        userData: UserData,
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
        userData: UserData
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
        data: (UserData) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("user")
            .document(mail)

        try{
            fireStoreRef.get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val userData = it.toObject<UserData>()!!
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