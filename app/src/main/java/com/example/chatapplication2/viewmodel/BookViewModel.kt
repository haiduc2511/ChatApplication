package com.example.chatapplication2.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.callback.UploadCallback
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.repo.BookRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot

class BookViewModel : ViewModel() {

    private val bookRepo = BookRepo()

    // LiveData for books list
    private val _booksLiveData = MutableLiveData<List<Book>>()
    val booksLiveData: LiveData<List<Book>> get() = _booksLiveData

    // LiveData for errors
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    // Add a new book
    fun addBook(book: Book) {
        bookRepo.addBook(book, object : OnCompleteListener<Void> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<Void>) {
                if (task.isSuccessful) {
                    // You may want to notify the user about success here
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }

    // Get all books
    fun getBooks() {
        bookRepo.getBooks(object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val books = task.result?.toObjects(Book::class.java) ?: emptyList()
                    for (j in 0..books.size - 1) {
                        Log.d("test", books[j].toString())
                    }
                    _booksLiveData.value = books
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }

    // Get books by a specific field (e.g., title, author)cloudclo
    fun getBooksByField(field: String, value: String) {
        bookRepo.getBooksByField(field, value, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val books = task.result?.toObjects(Book::class.java) ?: emptyList()
                    _booksLiveData.value = books
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }

    fun getBookById(value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        bookRepo.getBookById(value, onCompleteListener)
    }

    // Update an existing book
    fun updateBook(id: String, book: Book) {
        bookRepo.updateBook(id, book, object : OnCompleteListener<Void> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<Void>) {
                if (task.isSuccessful) {
                    // Notify user about the update success
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }

    // Delete a book
    fun deleteBook(id: String) {
        bookRepo.deleteBook(id, object : OnCompleteListener<Void> {
            override fun onComplete(task: com.google.android.gms.tasks.Task<Void>) {
                if (task.isSuccessful) {
                    // Notify user about the deletion success
                } else {
                    _errorLiveData.value = task.exception?.message
                }
            }
        })
    }
    fun uploadBookCoverCloudinary(imageUri: Uri?, uploadCallback: UploadCallback?) {
        bookRepo.uploadBookCoverCloudinary(
            imageUri,
            uploadCallback
        )
    }
    fun uploadPdfToCloudinary(pdfUri: Uri?, uploadCallback: UploadCallback?) {
        bookRepo.uploadPdfToCloudinary(
            pdfUri,
            uploadCallback
        )
    }

}
