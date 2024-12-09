package com.example.chatapplication2.repo

import com.example.chatapplication2.model.GroupEntryRequest
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GroupEntryRequestRepo {

    private val COLLECTION_NAME = "groupEntryRequests"
    private val db = FirebaseHelper.instance!!.db

    // Add a new GroupEntryRequest to Firebase
    fun addGroupEntryRequest(entryRequest: GroupEntryRequest, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val entryRequestWithId = entryRequest.copy(gerid = id) // Add generated ID to the entryRequest object
        db.collection(COLLECTION_NAME).document(id).set(entryRequestWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all group entry requests from Firebase
    fun getGroupEntryRequests(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get group entry requests by a specific field (e.g., groupId)
    fun getGroupEntryRequestsByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a group entry request
    fun updateGroupEntryRequest(id: String, entryRequest: GroupEntryRequest, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(entryRequest)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a group entry request
    fun deleteGroupEntryRequest(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
