package com.example.chatapplication2.viewmodel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.GroupUserMessage
import com.example.chatapplication2.repo.GroupUserMessageRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
class GroupUserMessageViewModel : ViewModel() {

    private val _groupUserMessagesLiveData = MutableLiveData<List<GroupUserMessage>>()
    val groupUserMessagesLiveData: LiveData<List<GroupUserMessage>> = _groupUserMessagesLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val groupUserMessageRepo = GroupUserMessageRepo()

    // Add a new GroupUserMessage
    fun addGroupUserMessage(message: GroupUserMessage, onCompleteListener: OnCompleteListener<Void>) {
        groupUserMessageRepo.addGroupUserMessage(message, onCompleteListener)
    }

    // Get all group user messages
    fun getGroupUserMessages() {
        groupUserMessageRepo.getGroupUserMessages(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val messages = task.result?.toObjects(GroupUserMessage::class.java) ?: emptyList()
                    _groupUserMessagesLiveData.postValue(messages)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get messages by specific field (e.g., groupUserId or groupChatId)
    fun getGroupUserMessagesByField(field: String, value: String) {
        groupUserMessageRepo.getGroupUserMessagesByField(field, value, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val messages = task.result?.toObjects(GroupUserMessage::class.java) ?: emptyList()
                    _groupUserMessagesLiveData.postValue(messages)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Update a group user message
    fun updateGroupUserMessage(id: String, message: GroupUserMessage) {
        groupUserMessageRepo.updateGroupUserMessage(id, message, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUserMessages() // Refresh the messages list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Delete a group user message
    fun deleteGroupUserMessage(id: String) {
        groupUserMessageRepo.deleteGroupUserMessage(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUserMessages() // Refresh the messages list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    fun fetchGroupUserMessages(groupChatId: String) {
        groupUserMessageRepo.listenToGroupUserMessages(groupChatId) { messages, error ->
            if (messages != null) {
                _groupUserMessagesLiveData.postValue(messages!!)
            } else {
                Log.e("GroupUserMessageVM", "Error fetching messages: $error")
            }
        }
    }
}
