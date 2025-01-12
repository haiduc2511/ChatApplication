package com.example.chatapplication2.repo

import com.example.chatapplication2.model.GroupUserComment
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GroupUserCommentRepo {

    private val COLLECTION_NAME = "groupUserComments"
    private val db = FirebaseHelper.instance!!.db

    // Add a new GroupUserComment to Firebase
    fun addGroupUserComment(comment: GroupUserComment, onCompleteListener: OnCompleteListener<Void>) {
        val id = System.currentTimeMillis().toString() + db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val commentWithId = comment.copy(gucid = id) // Add generated ID to the comment object
        db.collection(COLLECTION_NAME).document(id).set(commentWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all group user comments from Firebase
    fun getGroupUserComments(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get comments by a specific field (e.g., groupUserId)
    fun getGroupUserCommentsByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a group user comment
    fun updateGroupUserComment(id: String, comment: GroupUserComment, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(comment)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a group user comment
    fun deleteGroupUserComment(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
