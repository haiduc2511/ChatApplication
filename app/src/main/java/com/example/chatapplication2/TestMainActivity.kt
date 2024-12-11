package com.example.chatapplication2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.activitytest.BookActivity
import com.example.chatapplication2.activitytest.BookTagActivity
import com.example.chatapplication2.activitytest.GroupActivity
import com.example.chatapplication2.activitytest.GroupChatActivity
import com.example.chatapplication2.activitytest.GroupEntryRequestActivity
import com.example.chatapplication2.activitytest.GroupUserActivity
import com.example.chatapplication2.activitytest.GroupUserCommentActivity
import com.example.chatapplication2.activitytest.GroupUserMessageActivity
import com.example.chatapplication2.activitytest.GroupUserReadingProcessActivity
import com.example.chatapplication2.activitytest.TagActivity
import com.example.chatapplication2.authactivity.LoginActivity
import com.example.chatapplication2.authactivity.RegisterActivity

class TestMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)
        var loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        var registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        var bookButton: Button = findViewById(R.id.bookButton)
        bookButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, BookActivity::class.java)
            startActivity(intent)
        }
        var bookTagButton: Button = findViewById(R.id.bookTagButton)
        bookTagButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, BookTagActivity::class.java)
            startActivity(intent)
        }
        var groupChatButton: Button = findViewById(R.id.groupChatButton)
        groupChatButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, GroupChatActivity::class.java)
            startActivity(intent)
        }
        var groupEntryRequestButton: Button = findViewById(R.id.groupEntryRequestButton)
        groupEntryRequestButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, GroupEntryRequestActivity::class.java)
            startActivity(intent)
        }
        var groupButton: Button = findViewById(R.id.groupButton)
        groupButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, GroupActivity::class.java)
            startActivity(intent)
        }
        var groupUserCommentButton: Button = findViewById(R.id.groupUserCommentButton)
        groupUserCommentButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, GroupUserCommentActivity::class.java)
            startActivity(intent)
        }
        var groupUserMessageButton: Button = findViewById(R.id.groupUserMessageButton)
        groupUserMessageButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, GroupUserMessageActivity::class.java)
            startActivity(intent)
        }
        var groupUserReadingProcessButton: Button = findViewById(R.id.groupUserReadingProcessButton)
        groupUserReadingProcessButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, GroupUserReadingProcessActivity::class.java)
            startActivity(intent)
        }
        var groupUserButton: Button = findViewById(R.id.groupUserButton)
        groupUserButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, GroupUserActivity::class.java)
            startActivity(intent)
        }
        var tagButton: Button = findViewById(R.id.tagButton)
        tagButton.setOnClickListener {
            val intent = Intent(this@TestMainActivity, TagActivity::class.java)
            startActivity(intent)
        }

    }

}