package com.example.app.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.ProfileImage
import com.example.app.bottomNavigation.AppToolBar
import com.example.app.bottomNavigation.BottomNavigationBar
import com.example.app.scrollingBanner.AnimatedPieChart
import com.example.app.scrollingBanner.PieChartData
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(navController: NavHostController){
    Scaffold(
        topBar = { AppToolBar(title = "Profile") },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.TopCenter
            ){

                val list = listOf(
                    "Profile",
                    "Badges",
                    "Pie"
                )
                val dotCount = 3
                val pageCount = Int.MAX_VALUE
                val pagerState = rememberPagerState(pageCount = {pageCount})

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.TopCenter
                ){
                    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {

                        HorizontalPager(state = pagerState) { page ->
                            list.getOrNull(
                                page % (list.size)
                            )?.let{content ->
                                when (content) {
                                    "Profile" -> {
                                        ProfileBanner("Username", 7, 5, 9)
                                    }
                                    "Badges" -> {
                                        BadgeBanner(12, 9, 8)
                                    }
                                    "Pie" -> {
                                        PieBanner(10, 5, 3)
                                    }
                                }
                            }

                        }

                        Row(
                            Modifier
                                .height(25.dp)
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.Center
                        ){
                            repeat(dotCount) { iteration ->
                                val color = if (pagerState.currentPage % dotCount == iteration) Color(0xFF6650a4) else Color.White
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(10.dp)

                                )
                            }
                        }

                    }

                    //auto-scroll
                    LaunchedEffect(key1 = pagerState.currentPage, block = {

                        launch {
                            while(true){

                                delay(6000)

                                withContext(NonCancellable){

                                    if(pagerState.currentPage + 1 in 0..Int.MAX_VALUE){
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                    else{
                                        pagerState.scrollToPage(0)
                                    }
                                }
                            }
                        }

                    })
                }
            }


        }
    }
}

@Composable
private fun BadgeBanner(bronze: Int, silver: Int, gold: Int){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD0BCFF)),
        contentAlignment = Alignment.Center,
    ){
        Column (modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Badges earned", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))

            Spacer(modifier = Modifier.height(5.dp))

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6650a4).copy(alpha = 0.75f))
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

                    Text(text = bronze.toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6650a4).copy(alpha = 0.75f))
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

                    Text(text = silver.toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6650a4).copy(alpha = 0.75f))
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

                    Text(text = gold.toString(), fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))
                }
            }
        }

    }
}

@Composable
private fun ProfileBanner(username : String, days : Int, skills : Int, badges : Int){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD0BCFF)),
        contentAlignment = Alignment.Center,
    ){
        Column (modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileImage("")

                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly)
                {
                    Text(text = username, fontWeight = FontWeight.W600, style = TextStyle(fontSize = 35.sp))

                    Spacer(modifier = Modifier.height(10.dp))

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ){
                        Text("Days using the app: ${days.toString()}", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 15.sp))

                        Spacer(modifier = Modifier.height(5.dp))

                        Text("Total Skills learned: ${skills.toString()}", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 15.sp))

                        Spacer(modifier = Modifier.height(5.dp))

                        Text("Total Badges earned: ${badges.toString()}", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 15.sp))
                    }

                }
            }
        }

    }
}

@Composable
private fun PieBanner(completed : Int, inProgress : Int, toDo : Int){

    val pieData = listOf(
        PieChartData("Completed Skills: ", completed, color = Color(0xFF6650a4)),
        PieChartData("Skills In progress: ", inProgress, color = Color(0xFF6650a4).copy(alpha = 0.75f)),
        PieChartData("Unstarted Skills: ", toDo, color = Color.White)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD0BCFF)),
        contentAlignment = Alignment.Center,
    ){
        Column (modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Current Progress", fontWeight = FontWeight.W600, style = TextStyle(fontSize = 20.sp))

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
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

    }
}