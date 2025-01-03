package com.example.chatapplication2.activitytest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.R

import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.chatapplication2.databinding.ActivityUserPostBinding
import com.example.chatapplication2.model.UserPost
import com.example.chatapplication2.viewmodel.UserPostViewModel

class UserPostActivity : AppCompatActivity() {

    private val userPostViewModel: UserPostViewModel by viewModels()
    private lateinit var binding: ActivityUserPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe the list of UserPosts
        userPostViewModel.userPostsLiveData.observe(this, Observer { posts ->
            // Update the UI with the list of UserPosts (e.g., in a RecyclerView)
            // Example:
            // userPostAdapter.submitList(posts)
        })

        // Observe error messages
        userPostViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Add UserPost button click listener
        binding.btnAddPost.setOnClickListener {
            val newUserPost = UserPost(
                upid = "",
                groupId = binding.etGroupId.text.toString(),
                userId = binding.etUserId.text.toString(),
                imagePost = binding.etImagePost.text.toString(),
                captionPost = binding.etCaptionPost.text.toString()
            )
            userPostViewModel.addUserPost(newUserPost)
        }

        // Get UserPosts button click listener
        binding.btnGetPosts.setOnClickListener {
            userPostViewModel.getUserPosts()
        }

        // Update UserPost button click listener
        binding.btnUpdatePost.setOnClickListener {
            val postId = binding.etPostId.text.toString()
            val updatedUserPost = UserPost(
                upid = postId,
                groupId = binding.etGroupId.text.toString(),
                userId = binding.etUserId.text.toString(),
                imagePost = binding.etImagePost.text.toString(),
                captionPost = binding.etCaptionPost.text.toString()
            )
            userPostViewModel.updateUserPost(postId, updatedUserPost)
        }

        // Delete UserPost button click listener
        binding.btnDeletePost.setOnClickListener {
            val postId = binding.etPostId.text.toString()
            userPostViewModel.deleteUserPost(postId)
        }
    }
}
