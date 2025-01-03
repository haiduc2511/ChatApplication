package com.example.chatapplication2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.UserPost
import com.example.chatapplication2.repo.UserPostRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class UserPostViewModel : ViewModel() {

    private val _userPostsLiveData = MutableLiveData<List<UserPost>>()
    val userPostsLiveData: LiveData<List<UserPost>> = _userPostsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val userPostRepo = UserPostRepo()

    // Add a new UserPost
    fun addUserPost(userPost: UserPost) {
        userPostRepo.addUserPost(userPost, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getUserPosts() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get all UserPosts
    fun getUserPosts() {
        userPostRepo.getUserPosts(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val posts = task.result?.toObjects(UserPost::class.java) ?: emptyList()
                    _userPostsLiveData.postValue(posts)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Update a UserPost
    fun updateUserPost(id: String, userPost: UserPost) {
        userPostRepo.updateUserPost(id, userPost, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getUserPosts() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Delete a UserPost
    fun deleteUserPost(id: String) {
        userPostRepo.deleteUserPost(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getUserPosts() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }
}