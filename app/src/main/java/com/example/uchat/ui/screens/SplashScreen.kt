package com.example.uchat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uchat.ui.theme.Roboto
import com.example.uchat.ui.utils.ScreensNav
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center) {
        Text(text = "Splash Screen", fontFamily = Roboto, fontSize = 32.sp)

        LaunchedEffect(key1 = Unit){
            delay(1200L)
            navController.navigate(ScreensNav.LoginScreen.route){
                popUpTo(0)
            }
        }
    }
}