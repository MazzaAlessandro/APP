package com.example.app

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
import com.example.app.additionalUI.BadgeBanner
import com.example.app.additionalUI.BadgeCard
import com.example.app.additionalUI.BadgeColor
import com.example.app.models.BadgeDataModel
import com.example.app.screens.BadgesScreen
import com.example.app.screens.ScreenMain
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Rule
import org.junit.Test

class BadgeScreenUITest {
    @get:Rule
    val test = createComposeRule()

    @Test
    fun testBadgeBannerWithoutDateUI() {
        test.setContent { BadgeBanner(badge = BadgeColor.BRONZE, skillName = "name", description = "description", date = "") {} }

        test.onNodeWithTag("Banner").assertExists().assertHasClickAction()
        test.onNodeWithText("name").assertExists()
        test.onNodeWithText("description").assertExists()
        test.onNodeWithText("Achieved on:").assertDoesNotExist()
        test.onNodeWithContentDescription("BronzeBadge").assertExists()
    }

    @Test
    fun testBadgeBannerWithDateUI(){
        test.setContent { BadgeBanner(badge = BadgeColor.SILVER, skillName = "new", description = "description", date = "22/01/21") {} }

        test.onNodeWithTag("Banner").assertExists().assertHasClickAction()
        test.onNodeWithText("new").assertExists()
        test.onNodeWithText("description").assertExists()
        test.onNodeWithText("Achieved on: 22/01/21").assertExists()
        test.onNodeWithContentDescription("SilverBadge").assertExists()
    }

    @Test
    fun testCardUI(){
        var badge = BadgeDataModel(
            BadgeColor.GOLD,
            "",
            "Badge Name",
            "",
            "",
            "Description",
            "22/12/21"
        )

        test.setContent { BadgeCard(badge = badge, sharedViewModel = SharedViewModel(
            UserRepository(), SkillRepository()
        ) ) {} }

        test.onNodeWithText("Creator: ").assertExists()
        test.onNodeWithText("Badge Name").assertExists()
        test.onNodeWithText("Description").assertExists()
        test.onNodeWithText("Close").assertExists()
        test.onNodeWithContentDescription("GoldBadge").assertExists()
        test.onNodeWithContentDescription("close").assertExists()
    }

    @Test
    fun testEmptyBadgeScreenUI(){
        test.setContent { BadgesScreen(navController = rememberNavController(), sharedViewModel = SharedViewModel(
            UserRepository(), SkillRepository()
        )
        ) }

        //check the existence of the AppToolBar
        test.onNodeWithTag("AppToolBar").assertExists()
        test.onNodeWithText("Total Badges").assertExists()
        test.onNodeWithContentDescription("Back").assertExists()
        test.onNodeWithContentDescription("Logout").assertExists()
        test.onNodeWithContentDescription("search").assertExists()

        test.onNodeWithText("You have not obtained a badge yet").assertExists()
        test.onNodeWithTag("SkillsButton").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testBadgeScreenUI(){
        test.setContent { ScreenMain() }

        test.onNodeWithTag("LoginPage").assertExists()

        test.onNodeWithTag("mailTextField").assertExists().performTextInput("abba@mail.com")
        test.onNodeWithTag("passwordTextField").assertExists().performTextInput("111111")
        test.onNode(hasClickAction() and hasText("Login")).assertExists().assertIsEnabled().performClick()

        test.waitUntilAtLeastOneExists(hasText("Profile"), 5000)

        test.onNodeWithTag("ProfileScreen").assertExists()
        test.onNodeWithTag("Badges").assertExists().performClick()

        test.waitUntilAtLeastOneExists(hasTestTag("BadgesScreen"), 5000)
        test.waitUntilDoesNotExist(hasText("You have not obtained a badge yet"), 5000)

        test.onNodeWithText("Read Dune - Halfway there").assertExists().performClick()
        test.onNodeWithContentDescription("close").assertExists().performClick()

        test.onNodeWithContentDescription("Logout").assertExists().performClick()
    }
}