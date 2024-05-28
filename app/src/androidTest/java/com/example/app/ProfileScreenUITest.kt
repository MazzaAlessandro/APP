package com.example.app

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import com.example.app.screens.ProfileScreen
import com.example.app.util.SharedViewModel
import com.example.app.util.SkillRepository
import com.example.app.util.UserRepository
import org.junit.Rule
import org.junit.Test

class ProfileScreenUITest {
    @get:Rule
    val test = createComposeRule()

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
    }
}