package com.example.chatapplication2.repo

import com.example.chatapplication2.model.Tag
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class TagRepo {

    private val COLLECTION_NAME = "tags"
    private val db = FirebaseHelper.instance!!.db

    // Add a new tag to Firebase
    fun addTag(tag: Tag, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val tagWithId = tag.copy(tId = id) // Add generated ID to the Tag object
        db.collection(COLLECTION_NAME).document(id).set(tagWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all tags from Firebase
    fun getTags(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get tags by a specific field (e.g., tag name)
    fun getTagsByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a tag in Firebase
    fun updateTag(id: String, tag: Tag, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(tag)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a tag from Firebase
    fun deleteTag(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
