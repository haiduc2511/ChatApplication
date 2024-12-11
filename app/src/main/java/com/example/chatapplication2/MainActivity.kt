package com.example.chatapplication2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapplication2.authactivity.LoginActivity
import com.example.chatapplication2.authactivity.RegisterActivity
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
import com.example.chatapplication2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}