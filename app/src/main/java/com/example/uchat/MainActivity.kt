package com.example.uchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.uchat.domain.viewmodel.UserViewModel
import com.example.uchat.ui.screens.NavScreen
import com.example.uchat.ui.theme.UChatTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userViewModel:UserViewModel  = viewModel()
            UChatTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val systemUiController = rememberSystemUiController()
                    val useDarkIcons = !isSystemInDarkTheme()
                    val color = MaterialTheme.colors.background
                    val navController = rememberNavController()

                    DisposableEffect(systemUiController, useDarkIcons) {
                        systemUiController.setSystemBarsColor(
                            color = color,
                            darkIcons = useDarkIcons
                        )

                        onDispose {}
                    }

                    NavScreen(navController = navController,userViewModel)
                }
            }
        }
    }

}



