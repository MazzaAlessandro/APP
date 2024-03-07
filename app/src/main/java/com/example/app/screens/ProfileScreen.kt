package com.example.app.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.ProfileImage
import com.example.app.Routes
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.models.UserDataModel
import com.example.app.scrollingBanner.AnimatedPieChart
import com.example.app.scrollingBanner.PieChartData
import com.example.app.util.SharedViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(navController: NavHostController,
                  sharedViewModel: SharedViewModel
){
    val context = LocalContext.current

    val pieData = listOf(
        PieChartData("Completed Skills: ", 12, color = Color(0xFF6650a4)),
        PieChartData("Skills In progress: ", 5, color = Color(0xFF6650a4).copy(alpha = 0.75f)),
        PieChartData("Unstarted Skills: ", 9, color = Color.Gray.copy(alpha = 0.5f))
    )

    val userData = UserDataModel()

    if(FirebaseAuth.getInstance().currentUser != null && sharedViewModel.getCurrentUserMail().isBlank()){
        val currMail = FirebaseAuth.getInstance().currentUser?.email
        if (currMail!=null){
            sharedViewModel.setCurrentUserMail(currMail)
        }
    }

    val mail = sharedViewModel.getCurrentUserMail()

    sharedViewModel.retrieveUserData(
        mail,
        context
    ){
        data ->
        userData.username = data.username
    }

    Scaffold(
        topBar = { AppToolBar(title = "Profile", navController, sharedViewModel) },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImage("", false, 90.dp){
                }

                Text(text = "Username", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 35.sp))

                IconButton(
                    onClick = { navController.navigate(Routes.Update.route) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit"
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .padding(10.dp, 0.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            Text(text = "Badges earned", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))

            Row (modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    //sends to an extended list of all obtained badges
                    Log.println(Log.INFO, "badges", "click")
                }
                .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box(
                        modifier = Modifier
                            .size(75.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.75f))
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFCD7F32))
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(text = "34", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box(
                        modifier = Modifier
                            .size(75.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.75f))
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFC0C0C0))
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text("25", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box(
                        modifier = Modifier
                            .size(75.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.75f))
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD700))
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text("12", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                }
            }

            Divider(
                modifier = Modifier
                    .padding(10.dp, 0.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            Text(text = "Current progression", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedPieChart(
                    modifier = Modifier
                        .padding(10.dp),
                    pieDataPoints = pieData
                )

                Column (
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ){
                    pieData.map{
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier
                                .size(15.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(it.color))
                            Text(text = it.label, fontWeight = FontWeight.W600)
                            Text(text = it.value.toString())
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .padding(10.dp, 0.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            Text(text = "Statistics", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))

            Column(
                modifier = Modifier
                    .clickable {
                        //sends to an extended list of stats
                        Log.println(Log.INFO, "stats", "click")
                    }
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

            }

        }
    }
}
