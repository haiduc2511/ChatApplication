package com.example.chatapplication2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.GroupUserReadingProcess
import com.example.chatapplication2.repo.GroupUserReadingProcessRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
class GroupUserReadingProcessViewModel : ViewModel() {

    private val _groupUserReadingProcessesLiveData = MutableLiveData<List<GroupUserReadingProcess>>()
    val groupUserReadingProcessesLiveData: LiveData<List<GroupUserReadingProcess>> = _groupUserReadingProcessesLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val groupUserReadingProcessRepo = GroupUserReadingProcessRepo()

    // Add a new GroupUserReadingProcess
    fun addGroupUserReadingProcess(process: GroupUserReadingProcess) {
        groupUserReadingProcessRepo.addGroupUserReadingProcess(process, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUserReadingProcesses() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get all group user reading processes
    fun getGroupUserReadingProcesses() {
        groupUserReadingProcessRepo.getGroupUserReadingProcesses(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val processes = task.result?.toObjects(GroupUserReadingProcess::class.java) ?: emptyList()
                    _groupUserReadingProcessesLiveData.postValue(processes)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get processes by a specific field (e.g., reading progress)
    fun getGroupUserReadingProcessesByField(field: String, value: String) {
        groupUserReadingProcessRepo.getGroupUserReadingProcessesByField(field, value, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val processes = task.result?.toObjects(GroupUserReadingProcess::class.java) ?: emptyList()
                    _groupUserReadingProcessesLiveData.postValue(processes)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Update a group user reading process
    fun updateGroupUserReadingProcess(id: String, process: GroupUserReadingProcess) {
        groupUserReadingProcessRepo.updateGroupUserReadingProcess(id, process, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUserReadingProcesses() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Delete a group user reading process
    fun deleteGroupUserReadingProcess(id: String) {
        groupUserReadingProcessRepo.deleteGroupUserReadingProcess(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUserReadingProcesses() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }
}
