package com.example.app

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.app.additionalUI.BadgeBanner
import com.example.app.additionalUI.BadgeColor
import com.example.app.screens.BadgesScreen
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

    }
}