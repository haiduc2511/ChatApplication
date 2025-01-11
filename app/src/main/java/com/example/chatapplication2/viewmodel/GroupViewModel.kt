package com.example.chatapplication2.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.callback.UploadCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.repo.GroupRepo

class GroupViewModel : ViewModel() {

    private val _groupsLiveData = MutableLiveData<List<Group>>()
    val groupsLiveData: LiveData<List<Group>> = _groupsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val groupRepo = GroupRepo()

    // Add a new Group
    fun addGroup(group: Group) {
        groupRepo.addGroup(group, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroups() // Refresh the group list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get all groups
    fun getGroups() {
        groupRepo.getGroups(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val groups = task.result?.toObjects(Group::class.java) ?: emptyList()
                    for (j in 0..groups.size - 1) {
                        Log.d("test", groups[j].toString())
                    }

                    _groupsLiveData.postValue(groups)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get groups by specific field (e.g., bookId or adminUserId)
    fun getGroupsByField(field: String, value: String) {
        groupRepo.getGroupsByField(field, value, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val groups = task.result?.toObjects(Group::class.java) ?: emptyList()
                    _groupsLiveData.postValue(groups)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }
    fun getGroupById(value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        groupRepo.getGroupById(value, onCompleteListener)
    }
    fun getGroupsByIds(groupIds: List<String>) {
        groupRepo.getGroupsByIds(groupIds, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val groups = task.result?.toObjects(Group::class.java) ?: emptyList()
                    _groupsLiveData.postValue(groups)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Update a group
    fun updateGroup(id: String, group: Group) {
        groupRepo.updateGroup(id, group, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroups() // Refresh the group list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Delete a group
    fun deleteGroup(id: String) {
        groupRepo.deleteGroup(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroups() // Refresh the group list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }
    fun uploadGroupPhotoCloudinary(imageUri: Uri?, uploadCallback: UploadCallback?) {
        groupRepo.uploadGroupPhotoCloudinary(
            imageUri,
            uploadCallback
        )
    }
}
