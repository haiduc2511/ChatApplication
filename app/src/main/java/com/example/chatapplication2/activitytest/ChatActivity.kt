package com.example.chatapplication2.activitytest

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.MessageAdapter
import com.example.chatapplication2.model.GroupUserMessage
import com.example.chatapplication2.viewmodel.GroupUserMessageViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: GroupUserMessageViewModel
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        viewModel = ViewModelProvider(this)[GroupUserMessageViewModel::class.java]

        val recyclerViewMessages: RecyclerView = findViewById(R.id.recyclerViewMessages)
        val editTextMessage: EditText = findViewById(R.id.editTextMessage)
        val buttonSend: ImageButton = findViewById(R.id.buttonSend)

        adapter = MessageAdapter()
        recyclerViewMessages.layoutManager = LinearLayoutManager(this)
        recyclerViewMessages.adapter = adapter

        viewModel.fetchGroupUserMessages("group_123")

        // Observe messages
        viewModel.groupUserMessagesLiveData.observe(this) { messages ->
            adapter.submitList(messages)
            recyclerViewMessages.scrollToPosition(adapter.itemCount - 1)
        }


        buttonSend.setOnClickListener {
            val messageText = editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                val message = GroupUserMessage(
                    gumid = "",
                    groupChatId = "group_123",  // Group ID giả lập
                    groupUserId = "user_001",    // User ID giả lập
                    message = messageText,
                    timeStamp = ""
                )
                viewModel.addGroupUserMessage(message, object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {

                        } else {
                        }
                    }
                })
                editTextMessage.text.clear()
            }
        }
    }
}
