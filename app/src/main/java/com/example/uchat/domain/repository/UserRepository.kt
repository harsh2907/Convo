package com.example.uchat.domain.repository

import com.example.uchat.data.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface UserRepository {

    fun getAuth():FirebaseAuth

    suspend fun getUsers(): CollectionReference

    suspend fun addUser(user: User)

    suspend fun getUserById(id:String): Task<DocumentSnapshot>
}