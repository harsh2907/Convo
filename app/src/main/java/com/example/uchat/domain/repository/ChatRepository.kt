package com.example.uchat.domain.repository

import com.example.uchat.data.models.Message
import com.example.uchat.data.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface ChatRepository {

    fun getAuth(): FirebaseAuth

    suspend fun getMessages(senderId: String,receiverId: String): CollectionReference

    suspend fun addMessage(message: Message,senderId:String,receiverId:String)

}