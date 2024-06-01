package com.example.app.screens

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.ProfileImage
import com.example.app.Routes
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.models.UserDataModel
import com.example.app.ui.theme.PrimaryColor
import com.example.app.util.SharedViewModel
import com.example.app.util.WindowInfo
import com.example.app.util.relative
import com.example.app.util.rememberWindowInfo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyAccountScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val windowInfo = rememberWindowInfo()

    val updating = remember { mutableStateOf(false) }

    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppToolBar(
                title = "Update Profile",
                navController,
                sharedViewModel,
                true,
                Routes.Profile.route
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .testTag("ModifyAccountScreen"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val username = remember { mutableStateOf(TextFieldValue()) }
            val mail = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }
            val passwordCheck = remember { mutableStateOf(TextFieldValue()) }
            val pfpUri = remember { MutableStateFlow(sharedViewModel.getCurrentUserPfpUri()) }

            Spacer(modifier = Modifier.height(30.dp))
            
            Text(text = "Update account info", style = TextStyle(fontSize = 50.sp), textAlign = TextAlign.Center, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(relative(15.dp)))

            ProfileImage(
                sharedViewModel.getCurrentUserPfpUri(),
                true,
                relative(120.dp)
            ) { uri ->
                pfpUri.value = uri
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded) {
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = PrimaryColor,
                        focusedLabelColor = PrimaryColor,
                        focusedLeadingIconColor = PrimaryColor,
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18)),
                    label = { Text(text = "Username") },
                    value = username.value,
                    onValueChange = { username.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "username"
                        )
                    },
                    enabled = !updating.value
                )
            }
            else{
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = PrimaryColor,
                        focusedLabelColor = PrimaryColor,
                        focusedLeadingIconColor = PrimaryColor,
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Username") },
                    value = username.value,
                    onValueChange = { username.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "username"
                        )
                    },
                    enabled = !updating.value
                )
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded) {
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = PrimaryColor,
                        focusedLabelColor = PrimaryColor,
                        focusedLeadingIconColor = PrimaryColor,
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18)),
                    label = { Text(text = "New Password") },
                    value = password.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "password"
                        )
                    },
                    onValueChange = { password.value = it },
                    enabled = !updating.value
                )
            }
            else{
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = PrimaryColor,
                        focusedLabelColor = PrimaryColor,
                        focusedLeadingIconColor = PrimaryColor,
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "New Password") },
                    value = password.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "password"
                        )
                    },
                    onValueChange = { password.value = it },
                    enabled = !updating.value
                )
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded) {
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = PrimaryColor,
                        focusedLabelColor = PrimaryColor,
                        focusedLeadingIconColor = PrimaryColor,
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18)),
                    label = { Text(text = "Confirm Password") },
                    value = passwordCheck.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "checkPassword"
                        )
                    },
                    onValueChange = { passwordCheck.value = it },
                    enabled = !updating.value
                )
            }
            else{
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = PrimaryColor,
                        focusedLabelColor = PrimaryColor,
                        focusedLeadingIconColor = PrimaryColor,
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Confirm Password") },
                    value = passwordCheck.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "checkPassword"
                        )
                    },
                    onValueChange = { passwordCheck.value = it },
                    enabled = !updating.value
                )
            }


            Spacer(modifier = Modifier.height(relative(20.dp)))

            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded) {
                Box(
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18))
                ) {
                    Button(
                        onClick = {
                            if (password.value.text != passwordCheck.value.text) {
                                notification.value = "Please verify that the password match"
                            } else {
                                updating.value = true
                                FirebaseAuth.getInstance().currentUser!!
                                    .updatePassword(password.value.text)
                                    .addOnCompleteListener {
                                        sharedViewModel.updateUserData(
                                            sharedViewModel.getCurrentUserMail(),
                                            context,
                                            userData = UserDataModel(
                                                FirebaseAuth.getInstance().currentUser!!.uid,
                                                username.value.text,
                                                sharedViewModel.getCurrentUserMail(),
                                                pfpUri.value,
                                            ),
                                        )
                                        updating.value = false
                                        navController.navigate(Routes.Profile.route)
                                    }
                                    .addOnFailureListener {
                                        updating.value = false
                                    }


                            }
                        },
                        enabled = password.value.text.isNotBlank()
                                && username.value.text.isNotBlank()
                                && passwordCheck.value.text.isNotBlank()
                                && !updating.value,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Update info", fontSize = 20.sp)
                    }
                }
            } else {
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {
                            if (password.value.text != passwordCheck.value.text) {
                                notification.value = "Please verify that the password match"
                            } else {
                                updating.value = true
                                FirebaseAuth.getInstance().currentUser!!
                                    .updatePassword(password.value.text)
                                    .addOnCompleteListener {
                                        sharedViewModel.updateUserData(
                                            sharedViewModel.getCurrentUserMail(),
                                            context,
                                            userData = UserDataModel(
                                                FirebaseAuth.getInstance().currentUser!!.uid,
                                                username.value.text,
                                                sharedViewModel.getCurrentUserMail(),
                                                pfpUri.value,
                                            ),
                                        )
                                        updating.value = false
                                        navController.navigate(Routes.Profile.route)
                                    }
                                    .addOnFailureListener {
                                        updating.value = false
                                    }


                            }
                        },
                        enabled = password.value.text.isNotBlank()
                                && username.value.text.isNotBlank()
                                && passwordCheck.value.text.isNotBlank()
                                && !updating.value,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Update info", fontSize = 20.sp)
                    }
                }
            }


        }
    }


    BackHandler {
        navController.navigate(Routes.Profile.route)
    }
}