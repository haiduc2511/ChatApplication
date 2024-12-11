package com.example.chatapplication2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.example.chatapplication2.adapter.GroupAdapter
import com.example.chatapplication2.databinding.ActivityMainBinding
import com.example.chatapplication2.viewmodel.GroupViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val groupViewModel: GroupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val intent = Intent(this@MainActivity, TestMainActivity::class.java)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        // Observe the groupsLiveData
        groupViewModel.groupsLiveData.observe(this, Observer { groups ->
            val adapter = GroupAdapter(groups)
            binding.recyclerView.adapter = adapter
        })

        // Observe errorLiveData for errors
        groupViewModel.errorLiveData.observe(this, Observer { error ->
            // Handle error (e.g., show a Toast or Log)
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch sample groups
        groupViewModel.getGroups()
    }
}