package com.example.chatapplication2.activitytest

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.R

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.chatapplication2.databinding.ActivityGroupUserCommentBinding
import com.example.chatapplication2.model.GroupUserComment
import com.example.chatapplication2.viewmodel.GroupUserCommentViewModel

class GroupUserCommentActivity : AppCompatActivity() {

    private val groupUserCommentViewModel: GroupUserCommentViewModel by viewModels()
    private lateinit var binding: ActivityGroupUserCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupUserCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe the list of group user comments
        groupUserCommentViewModel.groupUserCommentsLiveData.observe(this, Observer { comments ->
            // Update the UI with the list of comments (e.g., in a RecyclerView)
            // Example:
            // commentAdapter.submitList(comments)
        })

        // Observe error messages
        groupUserCommentViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Add GroupUserComment button click listener
        binding.btnAddComment.setOnClickListener {
            val newComment = GroupUserComment(
                gucid = "", // ID will be generated by Firestore
                groupUserId = binding.etGroupUserId.text.toString(),
                comment = binding.etComment.text.toString(),
                pageNumber = binding.etPageNumber.text.toString(),
                pagePosition = binding.etPagePosition.text.toString(),
                timeStamp = System.currentTimeMillis().toString()
            )
            groupUserCommentViewModel.addGroupUserComment(newComment)
        }

        // Get GroupUserComments button click listener
        binding.btnGetComments.setOnClickListener {
            groupUserCommentViewModel.getGroupUserComments()
        }

        // Update GroupUserComment button click listener
        binding.btnUpdateComment.setOnClickListener {
            val commentId = binding.etCommentId.text.toString()
            val updatedComment = GroupUserComment(
                gucid = commentId,
                groupUserId = binding.etGroupUserId.text.toString(),
                comment = binding.etComment.text.toString(),
                pageNumber = binding.etPageNumber.text.toString(),
                pagePosition = binding.etPagePosition.text.toString(),
                timeStamp = System.currentTimeMillis().toString()
            )
            groupUserCommentViewModel.updateGroupUserComment(commentId, updatedComment)
        }

        // Delete GroupUserComment button click listener
        binding.btnDeleteComment.setOnClickListener {
            val commentId = binding.etCommentId.text.toString()
            groupUserCommentViewModel.deleteGroupUserComment(commentId)
        }
    }
}