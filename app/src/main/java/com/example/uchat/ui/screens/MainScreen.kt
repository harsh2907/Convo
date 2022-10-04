package com.example.uchat.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uchat.ui.theme.Pink
import com.example.uchat.ui.utils.ScreensNav
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen(navController: NavHostController) {
    val user = FirebaseAuth.getInstance().currentUser

    Column(Modifier.fillMaxSize()) {

        val context = LocalContext.current
        Text(text = "Main Screen", fontSize = 32.sp, color = Pink)

        Spacer(modifier = Modifier.padding(24.dp))

        Button(
            onClick = {
                //Sign out functionality
                FirebaseAuth.getInstance().signOut()
                getGoogleLoginAuth(context as Activity).signOut().addOnSuccessListener {
                    navController.navigate(ScreensNav.LoginScreen.route) {
                        popUpTo(0)
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Logout failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            }, modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = "Log out", fontSize = 24.sp)
        }
    }
}