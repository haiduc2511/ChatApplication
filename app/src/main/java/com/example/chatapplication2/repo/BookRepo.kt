package com.example.chatapplication2.repo

import android.content.ContentResolver
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import java.util.Objects

class BookRepo {

    private val COLLECTION_NAME = "books"
    private val db = FirebaseHelper.instance!!.db
    private val myUserId: String = FirebaseHelper.instance!!.auth.currentUser!!.uid

    // Add a new book to Firebase
    fun addBook(book: Book, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val bookWithId = book.copy(bid = id) // Add generated ID to the Book object
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

    fun uploadBookCoverCloudinary(
        imageUri: Uri?,
        uploadCallback: UploadCallback?,
    ) {
        val imageId = System.currentTimeMillis().toString() + myUserId
        val options: MutableMap<String, Any> = HashMap()
        options["format"] = "jpg"
        options["folder"] = "meme_storage/images"
        options["public_id"] = imageId
        MediaManager.get().upload(imageUri)
            .unsigned("your_unsigned_preset")
            .options(options)
            .callback(uploadCallback).dispatch()
    }

}
