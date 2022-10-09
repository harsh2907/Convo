package com.example.uchat.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uchat.data.models.User
import com.example.uchat.domain.repository.UserRepository
import com.example.uchat.ui.utils.UserListState
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "UserViewModel"

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo:UserRepository
):ViewModel() {


    val auth = userRepo.getAuth()
    private val _users = MutableStateFlow(UserListState())
    val users = _users.asStateFlow()

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