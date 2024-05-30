package com.example.app

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.app.screens.SearchScreen
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Rule
import org.junit.Test

class SearchScreenUITest {
    @get:Rule
    val test = createComposeRule()

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

        //check the existence of the BottomNavigationBar
        test.onNodeWithTag("BottomNavigationBar").assertExists()
        test.onNodeWithTag("Search Skills").assertExists()
        test.onNodeWithTag("Profile").assertExists()
        test.onNodeWithTag("Create Skills").assertExists()
        test.onNodeWithTag("My Skills").assertExists()

    }
}