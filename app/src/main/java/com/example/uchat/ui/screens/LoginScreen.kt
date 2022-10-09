package com.example.uchat.ui.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.uchat.R
import com.example.uchat.data.models.User
import com.example.uchat.domain.viewmodel.UserViewModel
import com.example.uchat.ui.theme.Pink
import com.example.uchat.ui.theme.Roboto
import com.example.uchat.ui.theme.Shapes
import com.example.uchat.ui.utils.Constants
import com.example.uchat.ui.utils.ScreensNav
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


 fun getGoogleLoginAuth(activity: Activity): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(Constants.googleId)
        .requestId()
        .requestProfile()
        .build()
    return GoogleSignIn.getClient(activity, gso)
}

fun handleSignInResult(
    task: Task<GoogleSignInAccount>,
    context: Context,
    navController: NavHostController,
    viewModel: UserViewModel
) {
    if (task.isSuccessful) {
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!, context,navController,viewModel)
        } catch (e: Exception) {
            val error = e.message ?: "Unknown error occurred"
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            Log.e("Google Login Error", error)
        }
    } else {
        val error = task.exception?.message ?: "Unknown error occurred"
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        Log.e("Firebase Login Error", error)
    }
}

fun firebaseAuthWithGoogle(
    idToken: String,
    context: Context,
    navController: NavHostController,
    viewModel:UserViewModel) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)

    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val curUser = viewModel.auth.currentUser!!
            val user = User(
                id = curUser.uid,
                name = curUser.displayName?:"Anonymous",
                image = curUser.photoUrl.toString(),
                addedAt = System.currentTimeMillis(),
                email = curUser.email ?: ""
            )
            viewModel.addUser(user)
            navController.navigate(ScreensNav.MainScreen.route) {
                popUpTo(0)
            }
        } else {
            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }
}


@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel:UserViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var isLoading by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val startForResult =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    if (result.data != null) {
                        val task: Task<GoogleSignInAccount> =
                            GoogleSignIn.getSignedInAccountFromIntent(intent)
                        handleSignInResult(task,context,navController,viewModel)
                    }
                }
            }


        Image(
            painter = painterResource(id = R.drawable.login_logo),
            contentDescription = "login_logo"
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Text(
            text = "Start making friends\nBy signing up!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Roboto,
            modifier = Modifier.padding(12.dp)
        )
        Spacer(modifier = Modifier.padding(12.dp))

        GoogleSignInButton(
            modifier = Modifier.fillMaxWidth(.7f),
            text = "Sign in with google",
            icon = painterResource(id = R.drawable.google_logo),
            shape = RoundedCornerShape(6.dp),
            isLoading = isLoading,
            progressIndicatorColor = Pink
        ) {
            isLoading = !isLoading
            startForResult.launch(getGoogleLoginAuth(context as Activity).signInIntent)
        }

    }
}


@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    text: String,
    loadingText: String = "Signing in...",
    icon: Painter,
    isLoading: Boolean = false,
    shape: Shape = Shapes.medium,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = MaterialTheme.colors.surface,
    progressIndicatorColor: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(
            enabled = !isLoading,
            onClick = onClick
        ),
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = "SignInButton",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(text = if (isLoading) loadingText else text)
            AnimatedVisibility(visible = isLoading) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}

