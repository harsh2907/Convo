package com.example.uchat.ui.utils

import com.example.uchat.data.models.User
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*

object Constants {
    const val googleId = "105954805066-idacpds0r84p32fqe3qkog1douuihi7h.apps.googleusercontent.com"

    fun parseTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("hh:mm aa")
        return format.format(date)
    }
}

fun FirebaseUser.toUser():User{
    return User(
        id = uid,
        name = displayName!!,
        image = photoUrl!!.toString(),
        email = email!!
    )
}