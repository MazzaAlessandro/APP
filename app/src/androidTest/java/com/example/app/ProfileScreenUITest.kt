package com.example.app

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
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
import androidx.navigation.compose.rememberNavController
import com.example.app.models.SkillModel
import com.example.app.screens.ProfileScreen
import com.example.app.screens.ScreenMain
import com.example.app.screens.SkillCard
import com.example.app.screens.SkillCardEmpty
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Rule
import org.junit.Test

class ProfileScreenUITest {
    @get:Rule
    val test = createComposeRule()

    @Test
    fun testSkillCard(){
        test.setContent {
            SkillCard(skill = SkillModel(
                "",
                "",
                "Skill Title",
                "Skill description",
                "",
                true,
                mutableListOf(
                    "Section 1",
                    "Section 2",
                    "Section 3"
                ),
                "username"
            )) {}
        }

        test.onNodeWithTag("SkillCard").assertExists().assertHasClickAction()
        test.onNodeWithContentDescription("SkillLogo").assertExists()
        test.onNodeWithText("Skill Title").assertExists()
        test.onNodeWithText("3 sections").assertExists()
        test.onNodeWithText("Creator: username").assertExists()
    }

    @Test
    fun testEmptySkillCard(){
        test.setContent {
            SkillCardEmpty {}
        }

        test.onNodeWithTag("EmptySkillCard").assertExists().assertHasClickAction()
        test.onNodeWithContentDescription("SkillLogo").assertExists()
        test.onNodeWithText("No skill started yet.").assertExists()
    }

    @Test
    fun testProfileScreenUI(){
        test.setContent { ProfileScreen(navController = rememberNavController(), sharedViewModel = SharedViewModel(
            UserRepository(), SkillRepository()
        ), mutableStateOf(false) , mutableStateOf<String?>(null)
        ) }

        //check the existence of the AppToolBar
        test.onNodeWithTag("AppToolBar").assertExists()
        test.onNodeWithContentDescription("Logout").assertExists()

        //check the existence of the BottomNavigationBar
        test.onNodeWithTag("BottomNavigationBar").assertExists()
        test.onNodeWithTag("Search Skills").assertExists()
        test.onNodeWithTag("Profile").assertExists()
        test.onNodeWithTag("Create Skills").assertExists()
        test.onNodeWithTag("My Skills").assertExists()

        test.onNodeWithContentDescription("profilePic").assertExists()
        test.onNodeWithContentDescription("Edit").assertExists()
        test.onNodeWithTag("ProfileInfo").assertExists()

        test.onNodeWithTag("Badges").assertExists()
        test.onNodeWithContentDescription("BronzeBadge").assertExists()
        test.onNodeWithContentDescription("SilverBadge").assertExists()
        test.onNodeWithContentDescription("GoldBadge").assertExists()

        test.onNodeWithTag("pieChart").assertExists()

        test.onNodeWithTag("LastSkillSection").assertExists()
        test.onNodeWithText("Last Started Skill").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testAllProfileNavigation(){
        test.setContent { ScreenMain() }

        test.onNodeWithTag("LoginPage").assertExists()

        test.onNodeWithTag("mailTextField").assertExists().performTextInput("abba@mail.com")
        test.onNodeWithTag("passwordTextField").assertExists().performTextInput("111111")
        test.onNode(hasClickAction() and hasText("Login")).assertExists().assertIsEnabled().performClick()

        test.waitUntilAtLeastOneExists(hasText("Profile"), 15000)
        test.onNodeWithTag("ProfileScreen").assertExists()

        test.onNodeWithTag("Badges").assertExists().performClick()
        test.waitUntilAtLeastOneExists(hasTestTag("BadgesScreen"), 15000)
        test.onNodeWithContentDescription("Back").assertExists().performClick()
        test.onNodeWithTag("ProfileScreen").assertExists()

        test.onNodeWithTag("pieChart").assertExists().performClick()
        test.waitUntilAtLeastOneExists(hasTestTag("HistoryScreen"), 15000)
        test.onNodeWithContentDescription("Back").assertExists().performClick()
        test.onNodeWithTag("ProfileScreen").assertExists()

        test.onNodeWithContentDescription("Edit").assertExists().performClick()
        test.waitUntilAtLeastOneExists(hasTestTag("ModifyAccountScreen"), 15000)
        test.onNodeWithContentDescription("Back").assertExists().performClick()
        test.onNodeWithTag("ProfileScreen").assertExists()

        test.onNodeWithContentDescription("Logout").assertExists().performClick()
        test.waitUntilAtLeastOneExists(hasTestTag("LoginPage"), 15000)
    }
}