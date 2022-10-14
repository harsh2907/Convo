package com.example.uchat.domain.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.snap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uchat.data.models.User
import com.example.uchat.domain.repository.UserRepository
import com.example.uchat.ui.utils.UserListState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

const val TAG = "UserViewModel"

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo:UserRepository
):ViewModel() {


    val auth = userRepo.getAuth()

    private val _users = MutableStateFlow(UserListState())
    val users = _users.asStateFlow()

    private val _currentUser:MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser = _currentUser.asStateFlow()

    private fun setUserList(list: List<User>){
        _users.value = users.value.copy(users = list.sortedByDescending { it.addedAt }, isLoading = false, error = "")
    }


    fun getUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            val uid = auth.currentUser!!.uid

            userRepo.getUsers().addSnapshotListener { value, error ->
                if (error != null) {
                    _users.value = users.value.copy(
                        isLoading = false,
                        error = error.message ?: "An unknown error occurred"
                    )
                    Log.e(TAG, "Failed due to ${error.message}")
                }
                val userList = ArrayList<User>()
                value!!.documentChanges.forEach { dc ->
                    val map = dc.document.data
                    val user = User(
                        id = map["id"] as String,
                        name = map["name"] as String,
                        addedAt = map["addedAt"] as Long,
                        email = map["email"] as String,
                        image = map["image"] as String
                    )
                    if (user.id != uid)
                        userList.add(user)
                    else
                        _currentUser.value  = user
                }
                setUserList(userList)
            }
        }
    }

    fun addUser(user:User){
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.addUser(user)
        }
    }




}

/*
TODO 1 : Image is changed but not properly upload in firebase
TODO 2 : Changes are temporary in profile section as on login again details are reset
TODO 3 : User list glitches sometimes reason unknown
TODO 4 : Change splash screen in future
*/