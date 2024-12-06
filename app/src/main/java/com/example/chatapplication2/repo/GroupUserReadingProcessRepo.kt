package com.example.chatapplication2.repo

import com.example.chatapplication2.model.GroupUserReadingProcess
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GroupUserReadingProcessRepo {

    private val COLLECTION_NAME = "groupUserReadingProcesses"
    private val db = FirebaseHelper.instance!!.db

    // Add a new GroupUserReadingProcess to Firebase
    fun addGroupUserReadingProcess(process: GroupUserReadingProcess, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val processWithId = process.copy(gURDId = id) // Add generated ID to the object
        db.collection(COLLECTION_NAME).document(id).set(processWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all group user reading processes from Firebase
    fun getGroupUserReadingProcesses(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get processes by a specific field (e.g., reading progress)
    fun getGroupUserReadingProcessesByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Update a group user reading process
    fun updateGroupUserReadingProcess(id: String, process: GroupUserReadingProcess, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(process)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a group user reading process
    fun deleteGroupUserReadingProcess(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }
}
