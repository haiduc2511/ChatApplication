package com.example.chatapplication2.repo

import com.example.chatapplication2.model.BookTag
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class BookTagRepo {

    private val COLLECTION_NAME = "bookTags"
    private val db = FirebaseHelper.instance!!.db

    // Add a new BookTag to Firebase
    fun addBookTag(bookTag: BookTag, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val bookTagWithId = bookTag.copy(btid = id) // Add generated ID to the BookTag object
        db.collection(COLLECTION_NAME).document(id).set(bookTagWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all book tags from Firebase
    fun getBookTags(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get book tags by a specific field (e.g., bookId or tagId)
    fun getBookTagsByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a book tag
    fun updateBookTag(id: String, bookTag: BookTag, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(bookTag)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a book tag
    fun deleteBookTag(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
