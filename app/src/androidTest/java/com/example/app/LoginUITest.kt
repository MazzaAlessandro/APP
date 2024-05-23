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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app.screens.LoginScreen
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUITest {
    @get:Rule
    val test = createComposeRule()

    private val loginButton = hasClickAction() and hasText("Login")

    @Test
    fun testLoginUI(){
        test.setContent { LoginScreen(navController = rememberNavController(), sharedViewModel = SharedViewModel(
            UserRepository(), SkillRepository()
        )
        ) }

        test.onNode(loginButton).assertExists().assertIsNotEnabled()

        test.onNodeWithTag("mailTextField").assertExists().performTextInput("username")
        test.onNode(loginButton).assertExists().assertIsNotEnabled()

        test.onNodeWithTag("passwordTextField").assertExists().performTextInput("111111")
        test.onNode(loginButton).assertExists().assertIsEnabled()

        test.onNodeWithTag("GoogleButton").assertExists()

        test.onNodeWithText("Don't have an account?").assertExists()
        test.onNodeWithText("Create now!").assertExists()
    }
}