package com.example.chatapplication2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.BookTag
import com.example.chatapplication2.repo.BookTagRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot

class BookTagViewModel : ViewModel() {

    private val bookTagRepo = BookTagRepo()

    // LiveData for book tags list
    private val _bookTagsLiveData = MutableLiveData<List<BookTag>>()
    val bookTagsLiveData: LiveData<List<BookTag>> get() = _bookTagsLiveData

    // LiveData for errors
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    // Add a new BookTag
    fun addBookTag(bookTag: BookTag) {
        bookTagRepo.addBookTag(bookTag, object : OnCompleteListener<Void> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<Void>) {
                if (task.isSuccessful) {
                    // You may want to notify the user about success here
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }

    // Get all BookTags
    fun getBookTags() {
        bookTagRepo.getBookTags(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val bookTags = task.result?.toObjects(BookTag::class.java) ?: emptyList()
                    _bookTagsLiveData.value = bookTags
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }

    // Get BookTags by a specific field (e.g., bookId or tagId)
    fun getBookTagsByField(field: String, value: String) {
        bookTagRepo.getBookTagsByField(field, value, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val bookTags = task.result?.toObjects(BookTag::class.java) ?: emptyList()
                    _bookTagsLiveData.value = bookTags
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }

    // Update a BookTag
    fun updateBookTag(id: String, bookTag: BookTag) {
        bookTagRepo.updateBookTag(id, bookTag, object : OnCompleteListener<Void> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<Void>) {
                if (task.isSuccessful) {
                    // Notify user about update success
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }

    // Delete a BookTag
    fun deleteBookTag(id: String) {
        bookTagRepo.deleteBookTag(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<Void>) {
                if (task.isSuccessful) {
                    // Notify user about deletion success
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }
}
