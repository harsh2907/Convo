package com.example.uchat.ui.screens.bottom_nav

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.uchat.domain.viewmodel.ChatViewModel
import com.example.uchat.domain.viewmodel.UserViewModel
import com.example.uchat.ui.screens.UserScreen
import com.example.uchat.ui.screens.ProfileScreen
import com.example.uchat.ui.theme.Pink

@Composable
fun BottomNavBar(
    navController: NavHostController
) {
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation(
        elevation = 10.dp
    ) {
        screens.forEach { screen ->
            AddItem(
                currentDestination = currentDestination,
                screen = screen,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    currentDestination: NavDestination?,
    screen: BottomNavItem,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "bottom_icon"
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        selectedContentColor = Pink,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            if(currentDestination?.hierarchy?.any { it.route == screen.route } == false) {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        }
    )
}

@Composable
fun BottomNavGraph(
    bottomNavController: NavHostController,
    mainNavController:NavHostController,
    chatViewModel:ChatViewModel,
    userViewModel: UserViewModel
) {
    NavHost(navController = bottomNavController, startDestination = BottomNavItem.Home.route ) {
        composable(route = BottomNavItem.Home.route) {
            UserScreen(navController = mainNavController, chatViewModel =  chatViewModel, userViewModel = userViewModel)
        }
        composable(route = BottomNavItem.Profile.route) {
            ProfileScreen(
                userViewModel
        )
        }
    }
}