package com.example.chatapplication2.authactivity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.R
import com.example.chatapplication2.databinding.ActivityRegisterBinding
import com.example.chatapplication2.model.User
import com.example.chatapplication2.repo.AuthRepository
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authRepository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        authRepository = AuthRepository()
        initUI()
    }

    private fun initUI() {
        binding.fabRegisterBack.setOnClickListener { onBackPressed() }
        binding.btRegister.setOnClickListener {
            val email: String = binding.etEmail.getText().toString().trim()
            val password: String = binding.etPassword.getText().toString().trim()
            val name: String = binding.etUsername.getText().toString().trim()
            if (binding.etPasswordConfirm.text.toString() == password) {
                authRepository.registerUser(email, password, name) { isSuccess, errorMessage ->
                    if (isSuccess) {
                        Log.d("RegisterUser", "Registration successful")
                        // Navigate to another screen or provide feedback to the user
                    } else {
                        Log.w("RegisterUser", "Registration failed: $errorMessage")
                        // Show an error message to the user
                    }
                }
            } else {
                Log.w("RegisterUser", "Passwords do not match")
                // Handle password mismatch (e.g., show a Toast)
            }
        }
        binding.ibRegisterGoogle.setOnClickListener { v -> }
    }


}