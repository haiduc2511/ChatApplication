package com.example.chatapplication2.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.model.GroupEntryRequest
import com.example.chatapplication2.model.GroupUserComment
import com.example.chatapplication2.repo.GroupEntryRequestRepo
import com.example.chatapplication2.repo.GroupUserCommentRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class GroupUserCommentViewModel : ViewModel() {

    private val _groupUserCommentsLiveData = MutableLiveData<List<GroupUserComment>>()
    val groupUserCommentsLiveData: LiveData<List<GroupUserComment>> = _groupUserCommentsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val groupUserCommentRepo = GroupUserCommentRepo()

    // Add a new GroupUserComment
    fun addGroupUserComment(comment: GroupUserComment) {
        groupUserCommentRepo.addGroupUserComment(comment, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUserComments() // Refresh the comments list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    fun addGroupUserCommentInGroupUserCommentFragment(comment: GroupUserComment, group: Group) {
        groupUserCommentRepo.addGroupUserComment(comment, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUserCommentsByField("groupId", group.gid)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }


    // Get all group user comments
    fun getGroupUserComments() {
        groupUserCommentRepo.getGroupUserComments(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val comments = task.result?.toObjects(GroupUserComment::class.java)?: emptyList()
                    _groupUserCommentsLiveData.postValue(comments)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get comments by specific field (e.g., groupUserId)
    fun getGroupUserCommentsByField(field: String, value: String) {
        groupUserCommentRepo.getGroupUserCommentsByField(field, value, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val comments = task.result?.toObjects(GroupUserComment::class.java)?: emptyList()
                    _groupUserCommentsLiveData.postValue(comments)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Update a group user comment
    fun updateGroupUserComment(id: String, comment: GroupUserComment) {
        groupUserCommentRepo.updateGroupUserComment(id, comment, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUserComments() // Refresh the comments list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Delete a group user comment
    fun deleteGroupUserComment(id: String) {
        groupUserCommentRepo.deleteGroupUserComment(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getGroupUserComments() // Refresh the comments list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }
}
