package com.example.chatapplication2.authactivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.activityreal.MainActivity
import com.example.chatapplication2.R
import com.example.chatapplication2.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth


class StartActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mAuth = FirebaseAuth.getInstance()
        initUI()
    }

    private fun initUI() {
        if (mAuth!!.currentUser != null) {
            val intent = Intent(
                this@StartActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }
        binding.btLogin.setOnClickListener { v ->
            val intent = Intent(
                this@StartActivity,
                LoginActivity::class.java
            )
            startActivity(intent)
        }
        binding.btRegister.setOnClickListener { v ->
            val intent = Intent(
                this@StartActivity,
                RegisterActivity::class.java
            )
            startActivity(intent)
        }
    }
}