package com.example.chatapplication2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.GroupEntryRequest
import com.example.chatapplication2.repo.GroupEntryRequestRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class GroupEntryRequestViewModel : ViewModel() {

    private val _groupEntryRequestsLiveData = MutableLiveData<List<GroupEntryRequest>>()
    val groupEntryRequestsLiveData: LiveData<List<GroupEntryRequest>> = _groupEntryRequestsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val groupEntryRequestRepo = GroupEntryRequestRepo()

    // Add a new Group Entry Request
    fun addGroupEntryRequest(groupEntryRequest: GroupEntryRequest) {
        groupEntryRequestRepo.addGroupEntryRequest(groupEntryRequest, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    // Handle success
                    getGroupEntryRequests() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get all group entry requests
    fun getGroupEntryRequests() {
        groupEntryRequestRepo.getGroupEntryRequests(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val entryRequests = task.result?.toObjects(GroupEntryRequest::class.java)
                    _groupEntryRequestsLiveData.postValue(entryRequests)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Update a group entry request
    fun updateGroupEntryRequest(id: String, groupEntryRequest: GroupEntryRequest) {
        groupEntryRequestRepo.updateGroupEntryRequest(id, groupEntryRequest, object :
            OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupEntryRequests() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Delete a group entry request
    fun deleteGroupEntryRequest(id: String) {
        groupEntryRequestRepo.deleteGroupEntryRequest(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupEntryRequests() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }
}
