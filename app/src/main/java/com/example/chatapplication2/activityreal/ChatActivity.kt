package com.example.chatapplication2.activityreal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.MessageAdapter
import com.example.chatapplication2.model.GroupUserMessage
import com.example.chatapplication2.viewmodel.GroupUserMessageViewModel

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

        viewModel.groupUserMessagesLiveData.observe(this) { messages ->
            adapter.submitList(messages)
        }

        buttonSend.setOnClickListener {
            val messageText = editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                val message = GroupUserMessage(
                    gumid = "",
                    groupChatId = "group_123",  // Group ID giả lập
                    groupUserId = "user_001",    // User ID giả lập
                    message = messageText,
                    replyMessageId = ""
                )
                viewModel.addGroupUserMessage(message)
                editTextMessage.text.clear()
            }
        }
    }
}
