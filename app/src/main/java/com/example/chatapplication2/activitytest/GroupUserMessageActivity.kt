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
import com.example.chatapplication2.databinding.ActivityGroupUserMessageBinding
import com.example.chatapplication2.model.GroupUserMessage
import com.example.chatapplication2.viewmodel.GroupUserMessageViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class GroupUserMessageActivity : AppCompatActivity() {

    private val groupUserMessageViewModel: GroupUserMessageViewModel by viewModels()
    private lateinit var binding: ActivityGroupUserMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupUserMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe the list of group user messages
        groupUserMessageViewModel.groupUserMessagesLiveData.observe(this, Observer { messages ->
            // Update the UI with the list of messages (e.g., in a RecyclerView)
            // Example:
            // messageAdapter.submitList(messages)
        })

        // Observe error messages
        groupUserMessageViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Add GroupUserMessage button click listener
        binding.btnAddMessage.setOnClickListener {
            val newMessage = GroupUserMessage(
                gumid = "", // ID will be generated by Firestore
                groupChatId = binding.etGroupChatId.text.toString(),
                groupUserId = binding.etGroupUserId.text.toString(),
                message = binding.etMessage.text.toString(),
                replyMessageId = binding.etReplyMessageId.text.toString()
            )
            groupUserMessageViewModel.addGroupUserMessage(newMessage, object :
                OnCompleteListener<Void> {
                override fun onComplete(task: Task<Void>) {
                    if (task.isSuccessful) {
                    } else {
                        Toast.makeText(this@GroupUserMessageActivity, "add group user failed", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        // Get GroupUserMessages button click listener
        binding.btnGetMessages.setOnClickListener {
            groupUserMessageViewModel.getGroupUserMessages()
        }

        // Update GroupUserMessage button click listener
        binding.btnUpdateMessage.setOnClickListener {
            val messageId = binding.etMessageId.text.toString()
            val updatedMessage = GroupUserMessage(
                gumid = messageId,
                groupChatId = binding.etGroupChatId.text.toString(),
                groupUserId = binding.etGroupUserId.text.toString(),
                message = binding.etMessage.text.toString(),
                replyMessageId = binding.etReplyMessageId.text.toString()
            )
            groupUserMessageViewModel.updateGroupUserMessage(messageId, updatedMessage)
        }

        // Delete GroupUserMessage button click listener
        binding.btnDeleteMessage.setOnClickListener {
            val messageId = binding.etMessageId.text.toString()
            groupUserMessageViewModel.deleteGroupUserMessage(messageId)
        }
    }
}
