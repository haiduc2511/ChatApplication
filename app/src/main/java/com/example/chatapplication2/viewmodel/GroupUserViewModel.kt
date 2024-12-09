package com.example.chatapplication2.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.GroupUser
import com.example.chatapplication2.model.GroupUserMessage
import com.example.chatapplication2.repo.GroupUserMessageRepo
import com.example.chatapplication2.repo.GroupUserRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
class GroupUserViewModel : ViewModel() {

    private val _groupUsersLiveData = MutableLiveData<List<GroupUser>>()
    val groupUsersLiveData: LiveData<List<GroupUser>> = _groupUsersLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val groupUserRepo = GroupUserRepo()

    // Add a new GroupUser
    fun addGroupUser(groupUser: GroupUser) {
        groupUserRepo.addGroupUser(groupUser, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUsers() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get all group users
    fun getGroupUsers() {
        groupUserRepo.getGroupUsers(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val users = task.result?.toObjects(GroupUser::class.java) ?: emptyList()
                    _groupUsersLiveData.postValue(users)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get group users by a specific field (e.g., groupId)
    fun getGroupUsersByField(field: String, value: String) {
        groupUserRepo.getGroupUsersByField(field, value, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val users = task.result?.toObjects(GroupUser::class.java) ?: emptyList()
                    _groupUsersLiveData.postValue(users)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Update a group user
    fun updateGroupUser(id: String, groupUser: GroupUser) {
        groupUserRepo.updateGroupUser(id, groupUser, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUsers() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Delete a group user
    fun deleteGroupUser(id: String) {
        groupUserRepo.deleteGroupUser(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUsers() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }
}
