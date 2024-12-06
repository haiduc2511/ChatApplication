package com.example.chatapplication2.repo

import com.example.chatapplication2.model.GroupUserMessage
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GroupUserMessageRepo {

    private val COLLECTION_NAME = "groupUserMessages"
    private val db = FirebaseHelper.instance!!.db

    // Add a new group user message to Firebase
    fun addGroupUserMessage(message: GroupUserMessage, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val messageWithId = message.copy(gUmId = id) // Add generated ID to the message object
        db.collection(COLLECTION_NAME).document(id).set(messageWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all group user messages from Firebase
    fun getGroupUserMessages(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get messages by a specific field (e.g., group user ID)
    fun getGroupUserMessagesByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a group user message
    fun updateGroupUserMessage(id: String, message: GroupUserMessage, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(message)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a group user message
    fun deleteGroupUserMessage(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
