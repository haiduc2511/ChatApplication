package com.example.chatapplication2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.chatapplication2.model.User
import com.example.chatapplication2.repo.AuthRepository

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    fun registerUser(email: String, password: String, name: String, onComplete: (Boolean, String?) -> Unit) {
        authRepository.registerUser(email, password, name, onComplete)
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        authRepository.loginUser(email, password, onComplete)
    }

    fun logoutUser() {
        authRepository.logoutUser()
    }

    fun getCurrentUser(): User? {
        return authRepository.getCurrentUser()
    }
}
