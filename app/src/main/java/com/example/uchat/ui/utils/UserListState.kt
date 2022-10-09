package com.example.uchat.ui.utils

import com.example.uchat.data.models.User

data class UserListState(
    val users:List<User> = emptyList(),
    val isLoading:Boolean = true,
    val error:String = ""
)