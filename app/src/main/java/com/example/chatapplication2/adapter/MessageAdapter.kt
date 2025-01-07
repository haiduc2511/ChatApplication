package com.example.chatapplication2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.model.GroupUserMessage

class MessageAdapter : ListAdapter<GroupUserMessage, MessageAdapter.MessageViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: GroupUserMessage) {
            (itemView as TextView).text = message.message
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GroupUserMessage>() {
        override fun areItemsTheSame(oldItem: GroupUserMessage, newItem: GroupUserMessage) =
            oldItem.gumid == newItem.gumid

        override fun areContentsTheSame(oldItem: GroupUserMessage, newItem: GroupUserMessage) =
            oldItem == newItem
    }
}
