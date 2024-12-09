package com.example.chatapplication2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.GroupChat
import com.example.chatapplication2.repo.GroupChatRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot

class GroupChatViewModel : ViewModel() {

    private val groupChatRepo = GroupChatRepo()

    // LiveData for group chats list
    private val _groupChatsLiveData = MutableLiveData<List<GroupChat>>()
    val groupChatsLiveData: LiveData<List<GroupChat>> get() = _groupChatsLiveData

    // LiveData for errors
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    // Add a new group chat
    fun addGroupChat(groupChat: GroupChat) {
        groupChatRepo.addGroupChat(groupChat, OnCompleteListener { task ->
            if (!task.isSuccessful) {
                _errorLiveData.value = task.exception?.message
            }
        })
    }

    // Get all group chats
    fun getGroupChats() {
        groupChatRepo.getGroupChats(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val groupChats = task.result?.toObjects(GroupChat::class.java) ?: emptyList()
                _groupChatsLiveData.value = groupChats
            } else {
                _errorLiveData.value = task.exception?.message
            }
        })
    }

    // Update a group chat
    fun updateGroupChat(id: String, groupChat: GroupChat) {
        groupChatRepo.updateGroupChat(id, groupChat, OnCompleteListener { task ->
            if (!task.isSuccessful) {
                _errorLiveData.value = task.exception?.message
            }
        })
    }

    // Delete a group chat
    fun deleteGroupChat(id: String) {
        groupChatRepo.deleteGroupChat(id, OnCompleteListener { task ->
            if (!task.isSuccessful) {
                _errorLiveData.value = task.exception?.message
            }
        })
    }
}
