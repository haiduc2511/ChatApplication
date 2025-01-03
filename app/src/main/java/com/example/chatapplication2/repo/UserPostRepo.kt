package com.example.chatapplication2.repo

import com.example.chatapplication2.model.UserPost
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot

class UserPostRepo {

    private val COLLECTION_NAME = "userPosts"
    private val db = FirebaseHelper.instance!!.db

    // Add a new UserPost to Firebase
    fun addUserPost(userPost: UserPost, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val postWithId = userPost.copy(upid = id) // Add generated ID to the UserPost object
        db.collection(COLLECTION_NAME).document(id).set(postWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all UserPosts from Firebase
    fun getUserPosts(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get UserPosts by a specific field (e.g., groupId or userId)
    fun getUserPostsByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a UserPost in Firebase
    fun updateUserPost(id: String, userPost: UserPost, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(userPost)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a UserPost from Firebase
    fun deleteUserPost(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
