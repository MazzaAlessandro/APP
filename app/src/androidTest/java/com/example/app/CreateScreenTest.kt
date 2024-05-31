package com.example.app

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.rememberNavController
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.screens.CreateScreen
import com.example.app.screens.GeneralInfoBox
import com.example.app.screens.SectionBox
import com.example.app.screens.TaskBox
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Rule
import org.junit.Test

class CreateScreenTest {
    @get:Rule
    val test = createComposeRule()

    @Test
    fun testGeneralInfoBox(){
        test.setContent { GeneralInfoBox(
            skill = SkillModel(),
            onTitleChange = {},
            onDescriptionChange = {},
            onCheckChange = {}
        ) }

        test.onNodeWithTag("GeneralInfoBox").assertExists()
        test.onNodeWithTag("TitleTextField").assertExists()
        test.onNodeWithTag("DescriptionTextField").assertExists()
        test.onNodeWithTag("PublicCheckBox").assertExists()
        test.onNodeWithText("General Information").assertExists()
        test.onNodeWithText("Enter a Description").assertExists()
        test.onNodeWithText("Enter a Title").assertExists()
        test.onNodeWithText("Title:").assertExists()
        test.onNodeWithText("Description:").assertExists()
        test.onNodeWithText("Public Skill").assertExists()
    }

    @Test
    fun testSectionBox(){
        test.setContent {
            SectionBox(
                id = 0,
                section = SkillSectionModel(),
                badge = BadgeDataModel(),
                listTasks = mutableListOf(),
                onTitleChange = {},
                onDescriptionChange = {},
                onAddTask = {},
                onDeleteSection = {},
                onChangeTaskDescription = {a, b ->},
                onChangeTaskAmount = {a, b ->},
                onDeleteTask = {a, b ->},
                onBadgeUpdate = {}
            )
        }

        test.onNodeWithTag("SectionBox0").assertExists()
        test.onNodeWithText("Section 1").assertExists()
        test.onNodeWithContentDescription("BadgeSelection").assertExists().assertHasClickAction()

        test.onNodeWithText("Name:").assertExists()
        test.onNodeWithTag("SectionNameTextField").assertExists()
        test.onNodeWithText("Enter a Name").assertExists()

        test.onNodeWithText("Description:").assertExists()
        test.onNodeWithTag("SectionDescriptionTextField").assertExists()
        test.onNodeWithText("Enter a Description").assertExists()

        test.onNodeWithText("Tasks").assertExists()
        test.onNodeWithText("0 element").assertExists()
        test.onNodeWithContentDescription("AddTask").assertExists()
        test.onNodeWithText("You need at least one task").assertExists()

        test.onNodeWithText("DELETE").assertExists().assertIsEnabled()
        test.onNodeWithText("DONE").assertExists().assertIsNotEnabled()
    }

    @Test
    fun testTaskBox(){
        test.setContent {
            TaskBox(
                id = 0,
                task = SkillTaskModel("", "", "", "", 1),
                onDescriptionChange = {},
                enabled = true,
                onAmountChange = {},
                onDeleteTask = {}
            )
        }

        test.onNodeWithTag("TaskCreate0").assertExists()
        test.onNodeWithTag("TaskDescriptionTextField0").assertExists()
        test.onNodeWithText("Task Description").assertExists()
        test.onNodeWithTag("AmountTextField0").assertExists()
        test.onNodeWithText("Times").assertExists()
        test.onNodeWithContentDescription("DeleteTask").assertExists()
    }

    @Test
    fun testCreateScreenUI(){
        test.setContent { CreateScreen(navController = rememberNavController(), sharedViewModel = SharedViewModel(
            UserRepository(), SkillRepository()
        ), mutableStateOf(false) , mutableStateOf<String?>(null)
        ) }

        //check the existence of the AppToolBar
        test.onNodeWithTag("AppToolBar").assertExists()
        test.onNodeWithContentDescription("Logout").assertExists()
        test.onNodeWithTag("CreateScreen").assertExists()

        test.onNodeWithText("Sections").assertExists()
        test.onNodeWithText("0 element").assertExists()
        test.onNodeWithText("You need at least one section to create a Skill").assertExists()
        test.onNodeWithText("ADD A SECTION").assertExists().assertIsEnabled()
        test.onNodeWithTag("SkillDoneButton").assertExists().assertIsNotEnabled()

        test.onNodeWithText("ADD A SECTION").performClick()
        test.onNodeWithText("1 element").assertExists()
        test.onNodeWithTag("SectionBox0").assertExists()
        test.onNodeWithTag("SkillDoneButton").performScrollTo().assertIsNotEnabled()

        //check the existence of the BottomNavigationBar
        test.onNodeWithTag("BottomNavigationBar").assertExists()
        test.onNodeWithTag("Search Skills").assertExists()
        test.onNodeWithTag("Profile").assertExists()
        test.onNodeWithTag("Create Skills").assertExists()
        test.onNodeWithTag("My Skills").assertExists()
    }
}