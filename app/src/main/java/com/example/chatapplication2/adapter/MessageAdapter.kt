package com.example.chatapplication2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication2.R
import com.example.chatapplication2.model.GroupUserMessage
import com.example.chatapplication2.model.User
import com.google.firebase.firestore.FirebaseFirestore

class MessageAdapter : ListAdapter<GroupUserMessage, MessageAdapter.MessageViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar: ImageView = itemView.findViewById(R.id.imageViewAvatar)
        private val senderName: TextView = itemView.findViewById(R.id.textViewSenderName)
        private val messageText: TextView = itemView.findViewById(R.id.textViewMessage)
        private val timestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)

        fun bind(message: GroupUserMessage) {
            // Bind data to views
            senderName.text = message.groupUserId // You can replace with actual user name if available
            FirebaseFirestore.getInstance().collection("users").document(message.groupUserId).get().addOnCompleteListener {
                var user = it.result?.toObject(User::class.java)
                senderName.text = user!!.name
                Glide.with(itemView.context).load(user.avaLink).error(R.drawable.ic_user).into(avatar)
            }
            messageText.text = message.message
            timestamp.text = formatTimestamp(message.timeStamp)

            // Placeholder for avatar image
        }

        private fun formatTimestamp(timestamp: String): String {
            // Format timestamp to a readable format (you can adjust as needed)
            return if (timestamp.isNotEmpty()) timestamp else "Now"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GroupUserMessage>() {
        override fun areItemsTheSame(oldItem: GroupUserMessage, newItem: GroupUserMessage) =
            oldItem.gumid == newItem.gumid

        override fun areContentsTheSame(oldItem: GroupUserMessage, newItem: GroupUserMessage) =
            oldItem == newItem
    }
}
