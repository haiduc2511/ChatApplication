package com.example.chatapplication2.authactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.activityreal.MainActivity
import com.example.chatapplication2.R
import com.example.chatapplication2.databinding.ActivityLoginBinding
import com.example.chatapplication2.model.User
import com.example.chatapplication2.utils.FirebaseHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore
    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        mAuth = FirebaseHelper.instance!!.auth
        db = FirebaseHelper.instance!!.db
        setContentView(binding.getRoot())
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(R.id.login)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Get the web client ID from google-services.json
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.fabLoginBack.setOnClickListener { onBackPressed() }
        binding.btLogin.setOnClickListener {
            val password: String = binding.etPassword.getText().toString().trim()
            val email: String = binding.etEmail.getText().toString().trim()
            Toast.makeText(this@LoginActivity, "click bt login", Toast.LENGTH_SHORT).show()
            if (password.length < 8) {
                Toast.makeText(this, "Mật khẩu chưa đủ 8 kí tự", Toast.LENGTH_SHORT).show()
            } else {
                signInWithEmailAndPassword(email, password)
            }
        }
//        binding.tvLoginForgotPassword.setOnClickListener { v ->
//            val intent =
//                Intent(this, PasswordResetActivity::class.java)
//            startActivity(intent)
//        }
        binding.ibRegisterGoogle.setOnClickListener { v -> signInWithGoogleAccount() }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@LoginActivity, "Login successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(
                    this@LoginActivity,
                    MainActivity::class.java
                )
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Wrong password or email", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun signInWithGoogleAccount() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign-In was successful, authenticate with Firebase
                val account = task.getResult(
                    ApiException::class.java
                )
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign-In failed
                Log.w("SignIn", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("SignIn", "firebaseAuthWithGoogle:" + acct.id)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignIn", "signInWithCredential:success")
                    val user = mAuth!!.currentUser
                    checkIfUserExistsAndCreate(user)
                } else {
                    // If sign-in fails, display a message to the user
                    Log.w(
                        "SignIn",
                        "signInWithCredential:failure",
                        task.exception
                    )
                }
            }
    }

    private fun checkIfUserExistsAndCreate(firebaseUser: FirebaseUser?) {
        val uid = firebaseUser!!.uid
        db!!.collection("users").document(uid).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot> ->
                if (task.isSuccessful) {
                    if (!task.result.exists()) {
                        // User does not exist, create the user
                        createUser(firebaseUser)
                    } else {

                        // User already exists, you can load their data or redirect as necessary
                        Log.d("User Registration", "User already exists.")
                    }
                    Toast.makeText(this@LoginActivity, "Login successfully", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    )
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("User Registration", "Error getting user", task.exception)
                }
            }
    }

    fun createUser(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {
            val uid = firebaseUser.uid
            val email = firebaseUser.email ?: ""

            val user = User(
                uid = uid,
                name = email,  // Assuming name is set to email; modify if needed
                email = email,
                avaLink = ""   // Set avatar link as needed
            )

            db.collection("users").document(uid).set(user)
                .addOnSuccessListener {
                    // User data successfully written!
                    Log.d("Create user after register", "Successfully created user!")
                }
                .addOnFailureListener { e: Exception? ->
                    // Failed to write user data
                    Log.w("Create user after register", "Error creating user", e)
                }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}