package com.example.uchat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uchat.ui.theme.Pink
import com.example.uchat.ui.theme.Roboto
import com.example.uchat.ui.utils.ScreensNav
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Splash Screen", fontFamily = Roboto, fontSize = 42.sp, color = Pink)
        Spacer(modifier = Modifier.padding(12.dp))
        CircularProgressIndicator(color = Pink, modifier = Modifier.size(30.dp))

        LaunchedEffect(key1 = Unit){
            delay(1200L)
            navController.navigate(ScreensNav.LoginScreen.route){
                popUpTo(0)
            }
        }
    }
}