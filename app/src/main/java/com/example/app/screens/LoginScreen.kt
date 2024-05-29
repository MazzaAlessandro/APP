package com.example.app.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.R
import com.example.app.Routes
import com.example.app.models.UserDataModel
import com.example.app.models.UserSkillSubsModel
import com.example.app.util.Constant
import com.example.app.util.SharedViewModel
import com.example.app.util.WindowInfo
import com.example.app.util.relative
import com.example.app.util.rememberWindowInfo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val windowInfo = rememberWindowInfo()
    val context = LocalContext.current
    val authenticating = remember { mutableStateOf(true) }
    val token = Constant.ServerClientId

    val notification = rememberSaveable { mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
    }

    var clicked by remember { mutableStateOf(false) }

    val colorCircle = MaterialTheme.colorScheme.primary

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        val task =
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    .getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var mail = task.result.user?.email
                            var username = task.result.user?.displayName

                            println(mail)
                            println(username)
                            println(FirebaseAuth.getInstance().currentUser!!.uid)

                            if(username==null)
                                username = "username"

                            if(mail!=null){
                                sharedViewModel.saveUserData(
                                    userData = UserDataModel(
                                        FirebaseAuth.getInstance().currentUser!!.uid,
                                        username,
                                        mail
                                    ),
                                    context
                                )

                                sharedViewModel.saveUserSub(UserSkillSubsModel(userEmail = mail), context)

                                authenticating.value = false
                                clicked = false
                                navController.navigate(Routes.Profile.route)
                                sharedViewModel.setCurrentUserMail(mail)
                            }
                        }
                        else{
                            authenticating.value = false
                            clicked = false
                        }
                    }
            }
            catch (e: ApiException) {
                Log.w("TAG", "GoogleSign in Failed", e)
            }
    }

    if(FirebaseAuth.getInstance().currentUser != null){
        navController.navigate(Routes.Profile.route)
    }
    else
        authenticating.value = false

    Box(
        modifier = Modifier.fillMaxSize().testTag("LoginPage"),
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

            Text(text = "Login", style = TextStyle(fontSize = 50.sp))

            Spacer(modifier = Modifier.height(relative(20.dp)))

            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                OutlinedTextField(
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18))
                        .testTag("mailTextField"),
                    label = { Text(text = "Mail") },
                    value = email.value,
                    onValueChange = { email.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
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
                OutlinedTextField(
                    modifier = Modifier.testTag("mailTextField"),
                    label = { Text(text = "Mail") },
                    value = email.value,
                    onValueChange = { email.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
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

            Spacer(modifier = Modifier.height(relative(10.dp)))

            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                OutlinedTextField(
                    modifier = Modifier
                        .width(windowInfo.screenWidth.div(2))
                        .height(windowInfo.screenHeight.div(18))
                        .testTag("passwordTextField"),
                    label = { Text(text = "Password")},
                    value = password.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
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
                OutlinedTextField(
                    modifier = Modifier.testTag("passwordTextField"),
                    label = { Text(text = "Password") },
                    value = password.value,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
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

            Spacer(modifier = Modifier.height(relative(30.dp)))

            if(windowInfo.screenWidthInfo == WindowInfo.WindowType.Expanded){
                Box(modifier = Modifier
                    .width(windowInfo.screenWidth.div(2))
                    .height(windowInfo.screenHeight.div(18))) {
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
                        shape = RoundedCornerShape(10.dp),
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
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Login", fontSize = 20.sp)
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

            Surface(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        if (!authenticating.value && !clicked) {
                            authenticating.value = true
                            clicked = true

                            val gso = GoogleSignInOptions
                                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(token)
                                .requestEmail()
                                .build()
                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            launcher.launch(googleSignInClient.signInIntent)
                        }
                    }
                    .testTag("GoogleButton"),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(width = 1.dp, color = Color.LightGray),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            end = 16.dp,
                            top = 12.dp,
                            bottom = 12.dp
                        )
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "Google Button",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (clicked) "Logging in..." else "Log In with Google")
                    if (clicked) {
                        Spacer(modifier = Modifier.width(16.dp))
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(16.dp)
                                .width(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth(), Alignment.BottomCenter){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = AnnotatedString("Not registered yet?"),
                    modifier = Modifier
                        .padding(5.dp, 0.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                    ),
                )

/*
                Button(onClick = {
                    navController.navigate(Routes.SignUp.route)
                    },

                    modifier = Modifier.padding(start = 10.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, colorCircle)
                    ) {
                    Text(
                        text = AnnotatedString("Join now!"),
                        modifier = Modifier,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Default,
                            color = colorCircle
                        )
                    )
                }

 */

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
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        color = colorCircle
                    )
                )


            }
        }

        if(authenticating.value)
            CircularProgressIndicator()
    }
}

