package com.example.app

import androidx.compose.runtime.mutableStateOf
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
import androidx.navigation.compose.rememberNavController
import com.example.app.additionalUI.BadgeColor
import com.example.app.models.BadgeDataModel
import com.example.app.models.SkillCompleteStructureModel
import com.example.app.models.SkillModel
import com.example.app.models.SkillProgressionModel
import com.example.app.models.SkillSectionModel
import com.example.app.models.SkillTaskModel
import com.example.app.screens.MySkillsScreen
import com.example.app.screens.ScreenMain
import com.example.app.screens.SkillListElement
import com.example.app.screens.SkillTitleBlock
import com.example.app.screens.SortingType
import com.example.app.screens.TaskListElement
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Rule
import org.junit.Test

class MySkillsTest {
    @get:Rule
    val test = createComposeRule()

    @Test
    fun testSkillBlockTitle(){
        test.setContent {
            SkillTitleBlock(
                skillCompleteStructureModel = SkillCompleteStructureModel(
                    SkillProgressionModel(),
                    SkillModel(),
                    SkillSectionModel("", "", "Section Title", "This is a description!"),
                    mapOf(
                        SkillTaskModel("a") to Pair(2, 3),
                        SkillTaskModel("b") to Pair(1, 3)
                    )
                ),
                badge = BadgeDataModel(BadgeColor.BRONZE, "", "", " ")
            )
        }

        test.onNodeWithContentDescription("BronzeBadge").assertExists()
        test.onNodeWithText("Section Title").assertExists()
        test.onNodeWithText("This is a description!").assertExists()
        test.onNodeWithText("3/6").assertExists()
    }

    @Test
    fun testSkillListElement(){
        test.setContent {
            SkillListElement(
                SkillCompleteStructureModel(
                    SkillProgressionModel(),
                    SkillModel("", "", "Skill Title"),
                    SkillSectionModel("", "", "Section Title", "This is a description!"),
                    mapOf(
                        SkillTaskModel("a", "", "", "desc a", 8) to Pair(2, 3),
                        SkillTaskModel("b", "", "", "desc b", 8) to Pair(1, 3)
                    )
                ),
                2,
                SortingType.Custom,
                SharedViewModel(UserRepository(), SkillRepository()),
                {i, s ->},
                {},
                {},
                {i, b ->},
                {}
            )
        }

        test.onNodeWithContentDescription("ArrowUp").assertExists()
        test.onNodeWithContentDescription("ArrowDown").assertExists()
        test.onNodeWithContentDescription("MenuButton").assertExists()

        test.onNodeWithText("Skill Title").assertExists()

        test.onNodeWithContentDescription("EmptyBadge").assertExists()
        test.onNodeWithText("Section Title").assertExists()
        test.onNodeWithText("This is a description!").assertExists()
        test.onNodeWithText("3/6").assertExists()

        test.onNodeWithText("Tasks").assertExists()

        test.onNodeWithText("desc a").assertExists()
        test.onNodeWithText("2/8").assertExists()

        test.onNodeWithText("desc b").assertExists()
        test.onNodeWithText("1/8").assertExists()

        test.onNodeWithText("Finish").assertExists()
    }

    @Test
    fun taskListElementTest(){
        test.setContent { TaskListElement(
            3,
            SkillTaskModel("a", "", "", "desc a", 8)
        ) {}
        }

        test.onNodeWithText("desc a").assertExists()
        test.onNodeWithText("3/8").assertExists()
    }

    @Test
    fun mySkillsScreenUITest(){
        test.setContent { MySkillsScreen(navController = rememberNavController(), sharedViewModel = SharedViewModel(
            UserRepository(), SkillRepository()
        ), mutableStateOf(false) , mutableStateOf<String?>(null)
        ) }
        //check the existence of the AppToolBar
        test.onNodeWithTag("AppToolBar").assertExists()
        test.onNodeWithContentDescription("Logout").assertExists()

        test.onNodeWithTag("MySkillsScreen").assertExists()

        test.onNodeWithText("Order by: Custom").assertExists()
        test.onNodeWithContentDescription("OrderingArrow").assertExists()

        test.onNodeWithText("Order by: Custom").performClick()
        test.onNodeWithText("Date Asc").performClick()
        test.onNodeWithText("Order by: Date Asc").assertExists().performClick()
        test.onNodeWithText("Date Desc").performClick()
        test.onNodeWithText("Order by: Date Desc").assertExists()

        //check the existence of the BottomNavigationBar
        test.onNodeWithTag("BottomNavigationBar").assertExists()
        test.onNodeWithTag("Search Skills").assertExists()
        test.onNodeWithTag("Profile").assertExists()
        test.onNodeWithTag("Create Skills").assertExists()
        test.onNodeWithTag("My Skills").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun integratedMySkillsTest(){
        test.setContent { ScreenMain() }

        test.onNodeWithTag("LoginPage").assertExists()

        test.onNodeWithTag("mailTextField").assertExists().performTextInput("abba@mail.com")
        test.onNodeWithTag("passwordTextField").assertExists().performTextInput("111111")
        test.onNode(hasClickAction() and hasText("Login")).assertExists().assertIsEnabled().performClick()

        test.waitUntilAtLeastOneExists(hasText("Profile"), 5000)
        test.onNodeWithTag("ProfileScreen").assertExists()

        test.onNodeWithTag("My Skills").assertExists().performClick()
        test.onNodeWithTag("MySkillsScreen").assertExists()
        test.waitUntilAtLeastOneExists(hasTestTag("SkillListBlock"), 5000)

        test.onNodeWithContentDescription("Logout").assertExists().performClick()
    }
}