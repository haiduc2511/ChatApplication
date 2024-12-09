package com.example.chatapplication2.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.GroupUser
import com.example.chatapplication2.model.GroupUserMessage
import com.example.chatapplication2.model.Tag
import com.example.chatapplication2.repo.GroupUserMessageRepo
import com.example.chatapplication2.repo.GroupUserRepo
import com.example.chatapplication2.repo.TagRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class TagViewModel : ViewModel() {

    private val _tagsLiveData = MutableLiveData<List<Tag>>()
    val tagsLiveData: LiveData<List<Tag>> = _tagsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val tagRepo = TagRepo()

    // Add a new tag
    fun addTag(tag: Tag) {
        tagRepo.addTag(tag, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getTags() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get all tags
    fun getTags() {
        tagRepo.getTags(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val tags = task.result?.toObjects(Tag::class.java) ?: emptyList()
                    _tagsLiveData.postValue(tags)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Get tags by a specific field (e.g., tagName)
    fun getTagsByField(field: String, value: String) {
        tagRepo.getTagsByField(field, value, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val tags = task.result?.toObjects(Tag::class.java) ?: emptyList()
                    _tagsLiveData.postValue(tags)
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Update a tag
    fun updateTag(id: String, tag: Tag) {
        tagRepo.updateTag(id, tag, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getTags() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }

    // Delete a tag
    fun deleteTag(id: String) {
        tagRepo.deleteTag(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    getTags() // Refresh the list
                } else {
                    _errorLiveData.postValue("Error: ${task.exception?.message}")
                }
            }
        })
    }
}
