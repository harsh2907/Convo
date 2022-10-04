package com.example.uchat.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uchat.ui.utils.ScreensNav

@Composable
fun NavScreen(navController: NavHostController) {
   NavHost(navController = navController, startDestination = ScreensNav.SplashScreen.route ){
       composable(route = ScreensNav.SplashScreen.route){
           SplashScreen(navController = navController)
       }
       composable(route = ScreensNav.LoginScreen.route){
           LoginScreen(navController)
       }
       composable(route = ScreensNav.MainScreen.route){
           MainScreen(navController)
       }
   }
}
