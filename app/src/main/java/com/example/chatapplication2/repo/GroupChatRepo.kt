package com.example.chatapplication2.repo

import com.example.chatapplication2.model.GroupChat
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GroupChatRepo {

    private val COLLECTION_NAME = "groupChats"
    private val db = FirebaseHelper.instance!!.db

    // Add a new GroupChat to Firebase
    fun addGroupChat(groupChat: GroupChat, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val groupChatWithId = groupChat.copy(gcid = id) // Add generated ID to the groupChat object
        db.collection(COLLECTION_NAME).document(id).set(groupChatWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all group chats from Firebase
    fun getGroupChats(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get group chats by a specific field (e.g., theme)
    fun getGroupChatsByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a group chat
    fun updateGroupChat(id: String, groupChat: GroupChat, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(groupChat)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a group chat
    fun deleteGroupChat(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
