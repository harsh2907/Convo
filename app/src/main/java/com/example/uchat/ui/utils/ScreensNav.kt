package com.example.uchat.ui.utils

sealed class ScreensNav(val route:String) {
    object SplashScreen:ScreensNav("splash_screen")
    object LoginScreen:ScreensNav("login_screen")
    object MainScreen:ScreensNav("main_screen")
    object ChatScreen:ScreensNav("chat_screen")
    object UserScreen:ScreensNav("user_screen")
}