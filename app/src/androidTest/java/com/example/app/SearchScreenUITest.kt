package com.example.app

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.screens.ScreenMain
import com.example.app.screens.SearchScreen
import com.example.app.screens.SectionElementBlock
import com.example.app.screens.SelectedSkillState
import com.example.app.screens.SkillInfoPopUp_STARTED
import com.example.app.screens.SkillInfoPopUp_UNSTARTED
import com.example.app.screens.SkillSearchBlock
import com.example.app.screens.SkillSearchBlockUnavailable
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Rule
import org.junit.Test

class SearchScreenUITest {
    @get:Rule
    val test = createComposeRule()

    @Test
    fun testSectionBlock(){
        test.setContent {
            SectionElementBlock(
                section = SkillSectionModel("", "", "Section 1"),
                index = 1,
                isResetable = true,
                amount = 4,
                required = 15,
                onChangeSection = {}
            )
        }

        test.onNodeWithText("Section 1: Section 1").assertExists()
        test.onNodeWithText("In Progress").assertExists()
    }

    @Test
    fun testSectionBlockDone(){
        test.setContent {
            SectionElementBlock(
                section = SkillSectionModel("", "", "Section 1"),
                index = 1,
                isResetable = true,
                amount = 4,
                required = 4,
                onChangeSection = {}
            )
        }

        test.onNodeWithText("Section 1: Section 1").assertExists()
        test.onNodeWithText("Done").assertExists()
    }
    @Test
    fun testSkillSearchBlockUnavailable(){
        test.setContent {
            SkillSearchBlockUnavailable(
                skill = SkillModel(
                    "",
                    "",
                    "Skill Title",
                    "This is a description"
                )
            ) { }
        }

        test.onNodeWithText("Skill Title").assertExists()
        test.onNodeWithText("Creator: ").assertExists()
        test.onNodeWithText("This skill is not publicly available anymore. Click to remove it from the list.").assertExists()
    }

    @Test
    fun testSkillSearchBlockStarted(){
        test.setContent {
            SkillSearchBlock(
                skill = SkillModel(
                    "",
                    "",
                    "Skill Title",
                    "This is a description"
                ),
                selectedSkillState = SelectedSkillState.STARTED_SELECTED) {
            }
        }
        test.onNodeWithTag("SkillSearchBlock Skill Title").assertExists()

        test.onNodeWithContentDescription("SkillLogo").assertExists()
        test.onNodeWithText("Skill Title").assertExists()
        test.onNodeWithText("Creator: ").assertExists()
        test.onNodeWithText("0 section").assertExists()
        test.onNodeWithText("In Progress").assertExists()
    }

    @Test
    fun testSkillSearchBlockNew(){
        test.setContent {
            SkillSearchBlock(
                skill = SkillModel(
                    "",
                    "",
                    "Skill Title",
                    "This is a description"
                ),
                selectedSkillState = SelectedSkillState.NEW_SELECTED) {
            }
        }
        test.onNodeWithTag("SkillSearchBlock Skill Title").assertExists()

        test.onNodeWithContentDescription("SkillLogo").assertExists()
        test.onNodeWithText("Skill Title").assertExists()
        test.onNodeWithText("Creator: ").assertExists()
        test.onNodeWithText("0 section").assertExists()
        test.onNodeWithText("Not Started").assertExists()
    }

    @Test
    fun testSkillSearchBlockRegistered(){
        test.setContent {
            SkillSearchBlock(
                skill = SkillModel(
                    "",
                    "",
                    "Skill Title",
                    "This is a description"
                ),
                selectedSkillState = SelectedSkillState.REGISTERED_SELECTED) {
            }
        }
        test.onNodeWithTag("SkillSearchBlock Skill Title").assertExists()

        test.onNodeWithContentDescription("SkillLogo").assertExists()
        test.onNodeWithText("Skill Title").assertExists()
        test.onNodeWithText("Creator: ").assertExists()
        test.onNodeWithText("0 section").assertExists()
        test.onNodeWithText("Registered").assertExists()
    }

    @Test
    fun testSkillInfoPopUpStarted(){
        test.setContent { SkillInfoPopUp_STARTED(
            sharedViewModel = SharedViewModel(UserRepository(), SkillRepository()),
            skill = SkillModel(
                "",
                "",
                "Skill Title",
                "This is a description"
            ),
            skillProgression = SkillProgressionModel(),
            onSubTask = { _, _ ->},
            onResetSkillProgression = {  },
            onCloseClick = {  },
            onChangeSection = { },
            onStopSkillProgression = {},
            onUnpublishSkill = {},
            onPublishSkill = {},
            onDeleteSkill = {}
        ) }

        test.onNodeWithContentDescription("close").assertExists()

        test.onNodeWithText("Skill Title").assertExists()
        test.onNodeWithText("Creator: ").assertExists()
        test.onNodeWithText("0 section").assertExists()

        test.onNodeWithText("Skill Description").assertExists()
        test.onNodeWithText("This is a description").assertExists()
        test.onNodeWithText("Sections").assertExists()

        test.onNodeWithText("Restart").assertExists()
        test.onNodeWithText("Stop Skill").assertExists()
    }

