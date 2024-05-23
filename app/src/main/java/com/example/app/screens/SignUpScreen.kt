package com.example.app.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.ProfileImage
import com.example.app.Routes
import com.example.app.models.UserDataModel
import com.example.app.models.UserSkillSubsModel
import com.example.app.util.SharedViewModel
import com.example.app.util.WindowInfo
import com.example.app.util.relative
import com.example.app.util.rememberWindowInfo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SignUpScreen(navController: NavHostController,
                 sharedViewModel: SharedViewModel
){
    val authenticating = remember { mutableStateOf(false) }

    val windowInfo = rememberWindowInfo()

    val context = LocalContext.current

    val notification = rememberSaveable { mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val username = remember { mutableStateOf(TextFieldValue()) }
            val mail = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }
            val passwordCheck = remember { mutableStateOf(TextFieldValue()) }
            val pfpUri = remember { MutableStateFlow("") }

            Text(text = "Registration", style = TextStyle(fontSize = 40.sp))

            ProfileImage("", true, relative(120.dp)){uri->
                pfpUri.value = uri
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded) {
                TextField(
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18))
                        .testTag("username"),
                    label = { Text(text = "Username") },
                    value = username.value,
                    onValueChange = { username.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {Icon(imageVector = Icons.Default.Person, contentDescription = "username")},
                    enabled = !authenticating.value
                )
            }
            else{
                TextField(
                    modifier = Modifier.testTag("username"),
                    label = { Text(text = "Username") },
                    value = username.value,
                    onValueChange = { username.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {Icon(imageVector = Icons.Default.Person, contentDescription = "username")},
                    enabled = !authenticating.value
                )
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded) {
                TextField(
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18))
                        .testTag("email"),
                    label = { Text(text = "Email") },
                    value = mail.value,
                    onValueChange = { mail.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {Icon(imageVector = Icons.Default.Mail, contentDescription = "email")},
                    enabled = !authenticating.value)
            }
            else{
                TextField(
                    modifier = Modifier.testTag("email"),
                    label = { Text(text = "Email") },
                    value = mail.value,
                    onValueChange = { mail.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {Icon(imageVector = Icons.Default.Mail, contentDescription = "email")},
                    enabled = !authenticating.value)
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded) {
                TextField(
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18))
                        .testTag("password"),
                    label = { Text(text = "Password") },
                    value = password.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {Icon(imageVector = Icons.Default.Lock, contentDescription = "password")},
                    onValueChange = { password.value = it },
                    enabled = !authenticating.value)
            }else{
                TextField(
                    modifier = Modifier.testTag("password"),
                    label = { Text(text = "Password") },
                    value = password.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {Icon(imageVector = Icons.Default.Lock, contentDescription = "password")},
                    onValueChange = { password.value = it },
                    enabled = !authenticating.value)
            }


            Spacer(modifier = Modifier.height(relative(20.dp)))

            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded) {
                TextField(
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18))
                        .testTag("confirmPassword"),
                    label = { Text(text = "Confirm Password") },
                    value = passwordCheck.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {Icon(imageVector = Icons.Default.Lock, contentDescription = "checkPassword")},
                    onValueChange = { passwordCheck.value = it },
                    enabled = !authenticating.value
                )
            }else{
                TextField(
                    modifier = Modifier.testTag("confirmPassword"),
                    label = { Text(text = "Confirm Password") },
                    value = passwordCheck.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {Icon(imageVector = Icons.Default.Lock, contentDescription = "checkPassword")},
                    onValueChange = { passwordCheck.value = it },
                    enabled = !authenticating.value
                )
            }


            Spacer(modifier = Modifier.height(relative(20.dp)))

            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded) {
                Box(modifier = Modifier
                    .width(windowInfo.screenWidth.div(2))
                    .height(windowInfo.screenHeight.div(18))) {
                    Button(
                        onClick = {
                            //checks if password and password check match.
                            //if yes, takes checks if username isn't already used
                            // if it's unique, registers the user and takes him to his new profile page
                            if(password.value.text != passwordCheck.value.text)
                            {
                                notification.value = "Please verify that the password match"
                            }
                            else
                            {
                                authenticating.value = true
                                //createUserInFirebase(username.value.text, mail.value.text, password.value.text)
                                FirebaseAuth
                                    .getInstance()
                                    .createUserWithEmailAndPassword(mail.value.text, password.value.text)
                                    .addOnCompleteListener {
                                        Log.d(TAG, "Inside OnCompleteListener")
                                        Log.d(TAG, "isSuccessful = ${it.isSuccessful}")

                                        if(it.isSuccessful){
                                            sharedViewModel.saveUserData(
                                                userData = UserDataModel(
                                                    FirebaseAuth.getInstance().currentUser!!.uid,
                                                    username.value.text,
                                                    mail.value.text,
                                                    pfpUri.value,
                                                ),
                                                context
                                            )
                                            sharedViewModel.setCurrentUserMail(mail.value.text)

                                            sharedViewModel.saveUserSub(UserSkillSubsModel(userEmail = mail.value.text), context)

                                            authenticating.value = false
                                            navController.navigate(Routes.Profile.route)
                                        }

                                    }
                                    .addOnFailureListener {
                                        authenticating.value = false
                                        Log.d(TAG, "Inside OnFailureListener")
                                        Log.d(TAG, "Exception = ${it.message}")
                                        Log.d(TAG, "Exception = ${it.localizedMessage}")
                                    }
                            }
                        },
                        enabled = password.value.text.isNotBlank()
                                && username.value.text.isNotBlank()
                                && mail.value.text.isNotBlank()
                                && passwordCheck.value.text.isNotBlank()
                                && !authenticating.value,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Register")
                    }
                }
            }else{
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {
                            //checks if password and password check match.
                            //if yes, takes checks if username isn't already used
                            // if it's unique, registers the user and takes him to his new profile page
                            if(password.value.text != passwordCheck.value.text)
                            {
                                notification.value = "Please verify that the password match"
                            }
                            else
                            {
                                authenticating.value = true
                                //createUserInFirebase(username.value.text, mail.value.text, password.value.text)
                                FirebaseAuth
                                    .getInstance()
                                    .createUserWithEmailAndPassword(mail.value.text, password.value.text)
                                    .addOnCompleteListener {
                                        Log.d(TAG, "Inside OnCompleteListener")
                                        Log.d(TAG, "isSuccessful = ${it.isSuccessful}")

                                        if(it.isSuccessful){
                                            sharedViewModel.saveUserData(
                                                userData = UserDataModel(
                                                    FirebaseAuth.getInstance().currentUser!!.uid,
                                                    username.value.text,
                                                    mail.value.text,
                                                    pfpUri.value,
                                                ),
                                                context
                                            )
                                            sharedViewModel.setCurrentUserMail(mail.value.text)

                                            sharedViewModel.saveUserSub(UserSkillSubsModel(userEmail = mail.value.text), context)

                                            authenticating.value = false
                                            navController.navigate(Routes.Profile.route)
                                        }

                                    }
                                    .addOnFailureListener {
                                        authenticating.value = false
                                        Log.d(TAG, "Inside OnFailureListener")
                                        Log.d(TAG, "Exception = ${it.message}")
                                        Log.d(TAG, "Exception = ${it.localizedMessage}")
                                    }
                            }
                        },
                        enabled = password.value.text.isNotBlank()
                                && username.value.text.isNotBlank()
                                && mail.value.text.isNotBlank()
                                && passwordCheck.value.text.isNotBlank()
                                && !authenticating.value,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Register")
                    }
                }
            }
        }

        if(authenticating.value)
            CircularProgressIndicator()
    }

    BackHandler {
        navController.navigate(Routes.Login.route)
    }
}
