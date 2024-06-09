package com.example.app

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.app.screens.ScreenMain
import org.junit.Rule
import org.junit.Test

class IntegratedCreateScreenTest {
    @get:Rule
    val test = createComposeRule()

    private val loginButton = hasClickAction() and hasText("Login")
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testCreateScreenFunction(){
        test.setContent { ScreenMain() }

        test.onNodeWithTag("LoginPage").assertExists()

        test.onNodeWithTag("mailTextField").assertExists().performTextInput("abba@mail.com")
        test.onNodeWithTag("passwordTextField").assertExists().performTextInput("111111")
        test.onNode(loginButton).assertExists().assertIsEnabled().performClick()

        test.waitUntilAtLeastOneExists(hasText("Profile"), 15000)

        test.onNodeWithTag("ProfileScreen").assertExists()

        test.onNodeWithTag("Create").assertExists().performClick()
        test.onNodeWithTag("CreateScreen").assertExists()

        test.onNodeWithTag("MySkills").assertExists().performClick()
        test.onNodeWithText("Yes").assertExists().performClick()
        test.onNodeWithTag("MySkillsScreen").assertExists()

        test.onNodeWithTag("Create").assertExists().performClick()
        test.onNodeWithTag("CreateScreen").assertExists()

        test.onNodeWithContentDescription("Logout").assertExists().performClick()
        test.waitUntilAtLeastOneExists(hasTestTag("LoginPage"), 15000)
    }
}