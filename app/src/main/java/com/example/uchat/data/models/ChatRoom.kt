package com.example.uchat.data.models

import androidx.room.Entity

data class ChatRoom(
    val id:String,
    val chats:List<Message> = emptyList()
)