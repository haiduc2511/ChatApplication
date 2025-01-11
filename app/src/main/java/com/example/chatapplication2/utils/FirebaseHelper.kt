package com.example.chatapplication2.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseHelper private constructor() {
    // Method to get the FirebaseFireStore instance
    val db: FirebaseFirestore

    // Method to get the FirebaseAuth instance
    val auth: FirebaseAuth

    // Private constructor to prevent direct instantiation
    init {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    fun getUserId(): String? {
        return auth.currentUser?.uid
    }
    companion object {
        // Singleton instance
        @get:Synchronized
        var instance: FirebaseHelper? = null
            // Method to get the singleton instance
            get() {
                if (field == null) {
                    field = FirebaseHelper()
                }
                return field
            }
            private set
    }
}

