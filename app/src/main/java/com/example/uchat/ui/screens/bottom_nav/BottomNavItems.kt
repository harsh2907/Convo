package com.example.uchat.ui.screens.bottom_nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {

    object Home : BottomNavItem(
        "Home",
        Icons.Default.Home,
        "home"
    )

    object Profile : BottomNavItem(
        "My Profile",
        Icons.Default.Person,
        "my_profile"
    )

}