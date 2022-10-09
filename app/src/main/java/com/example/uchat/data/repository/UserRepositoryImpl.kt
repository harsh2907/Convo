package com.example.uchat.data.repository

import android.util.Log
import com.example.uchat.data.models.User
import com.example.uchat.domain.repository.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

const val TAG = "User Repository"

class UserRepositoryImpl(
    private val dbRef: CollectionReference
):UserRepository {

    override fun getAuth(): FirebaseAuth  = FirebaseAuth.getInstance()

    override suspend fun getUsers(): CollectionReference {
        return dbRef
    }

    override suspend fun addUser(user: User) {
       try {
           dbRef.document(user.id)
               .set(user).addOnSuccessListener {
                   Log.d(TAG,"${user.name} is added successfully")
               }.addOnFailureListener{e->
                   Log.e(TAG,e.message.toString())
               }
       }catch (e:Exception){
           Log.e(TAG,"Failed due to exception ${e.message}")
       }
    }

    override suspend fun getUserById(id: String): Task<DocumentSnapshot> {
       return dbRef.document(id).get()
    }
}

sealed class EndPoints(val route:String){
    object Users:EndPoints("users")
    object Chat:EndPoints("chats")
}

