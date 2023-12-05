package com.example.app.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
                                        ProfileBanner()
                                    }
                                    "Badges" -> {
                                        BadgeBanner()
                                    }
                                    "Pie" -> {
                                        PieBanner()
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
                                val color = if (pagerState.currentPage % dotCount == iteration) Color.DarkGray else Color.LightGray
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
private fun BadgeBanner(){
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
            Text(text = "Badges earned", fontWeight = FontWeight.W600)

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box(
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFCD7F32))
                    )
                    Text(text = "12", fontWeight = FontWeight.W600)
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box(
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFC0C0C0))
                    )
                    Text(text = "10", fontWeight = FontWeight.W600)
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box(
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFFFD700))
                    )
                    Text(text = "9", fontWeight = FontWeight.W600)
                }
            }
        }

    }
}

@Composable
private fun ProfileBanner(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD0BCFF)),
        contentAlignment = Alignment.Center,
    ){
        Text(text = "Profile banner here")
    }

}

@Composable
private fun PieBanner(){

    val pieData = listOf(
        PieChartData("Completed", 10, color = Color.Red),
        PieChartData("In progress", 20, color = Color.Red.copy(alpha = 0.5f)),
        PieChartData("To Do", 15, color = Color.Gray)
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
        AnimatedPieChart(
            modifier = Modifier
                .padding(32.dp),
            pieDataPoints = pieData,
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = it.padding(16.dp)
                ){
                    pieData.map{
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = it.label, fontWeight = FontWeight.W600)
                            Text(text = it.value.toString())
                        }
                    }
                }
            }
        )
    }
}