package com.example.uchat.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uchat.data.models.Message
import com.example.uchat.data.models.User
import com.example.uchat.data.repository.EndPoints
import com.example.uchat.domain.repository.ChatRepository
import com.example.uchat.ui.utils.ChatListState
import com.google.api.Endpoint
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val tag= "ChatViewModel"

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepo:ChatRepository
):ViewModel() {


    val auth = chatRepo.getAuth()

    private val _receiver:MutableStateFlow<User?> = MutableStateFlow(null)
    val receiver = _receiver.asStateFlow()

    private val _chats = MutableStateFlow(ChatListState())
    val chats = _chats.asStateFlow()


    fun setReceiver(user:User){
        _receiver.value = user
    }

    fun getMessages(senderId:String,receiverId:String){
        viewModelScope.launch(Dispatchers.IO) {

            _chats.value = chats.value.copy(
                chats = emptyList(),
                isLoading = false
            )
            val messages = ArrayList<Message>()
            chatRepo.getMessages(senderId,receiverId)
                .addSnapshotListener { snapshots, error ->
                    if(error!=null){
                        Log.e(tag,"Cant Listen to snapshot",error)
                        return@addSnapshotListener
                    }

                    for(dc in snapshots!!.documentChanges){
                        val map = dc.document.data
                        val message = Message(
                            message = map["message"] as String,
                            createdBy = map["createdBy"] as String,
                            createdAt = map["createdAt"] as Long,
                            id = map["id"] as String,
                            likedBy = map["likedBy"] as List<String>
                        )
                        messages.add(message)

                    }
                    Log.w(tag,"Snapshot called & messages size : ${messages.size}")

                    _chats.value = chats.value.copy(
                        chats = messages.sortedBy { it.createdAt },
                        isLoading = false,
                        error=""
                    )



                }
                    /*
                .addOnSuccessListener { doc->
                    val messages = ArrayList<Message>()
                    doc.documents.forEach { snapshot->
                        val map = snapshot.data!!
                        val message = Message(
                            message = map["message"] as String,
                            createdBy = map["createdBy"] as String,
                            createdAt = map["createdAt"] as Long,
                            id = map["id"] as String,
                            likedBy = map["likedBy"] as List<String>
                        )
                        messages.add(message)
                    }
                    _chats.value = _chats.value.copy(
                        chats = messages.sortedBy { it.createdAt },
                        isLoading = false
                    )

                    Log.w("All messages",messages.size.toString())
            }.addOnFailureListener {

                    _chats.value = _chats.value.copy(
                        chats = emptyList(),
                        isLoading = false,
                        error = it.message ?: "An unknown error occurred"
                    )

                Log.e(tag,"Error due to ${it.message}")
            }

                     */
        }
    }

    fun addMessage(message: Message,senderId: String,receiverId: String){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepo.addMessage(message,senderId,receiverId)
        }
    }






}

