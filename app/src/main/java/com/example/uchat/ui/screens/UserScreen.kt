package com.example.uchat.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.uchat.data.models.User
import com.example.uchat.domain.viewmodel.ChatViewModel
import com.example.uchat.domain.viewmodel.UserViewModel
import com.example.uchat.ui.screens.bottom_nav.BottomNavBar
import com.example.uchat.ui.screens.bottom_nav.BottomNavGraph
import com.example.uchat.ui.theme.Pink
import com.example.uchat.ui.theme.Purple200
import com.example.uchat.ui.theme.QuickSand
import com.example.uchat.ui.theme.Roboto
import com.example.uchat.ui.utils.ScreensNav
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LogoutDialog(onDismiss:()->Unit,onLogout:()->Unit ) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
                Text(text = "Logout?", fontSize = 16.sp, fontFamily = Roboto, modifier = Modifier.padding(6.dp))
        },
        text = {
            Text(text = "Do you want to logout?", fontSize = 16.sp, fontFamily = Roboto, modifier = Modifier.padding(6.dp))
        },
        confirmButton = {
                Text(text = "Yes", fontSize = 20.sp, color = Pink, fontFamily = Roboto, modifier = Modifier
                    .padding(10.dp)
                    .clickable { onLogout() })
        },
        dismissButton = {
            Text(text = "Cancel", fontSize = 20.sp, color = Pink, fontFamily = Roboto, modifier = Modifier
                .padding(
                    10
                        .dp
                )
                .clickable { onDismiss() })
        }
    )
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel
) {

    LaunchedEffect(key1 = Unit) {
        userViewModel.getUsers()
    }


    var showDialog by remember{ mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val userState = userViewModel.users.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            UserScreenTopBar {
                showDialog = !showDialog
            }
        }
    ) {

        AnimatedVisibility(visible = showDialog, enter = slideInVertically()) {
            LogoutDialog(onDismiss = {showDialog = !showDialog  }) {
                FirebaseAuth.getInstance().signOut()
                getGoogleLoginAuth(context as Activity).signOut().addOnSuccessListener {
                    navController.navigate(ScreensNav.LoginScreen.route) {
                        popUpTo(0)
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Logout failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        //Showing snackbar on error
        if (userState.error.isNotEmpty()) {
            LaunchedEffect(key1 = userState.error) {
                val action = scaffoldState.snackbarHostState.showSnackbar(
                    userState.error,
                    "Retry",
                    SnackbarDuration.Long
                )
                if (action == SnackbarResult.ActionPerformed) {
                    userViewModel.getUsers()
                }
            }
        }
        AnimatedVisibility(visible = userState.isLoading) {
            LoadingBar(tint = Pink)
        }

        AnimatedVisibility(visible = !userState.isLoading && userState.error.isEmpty(), enter = slideInVertically(), exit = slideOutVertically()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                val users = userState.users
                items(users.size) { index ->
                    UserModel(user = users[index], onClick = {
                        chatViewModel.setReceiver(users[index])
                        navController.navigate(ScreensNav.ChatScreen.route)
                    })
                }
            }
        }
    }
}

@Composable
fun LoadingBar(
    tint: Color = MaterialTheme.colors.primary,
    stroke: Dp = 3.dp
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(strokeWidth = stroke, color = tint)
    }
}

@Composable
fun UserModel(
    user: User,
    onClick:()->Unit,
    dividerColor:Color = Color.DarkGray,
    dividerWidth:Dp = 1.dp
) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .clickable {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = rememberImagePainter(data = user.image),
                contentDescription = "user profile image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Pink, Purple200)
                        ),
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.padding(horizontal = 12.dp))
            Text(
                text = user.name,
                fontSize = 16.sp,
                fontFamily = Roboto,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )


        }
        Divider(
            color = dividerColor,
            thickness = dividerWidth,
            modifier = Modifier.padding( horizontal = 12.dp, vertical = 9.dp)
        )
    }
}

@Composable
fun UserScreenTopBar(onLogout: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Main Screen",
            fontFamily = QuickSand,
            fontSize = 24.sp,
            color = Pink
        )

        Icon(imageVector = Icons.Default.Logout,
            contentDescription = "log out icon",
            modifier = Modifier.clickable {
                onLogout()
            })
    }
    Spacer(
        modifier = Modifier
            .padding(1.dp)
            .background(MaterialTheme.colors.onBackground)
    )
}

