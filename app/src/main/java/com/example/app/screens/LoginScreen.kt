package com.example.app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.Routes
import com.example.app.ui.theme.GoogleButton
import com.example.app.util.SharedViewModel
import com.example.app.util.WindowInfo
import com.example.app.util.relative
import com.example.app.util.rememberWindowInfo
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {

    val windowInfo = rememberWindowInfo()

    val authenticating = remember { mutableStateOf(true) }

    val notification = rememberSaveable { mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
    }

    if(FirebaseAuth.getInstance().currentUser != null){
        navController.navigate(Routes.Profile.route)
    }
    else
        authenticating.value = false

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,

    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val email = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }

            Text(text = "Login", style = TextStyle(fontSize = 40.sp))

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                TextField(
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18)),
                    label = { Text(text = "Mail") },
                    value = email.value,
                    onValueChange = { email.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Mail,
                            contentDescription = "email"
                        )
                    },
                    enabled = !authenticating.value
                )
            }
            else{
                TextField(
                    label = { Text(text = "Mail") },
                    value = email.value,
                    onValueChange = { email.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Mail,
                            contentDescription = "email"
                        )
                    },
                    enabled = !authenticating.value
                )
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                TextField(
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18)),
                    label = { Text(text = "Password")},
                    value = password.value,
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
                            contentDescription = "password"
                        )
                    },
                    onValueChange = { password.value = it },
                    enabled = !authenticating.value
                )
            }
            else{
                TextField(
                    label = { Text(text = "Password") },
                    value = password.value,
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
                            contentDescription = "password"
                        )
                    },
                    onValueChange = { password.value = it },
                    enabled = !authenticating.value
                )
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                Box(modifier = Modifier
                    .width(windowInfo.screenWidth.div(2))
                    .height(windowInfo.screenHeight.div(18))) {
                    Button(
                        onClick = {
                            //checks if the credentials are right.
                            //if yes, takes to profile page
                            //if not an error should appear
                            /*if (password.value.text == "PASSWORD" && email.value.text == "USERNAME") {
                                navController.navigate(Routes.Profile.route)
                            } else {
                                notification.value = "Either Username or Password is incorrect"
                            }*/
                            authenticating.value = true

                            FirebaseAuth
                                .getInstance()
                                .signInWithEmailAndPassword(email.value.text, password.value.text)
                                .addOnCompleteListener {
                                    if(it.isSuccessful){
                                        sharedViewModel.setCurrentUserMail(email.value.text)
                                        authenticating.value = false
                                        navController.navigate(Routes.Profile.route)
                                    }
                                }
                                .addOnFailureListener {
                                    authenticating.value = false
                                }

                        },
                        enabled = !password.value.text.isBlank()
                                && !email.value.text.isBlank()
                                && !authenticating.value,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Login", fontSize = 20.sp)
                    }
                }
            }
            else{
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {
                            authenticating.value = true

                            FirebaseAuth
                                .getInstance()
                                .signInWithEmailAndPassword(email.value.text, password.value.text)
                                .addOnCompleteListener {
                                    if(it.isSuccessful){
                                        sharedViewModel.setCurrentUserMail(email.value.text)
                                        authenticating.value = false
                                        navController.navigate(Routes.Profile.route)
                                    }
                                }
                                .addOnFailureListener {
                                    authenticating.value = false
                                }

                        },
                        enabled = !password.value.text.isBlank()
                                && !email.value.text.isBlank()
                                && !authenticating.value,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Login")
                    }
                }
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            Row(
                modifier = Modifier.width(windowInfo.screenWidth.div(2)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = Color.Gray,
                    thickness = 1.dp
                )

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "or",
                    fontSize = 18.sp,
                    color = Color.Gray
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = Color.Gray,
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(relative(20.dp)))

            GoogleButton(authenticating = authenticating.value,
                onClick = {

                })
        }

        Box(modifier = Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth(), Alignment.BottomCenter){
            Row {
                Text(
                    text = AnnotatedString("Don't have an account?"),
                    modifier = Modifier
                        .padding(5.dp, 0.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Default,
                    )
                )

                ClickableText(
                    text = AnnotatedString("Create now!"),
                    modifier = Modifier
                        .padding(5.dp, 0.dp),
                    onClick = {
                        //here we should implement the navigation to the Sign Up Screen
                        navController.navigate(Routes.SignUp.route)
                    },
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Default,
                        textDecoration = TextDecoration.Underline,
                        color = Color.Blue
                    )
                )
            }
        }

        if(authenticating.value)
            CircularProgressIndicator()
    }


}

