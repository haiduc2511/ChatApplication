package com.example.chatapplication2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.MessageAdapter
import com.example.chatapplication2.model.GroupUserMessage
import com.example.chatapplication2.viewmodel.GroupUserMessageViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatFragment : Fragment() {

    private lateinit var viewModel: GroupUserMessageViewModel
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[GroupUserMessageViewModel::class.java]

        val recyclerViewMessages: RecyclerView = view.findViewById(R.id.recyclerViewMessages)
        val editTextMessage: EditText = view.findViewById(R.id.editTextMessage)
        val buttonSend: ImageButton = view.findViewById(R.id.buttonSend)
        val fabBack: FloatingActionButton = view.findViewById(R.id.floatingActionButton)

        fabBack.setOnClickListener {
            removeFragment()
        }

        adapter = MessageAdapter()
        recyclerViewMessages.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMessages.adapter = adapter

        // Fetch messages for the group
        viewModel.fetchGroupUserMessages("group_123")

        // Observe messages
        viewModel.groupUserMessagesLiveData.observe(viewLifecycleOwner) { messages ->
            adapter.submitList(messages)
            recyclerViewMessages.scrollToPosition(adapter.itemCount - 1)
        }

        // Send button click listener
        buttonSend.setOnClickListener {
            val messageText = editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                val message = GroupUserMessage(
                    gumid = "",
                    groupChatId = "group_123",  // Mock Group ID
                    groupUserId = "user_001",    // Mock User ID
                    message = messageText,
                    replyMessageId = ""
                )
                viewModel.addGroupUserMessage(message)
                editTextMessage.text.clear()
            }
        }
    }
    private fun removeFragment() {
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

}