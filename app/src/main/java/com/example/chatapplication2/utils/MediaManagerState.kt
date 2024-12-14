package com.example.chatapplication2.utils


object MediaManagerState {
    var isInitialized = false
        private set
    fun initialize() {
        isInitialized = true
    }
}

