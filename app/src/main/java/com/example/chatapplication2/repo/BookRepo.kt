package com.example.chatapplication2.repo

import com.example.chatapplication2.model.Book
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class BookRepo {

    private val COLLECTION_NAME = "books"
    private val db = FirebaseHelper.instance!!.db

    // Add a new book to Firebase
    fun addBook(book: Book, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val bookWithId = book.copy(bId = id) // Add generated ID to the Book object
        db.collection(COLLECTION_NAME).document(id).set(bookWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all books from Firebase
    fun getBooks(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get books by a specific field (e.g., book title or author)
    fun getBooksByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a book in Firebase
    fun updateBook(id: String, book: Book, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(book)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a book from Firebase
    fun deleteBook(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
