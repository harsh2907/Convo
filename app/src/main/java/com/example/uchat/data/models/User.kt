package com.example.uchat.data.models

data class User(
    val id:String,
    val name:String,
    var image:String,
    val addedAt:Long = System.currentTimeMillis(),
    val email:String,
    val bio:String = "Hey there! I am using convo"
)