package com.example.uchat.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uchat.ui.utils.Previews
import com.example.uchat.R
import com.example.uchat.ui.theme.Pink
import com.example.uchat.ui.theme.Roboto
import com.example.uchat.ui.theme.Shapes
import com.example.uchat.ui.theme.UChatTheme


@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var isLoading by remember { mutableStateOf(false) }
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
            if (isLoading) {
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



@Previews
@Composable
fun PreviewLogin(){
    UChatTheme{
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            LoginScreen()

        }
    }
}