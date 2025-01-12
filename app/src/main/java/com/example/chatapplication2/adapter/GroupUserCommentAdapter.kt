package com.example.chatapplication2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.R
import com.example.chatapplication2.databinding.ItemGroupUserCommentBinding
import com.example.chatapplication2.model.GroupUserComment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GroupUserCommentAdapter : ListAdapter<GroupUserComment, GroupUserCommentAdapter.ViewHolder>(
    DiffCallback()
) {

    inner class ViewHolder(private val binding: ItemGroupUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: GroupUserComment) {
            binding.tvUserName.text = comment.groupUserId
            binding.tvComment.text = comment.comment
            binding.tvTimeStamp.text = formatTimestamp(comment.timeStamp)
        }

        private fun formatTimestamp(timestamp: String): String {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val date = Date(timestamp.toLong())
            return sdf.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGroupUserCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<GroupUserComment>() {
        override fun areItemsTheSame(oldItem: GroupUserComment, newItem: GroupUserComment): Boolean {
            return oldItem.gucid == newItem.gucid
        }

        override fun areContentsTheSame(oldItem: GroupUserComment, newItem: GroupUserComment): Boolean {
            return oldItem == newItem
        }
    }
}
