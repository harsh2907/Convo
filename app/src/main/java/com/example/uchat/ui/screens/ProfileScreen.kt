package com.example.uchat.ui.screens

import AlertDialogBox
import RequestContentPermission
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.uchat.R
import com.example.uchat.data.models.User
import com.example.uchat.data.repository.EndPoints
import com.example.uchat.domain.viewmodel.UserViewModel
import com.example.uchat.ui.theme.Pink
import com.example.uchat.ui.theme.QuickSand
import com.example.uchat.ui.theme.Roboto


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel
) {
    val currentUser = userViewModel.currentUser.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showSaveIcon by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            contentPadding = PaddingValues(8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Profile Screen", fontFamily = QuickSand, fontSize = 24.sp)
                AnimatedVisibility(visible = showSaveIcon) {
                    Icon(imageVector = Icons.Default.Save,
                        contentDescription = "save",
                        modifier = Modifier.clickable {
                            showDialog = true
                            showSaveIcon = false
                        },
                    )
                }
            }
        }
    }) {
        AnimatedVisibility(
            visible = currentUser == null,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            LoadingBar(tint = Pink)
        }

        val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black

        AnimatedVisibility(visible = currentUser != null) {
            currentUser?.let { user ->

                var name by remember { mutableStateOf(user.name) }
                var bio by remember { mutableStateOf(user.bio) }
                var image by remember { mutableStateOf(user.image) }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    RequestContentPermission(userImage = user.image) { uri ->
                        image = uri
                        showSaveIcon = true
                    }

                    Spacer(modifier = Modifier.padding(12.dp))

                    TransparentTextField(
                        name = user.name,
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Medium,
                            color = textColor
                        ),
                        bgColor = textColor
                    ) {
                        name = it
                        showSaveIcon = true
                    }

                    Spacer(modifier = Modifier.padding(12.dp))

                    TransparentTextField(
                        name = user.bio,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = QuickSand,
                            fontWeight = FontWeight.Light,
                            color = textColor
                        ),
                        bgColor = textColor
                    ) {
                        bio = it
                        showSaveIcon = true
                    }
                }


                AnimatedVisibility(visible = showDialog, enter = slideInVertically()) {
                    AlertDialogBox(
                        title = "Save Changes?",
                        text = "Do you want to save changes.",
                        confirmText = "Save",
                        dismissText = "Cancel",
                        onConfirm = {
                            val curUser = user.copy(
                                name = name,
                                bio = bio,
                                image = image
                            )
                            userViewModel.addUser(curUser)
                            showDialog = false
                            Toast.makeText(context, "Changes made successfully", Toast.LENGTH_SHORT).show()
                        },
                        onDismiss = {
                            showDialog = false
                        }
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TransparentTextField(
    name: String,
    textStyle: TextStyle = TextStyle(),
    bgColor: Color = Color.White,
    onTextChange: (String) -> Unit
) {
    var input by remember { mutableStateOf(name) }
    val keyBoard = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp), contentAlignment = Alignment.Center
    ) {

        BasicTextField(
            value = input,
            onValueChange = {
                input = it
                onTextChange(it)
            },
            textStyle = textStyle,
            keyboardActions = KeyboardActions(onDone = {
                keyBoard?.hide()
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            cursorBrush = Brush.linearGradient(listOf(bgColor, bgColor))
        )
    }
}