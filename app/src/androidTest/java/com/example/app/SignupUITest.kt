package com.example.app

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.app.screens.SignUpScreen
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Rule
import org.junit.Test

class SignupUITest {
    @get:Rule
    val test = createComposeRule()

    private val signUpButton = hasClickAction() and hasText("Register")

    @Test
    fun testLoginUI(){
        test.setContent { SignUpScreen(navController = rememberNavController(), sharedViewModel = SharedViewModel(
            UserRepository(), SkillRepository()
        )
        ) }

        test.onNodeWithText("Registration").assertExists()

        test.onNode(signUpButton).assertExists().assertIsNotEnabled()

        test.onNodeWithTag("username").assertExists().performTextInput("username")
        test.onNode(signUpButton).assertExists().assertIsNotEnabled()

        test.onNodeWithTag("email").assertExists().performTextInput("mail")
        test.onNode(signUpButton).assertExists().assertIsNotEnabled()

        test.onNodeWithTag("password").assertExists().performTextInput("111111")
        test.onNode(signUpButton).assertExists().assertIsNotEnabled()

        test.onNodeWithTag("confirmPassword").assertExists().performTextInput("111111")
        test.onNode(signUpButton).assertExists().assertIsEnabled()
    }
}