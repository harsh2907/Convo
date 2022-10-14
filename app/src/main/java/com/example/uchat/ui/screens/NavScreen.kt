package com.example.uchat.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uchat.domain.viewmodel.ChatViewModel
import com.example.uchat.domain.viewmodel.UserViewModel
import com.example.uchat.ui.utils.ScreensNav

@Composable
fun NavScreen(navController: NavHostController,userViewModel: UserViewModel) {

    val chatViewModel:ChatViewModel = viewModel()

   NavHost(navController = navController, startDestination = ScreensNav.SplashScreen.route ){
       composable(route = ScreensNav.SplashScreen.route){
           SplashScreen(navController = navController)
       }
       composable(route = ScreensNav.LoginScreen.route){
           LoginScreen(navController)
       }
       composable(route = ScreensNav.MainScreen.route){
           MainScreen(navController = navController, chatViewModel =chatViewModel , userViewModel = userViewModel)
       }
       composable(route = ScreensNav.ChatScreen.route){
           ChatScreen(chatViewModel)
       }
       composable(route = ScreensNav.UserScreen.route){
           UserScreen(navController, chatViewModel = chatViewModel, userViewModel = userViewModel)
       }
   }
}
