package com.example.uchat.data.repository

import android.util.Log
import com.example.uchat.data.models.Message
import com.example.uchat.domain.repository.ChatRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class ChatRepositoryImpl(
    private val dbRef:CollectionReference
):ChatRepository {

    override fun getAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun getMessages(senderId: String,receiverId: String): CollectionReference {
        val senderRoom = senderId+receiverId
        return dbRef.document(senderRoom).collection("messages")
    }

    override suspend fun addMessage(message: Message,senderId:String,receiverId:String) {
        try {
            val senderRoom = senderId+receiverId
            val receiverRoom = receiverId+senderId

            dbRef.document(senderRoom).collection("messages")
                .add(message).addOnSuccessListener {
                    dbRef.document(receiverRoom).collection("messages").add(message)
                    Log.d(TAG,"Message is added successfully")
                }.addOnFailureListener{e->
                    Log.e(TAG,e.message.toString())
                }
        }catch (e:Exception){
            Log.e(TAG,"Failed due to exception ${e.message}")
        }
    }

}