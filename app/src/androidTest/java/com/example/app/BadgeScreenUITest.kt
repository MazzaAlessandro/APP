package com.example.app

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.app.additionalUI.BadgeBanner
import com.example.app.additionalUI.BadgeColor
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
    }

    @Test
    fun testBadgeBannerWithDateUI(){
        test.setContent { BadgeBanner(badge = BadgeColor.SILVER, skillName = "new", description = "description", date = "22/01/21") {} }

        test.onNodeWithTag("Banner").assertExists().assertHasClickAction()
        test.onNodeWithText("new").assertExists()
        test.onNodeWithText("description").assertExists()
        test.onNodeWithText("Achieved on: 22/01/21").assertExists()
    }
}