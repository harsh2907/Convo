package com.example.uchat.data.models

data class Message(
    val id:String,
    val message:String,
    val createdAt:Long = System.currentTimeMillis(),
    val createdBy:String,
    var likedBy:List<String> = emptyList()
)

