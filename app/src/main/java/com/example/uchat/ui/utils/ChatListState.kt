package com.example.uchat.ui.utils

import com.example.uchat.data.models.Message

data class ChatListState(
    val chats:List<Message> = emptyList(),
    val isLoading:Boolean = false,
    val error:String = ""
)