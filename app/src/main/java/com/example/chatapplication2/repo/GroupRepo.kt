package com.example.chatapplication2.repo

import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupRepo {

    private val COLLECTION_NAME = "groups"
    private val db = FirebaseHelper.instance!!.db
    private val myUserId: String = FirebaseHelper.instance!!.auth.currentUser!!.uid

    // Add a new Group to Firebase
    fun addGroup(group: Group, onCompleteListener: OnCompleteListener<Void>) {
        val id = db.collection(COLLECTION_NAME).document().id // Generate a new ID
        val groupWithId = group.copy(gid = id) // Add generated ID to the Group object
        db.collection(COLLECTION_NAME).document(id).set(groupWithId)
            .addOnCompleteListener(onCompleteListener)
    }

    // Get all groups from Firebase
    fun getGroups(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).get()
            .addOnCompleteListener(onCompleteListener)
    }

    // Get groups by a specific field (e.g., bookId or adminUserId)
    fun getGroupsByField(field: String, value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo(field, value).get()
            .addOnCompleteListener(onCompleteListener)
    }
    fun getGroupById(value: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME).whereEqualTo("gid", value).get()
            .addOnCompleteListener(onCompleteListener)
    }
    fun getGroupsByIds(groupIds: List<String>, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        db.collection(COLLECTION_NAME)
            .whereIn("gid", groupIds)
            .get()
            .addOnCompleteListener(onCompleteListener)
    }
    // Update a group
    fun updateGroup(id: String, group: Group, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).set(group)
            .addOnCompleteListener(onCompleteListener)
    }

    // Delete a group
    fun deleteGroup(id: String, onCompleteListener: OnCompleteListener<Void>) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnCompleteListener(onCompleteListener)
    }

    fun uploadGroupPhotoCloudinary(
        imageUri: Uri?,
        uploadCallback: UploadCallback?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageId = System.currentTimeMillis().toString() + myUserId
                val options: MutableMap<String, Any> = HashMap()
                options["format"] = "jpg"
                options["folder"] = "groupPhoto"
                options["public_id"] = imageId

                withContext(Dispatchers.Main) {
                    MediaManager.get().upload(imageUri)
                        .unsigned("your_unsigned_preset")
                        .options(options)
                        .callback(uploadCallback)
                        .dispatch()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("Error uploading image", e.message ?: "Unknown error")
                }
            }
        }
    }
}
