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
import com.example.app.additionalUI.BadgeColor
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillModel
import com.example.app.screens.EVEN_TYPE
import com.example.app.screens.EventCard
import com.example.app.screens.HistBan_Badge
import com.example.app.screens.HistBan_SkillCrea
import com.example.app.screens.HistBan_SkillFin
import com.example.app.screens.HistoryScreen
import com.example.app.screens.ScreenMain
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Rule
import org.junit.Test
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HistoryScreenTest {
    @get:Rule
    val test = createComposeRule()

    @Test
    fun testHistoryScreenUI(){
        test.setContent { HistoryScreen(
            navController = rememberNavController(),
            sharedViewModel = SharedViewModel(UserRepository(), SkillRepository())
        ) }

        test.onNodeWithTag("HistoryScreen").assertExists()
        test.onNodeWithTag("BadgeSearch").assertExists()

        test.onNodeWithTag("AppToolBar").assertExists()
        test.onNodeWithText("History").assertExists()
        test.onNodeWithContentDescription("Back").assertExists()
        test.onNodeWithContentDescription("Logout").assertExists()
    }

    @Test
    fun testHistoryBadgeBanner(){
        val time = ZonedDateTime.now()

        test.setContent {
            HistBan_Badge(
                badge = BadgeDataModel(BadgeColor.BRONZE, "", "Badge", "", "", "Description"),
                dateTime = time) {}
        }

        test.onNodeWithTag("Banner").assertExists().assertHasClickAction()
        test.onNodeWithTag("timeStamp", true).assertExists()

        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).assertExists()
        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).assertExists()

        test.onNodeWithText("Badge Obtained").assertExists()
        test.onNodeWithContentDescription("BronzeBadge").assertExists()
        test.onNodeWithText("Badge").assertExists()
        test.onNodeWithText("Description").assertExists()
    }

    @Test
    fun testHistoryFinishedSkillBanner(){
        val time = ZonedDateTime.now()

        test.setContent {
            HistBan_SkillFin(
                skillModel = SkillModel("", "", "Skill", "Description"),
                dateTime = time) {}
        }

        test.onNodeWithTag("SkillFinBanner").assertExists().assertHasClickAction()
        test.onNodeWithTag("timeStamp", true).assertExists()

        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).assertExists()
        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).assertExists()

        test.onNodeWithText("Skill Finished").assertExists()
        test.onNodeWithText("Skill").assertExists()
        test.onNodeWithText("Description").assertExists()
    }

    @Test
    fun testHistoryCreatedSkillBanner(){
        val time = ZonedDateTime.now()

        test.setContent {
            HistBan_SkillCrea(
                skillModel = SkillModel("", "", "Skill", "Description"),
                dateTime = time) {}
        }

        test.onNodeWithTag("SkillCreatedBanner").assertExists().assertHasClickAction()
        test.onNodeWithTag("timeStamp", true).assertExists()

        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).assertExists()
        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).assertExists()

        test.onNodeWithText("Skill Created").assertExists()
        test.onNodeWithText("Skill").assertExists()
        test.onNodeWithText("Description").assertExists()
    }

    @Test
    fun testEventCardBadge(){
        val time = ZonedDateTime.now()

        test.setContent {
            EventCard(
                eventType = EVEN_TYPE.BADGEGOTTEN,
                skillModel = SkillModel("", "", "Skill", "Description"),
                badge = BadgeDataModel(BadgeColor.BRONZE, "", "Badge", "", "", "Description"),
                dateTime = time,
                onClick = {})
        }

        test.onNodeWithTag("Banner").assertExists().assertHasClickAction()
        test.onNodeWithTag("timeStamp", true).assertExists()

        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).assertExists()
        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).assertExists()

        test.onNodeWithText("Badge Obtained").assertExists()
        test.onNodeWithContentDescription("BronzeBadge").assertExists()
        test.onNodeWithText("Badge").assertExists()
        test.onNodeWithText("Description").assertExists()
    }

    @Test
    fun testEventCardSkillFin() {
        val time = ZonedDateTime.now()

        test.setContent {
            EventCard(
                eventType = EVEN_TYPE.SKILLFINISHEDFT,
                skillModel = SkillModel("", "", "Skill", "Description"),
                badge = BadgeDataModel(BadgeColor.BRONZE, "", "Badge", "", "", "Description"),
                dateTime = time,
                onClick = {})
        }

        test.onNodeWithTag("SkillFinBanner").assertExists().assertHasClickAction()
        test.onNodeWithTag("timeStamp", true).assertExists()

        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).assertExists()
        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).assertExists()

        test.onNodeWithText("Skill Finished").assertExists()
        test.onNodeWithText("Skill").assertExists()
        test.onNodeWithText("Description").assertExists()
    }

    @Test
    fun testEventCardSkillCreated() {
        val time = ZonedDateTime.now()

        test.setContent {
            EventCard(
                eventType = EVEN_TYPE.SKILLCREATED,
                skillModel = SkillModel("", "", "Skill", "Description"),
                badge = BadgeDataModel(BadgeColor.BRONZE, "", "Badge", "", "", "Description"),
                dateTime = time,
                onClick = {})
        }

        test.onNodeWithTag("SkillCreatedBanner").assertExists().assertHasClickAction()
        test.onNodeWithTag("timeStamp", true).assertExists()

        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).assertExists()
        test.onNodeWithText(time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).assertExists()

        test.onNodeWithText("Skill Created").assertExists()
        test.onNodeWithText("Skill").assertExists()
        test.onNodeWithText("Description").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun integratedHistoryScreenTest(){
        test.setContent { ScreenMain() }

        test.onNodeWithTag("LoginPage").assertExists()

        test.onNodeWithTag("mailTextField").assertExists().performTextInput("p@mail.com")
        test.onNodeWithTag("passwordTextField").assertExists().performTextInput("111111")
        test.onNode(hasClickAction() and hasText("Login")).assertExists().assertIsEnabled().performClick()

        test.waitUntilAtLeastOneExists(hasText("Profile"), 15000)
        test.onNodeWithTag("ProfileScreen").assertExists()

        test.onNodeWithTag("pieChart").assertExists().performClick()
        test.waitUntilAtLeastOneExists(hasTestTag("HistoryScreen"), 15000)

        test.waitUntilAtLeastOneExists(hasText("Badge Obtained"), 15000)

        test.onNodeWithTag("AppToolBar").assertExists()
        test.onNodeWithText("History").assertExists()
        test.onNodeWithContentDescription("Back").assertExists()
        test.onNodeWithContentDescription("Logout").assertExists()

        test.onNodeWithTag("HistoryScreen").assertExists()
        test.onNodeWithTag("BadgeSearch").assertExists().performTextInput("Abs")

        test.onNodeWithText("Abs workout").performClick()
        test.onNodeWithTag("PopUp").assertExists()
        test.onNodeWithContentDescription("close").performClick()
        test.onNodeWithTag("PopUp").assertDoesNotExist()

        test.onNodeWithContentDescription("Logout").assertExists().performClick()
        test.waitUntilAtLeastOneExists(hasTestTag("LoginPage"), 15000)
    }
}