package com.example.uchat.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.uchat.domain.viewmodel.ChatViewModel
import com.example.uchat.domain.viewmodel.UserViewModel
import com.example.uchat.ui.screens.bottom_nav.BottomNavBar
import com.example.uchat.ui.screens.bottom_nav.BottomNavGraph


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    chatViewModel:ChatViewModel,
    userViewModel: UserViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val bottomNavController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomNavBar(navController = bottomNavController)
        }
    ) {
        BottomNavGraph(
            bottomNavController = bottomNavController,
            mainNavController = navController,
            chatViewModel = chatViewModel,
            userViewModel = userViewModel
        )
    }
}