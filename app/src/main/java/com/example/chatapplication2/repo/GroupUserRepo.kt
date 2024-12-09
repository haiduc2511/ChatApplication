package com.example.chatapplication2.repo

import com.example.chatapplication2.model.GroupUser
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GroupUserRepo {

    private val COLLECTION_NAME = "groupUsers"
    private val db = FirebaseHelper.instance!!.db

    // Add a new GroupUser to Firebase
    fun addGroupUser(groupUser: GroupUser, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val groupUserWithId = groupUser.copy(guid = id) // Add generated ID to the groupUser object
        db.collection(COLLECTION_NAME).document(id).set(groupUserWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all group users from Firebase
    fun getGroupUsers(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get group users by a specific field (e.g., groupId)
    fun getGroupUsersByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a group user
    fun updateGroupUser(id: String, groupUser: GroupUser, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(groupUser)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a group user
    fun deleteGroupUser(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