    @Test
    fun testSkillInfoPopUpUnstartedNotRegistered(){
        test.setContent { SkillInfoPopUp_UNSTARTED(
            skill = SkillModel(
                "",
                "",
                "Skill Title",
                "This is a description"
            ),
            sharedViewModel = SharedViewModel(UserRepository(), SkillRepository()),
            isRegistered = false,
            onAddProgression = {  },
            onCloseClick = {  },
            onRegisterSkill = {  },
            onUnpublishSkill = {  },
            onPublishSkill = {  },
            onDeleteSkill = {  }
        ) }

        test.onNodeWithContentDescription("close").assertExists()

        test.onNodeWithText("Skill Title").assertExists()
        test.onNodeWithText("Creator: ").assertExists()
        test.onNodeWithText("0 section").assertExists()

        test.onNodeWithText("Skill Description").assertExists()
        test.onNodeWithText("This is a description").assertExists()
        test.onNodeWithText("Sections").assertExists()

        test.onNodeWithText("Register Skill").assertExists()
    }

    @Test
    fun testSkillInfoPopUpUnstartedRegistered(){
        test.setContent { SkillInfoPopUp_UNSTARTED(
            skill = SkillModel(
                "",
                "",
                "Skill Title",
                "This is a description"
            ),
            sharedViewModel = SharedViewModel(UserRepository(), SkillRepository()),
            isRegistered = true,
            onAddProgression = {  },
            onCloseClick = {  },
            onRegisterSkill = {  },
            onUnpublishSkill = {  },
            onPublishSkill = {  },
            onDeleteSkill = {  }
        ) }

        test.onNodeWithContentDescription("close").assertExists()

        test.onNodeWithText("Skill Title").assertExists()
        test.onNodeWithText("Creator: ").assertExists()
        test.onNodeWithText("0 section").assertExists()

        test.onNodeWithText("Skill Description").assertExists()
        test.onNodeWithText("This is a description").assertExists()
        test.onNodeWithText("Sections").assertExists()

        test.onNodeWithText("Start Skill").assertExists()
    }

    @Test
    fun testSearchScreenUI(){
        test.setContent { SearchScreen(navController = rememberNavController(), sharedViewModel = SharedViewModel(
            UserRepository(), SkillRepository()
        ), mutableStateOf(false) , mutableStateOf<String?>(null)
        ) }

        //check the existence of the AppToolBar
        test.onNodeWithTag("AppToolBar").assertExists()
        test.onNodeWithContentDescription("Logout").assertExists()

        test.onNodeWithTag("SearchBar").assertExists()

        test.onNodeWithTag("StartedSkillsDivider").assertExists()
        test.onNodeWithText("Started Skills").assertExists()
        test.onNodeWithText("0 elements")

        test.onNodeWithTag("RegisteredSkillsDivider").assertExists()
        test.onNodeWithText("Registered Skills").assertExists()
        test.onNodeWithText("0 elements")

        test.onNodeWithTag("CreatedSkillsDivider").assertExists()
        test.onNodeWithText("Created Skills").assertExists()
        test.onNodeWithText("0 elements")

        test.onNodeWithText("Search Online").assertExists()

        //check the existence of the BottomNavigationBar
        test.onNodeWithTag("BottomNavigationBar").assertExists()
        test.onNodeWithTag("Search").assertExists()
        test.onNodeWithTag("Profile").assertExists()
        test.onNodeWithTag("Create").assertExists()
        test.onNodeWithTag("MySkills").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun integratedSearchScreenTest(){
        test.setContent { ScreenMain() }

        test.onNodeWithTag("LoginPage").assertExists()

        test.onNodeWithTag("mailTextField").assertExists().performTextInput("abba@mail.com")
        test.onNodeWithTag("passwordTextField").assertExists().performTextInput("111111")
        test.onNode(hasClickAction() and hasText("Login")).assertExists().assertIsEnabled().performClick()

        test.waitUntilAtLeastOneExists(hasText("Profile"), 15000)
        test.onNodeWithTag("ProfileScreen").assertExists()

        test.onNodeWithTag("Search").assertExists().performClick()
        test.onNodeWithTag("SearchScreen").assertExists()
        test.waitUntilAtLeastOneExists(hasContentDescription("SkillLogo"), 15000)

        test.onNodeWithTag("SearchBar").performTextInput("Basic cooking")
        test.waitUntilAtLeastOneExists(hasTestTag("SkillSearchBlock Basic cooking"), 15000)
        Espresso.closeSoftKeyboard()

        test.onNode(hasTestTag("SkillSearchBlock Basic cooking")).performClick()

        test.onNodeWithText("Register Skill").assertExists()
        test.onNodeWithContentDescription("close").assertExists().performClick()

        test.onNodeWithContentDescription("Logout").assertExists().performClick()
        test.waitUntilAtLeastOneExists(hasTestTag("LoginPage"), 15000)
    }
}