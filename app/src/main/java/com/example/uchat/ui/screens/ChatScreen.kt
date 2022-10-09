package com.example.uchat.ui.screens

import android.content.BroadcastReceiver
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.uchat.data.models.Message
import com.example.uchat.domain.viewmodel.ChatViewModel
import com.example.uchat.ui.theme.Pink
import com.example.uchat.ui.utils.Constants
import com.example.uchat.ui.utils.toUser
import kotlinx.coroutines.launch


@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel
) {
    val sender = chatViewModel.auth.currentUser!!
    val receiver = chatViewModel.receiver.collectAsState().value

    val chatState = chatViewModel.chats.collectAsState().value
    val chats = chatState.chats
    val scope = rememberCoroutineScope()

    val lazyColumnState = rememberLazyListState()

    AnimatedVisibility(visible = receiver == null) {
        LoadingBar(tint = Pink)
    }

    receiver?.let {

        LaunchedEffect(key1 = receiver) {
            chatViewModel.getMessages(senderId = sender.uid, receiverId = receiver.id)
        }

        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally()
        ) {

            Column(Modifier.fillMaxSize()) {
                UserModel(user = receiver, onClick = { })

                if (chats.isNotEmpty()) {
                    DisposableEffect(chats.size) {
                        scope.launch {
                            lazyColumnState.scrollToItem(index = chats.lastIndex)
                        }
                        onDispose { }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(.9f),
                    state = lazyColumnState
                ) {
                    items(chats.size) { index ->
                        val message = chats[index]
                        val uid = sender.uid + receiver.id + message.createdAt
                        MessageTemplate(
                            message = message,
                            isSent = message.id == uid
                        )
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .weight(.1f)
                ) {
                    SendMessageBox(modifier = Modifier.fillMaxWidth()) {
                        val message = Message(
                            id = sender.uid + receiver.id + System.currentTimeMillis(),
                            message = it,
                            createdBy = sender.uid,
                        )
                        chatViewModel.addMessage(
                            message = message,
                            senderId = sender.uid,
                            receiverId = receiver.id
                        )

                        if (chats.isNotEmpty()) {
                            scope.launch {
                                lazyColumnState.animateScrollToItem(chats.lastIndex)
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun SendMessageBox(
    modifier: Modifier = Modifier,
    theme: Color = Pink,
    onSend: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    Box(modifier = modifier) {
        OutlinedTextField(value = message, onValueChange = {
            message = it
        }, placeholder = {
            Text(text = "Write something...")
        },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Send, contentDescription = null,
                    tint =
                    if (message.isNotBlank()) theme else Color.Gray,
                    modifier = Modifier.clickable {
                        if (message.isNotBlank()) {
                            onSend(message)
                            message = ""

                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp, top = 3.dp),
            maxLines = 3,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = theme,
                cursorColor = theme,
                unfocusedBorderColor = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        )
    }
}


@Composable
fun MessageTemplate(
    message: Message,
    isSent: Boolean,
    senderColor: Color = Pink,
    receiverColor: Color = Color.LightGray
) {
    var messageClicked by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                messageClicked = !messageClicked
            }
    ) {
        val endPad = if (isSent) 8.dp else 25.dp
        val startPad = if (!isSent) 8.dp else 25.dp
        Text(
            text = message.message,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            modifier = Modifier
                .wrapContentSize()
                .padding(end = endPad, top = 8.dp, bottom = 12.dp, start = startPad)
                .background(
                    color = if (isSent) senderColor else receiverColor,
                    shape = RoundedCornerShape(
                        topStart = if (!isSent) 0f else 20f,
                        topEnd = if (isSent) 0f else 20f,
                        bottomStart = 20f,
                        bottomEnd = 20f
                    )
                )
                .padding(10.dp)
                .align(
                    if (isSent) Alignment.End else Alignment.Start
                ),
            color = Color.Black

        )
        AnimatedVisibility(
            visible = messageClicked,
            modifier = Modifier
                .align(
                    if (isSent) Alignment.End else Alignment.Start
                )
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = Constants.parseTime(message.createdAt),
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
    }
}