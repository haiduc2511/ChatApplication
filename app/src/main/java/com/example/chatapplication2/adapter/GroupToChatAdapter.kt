package com.example.chatapplication2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication2.databinding.ItemGroupToChatBinding
import com.example.chatapplication2.model.Group

class GroupToChatAdapter(
    private val onGroupClick: (Group) -> Unit
) : ListAdapter<Group, GroupToChatAdapter.GroupViewHolder>(GroupSearchAdapter.GroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemGroupToChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = getItem(position)
        holder.bind(group)
    }

    inner class GroupViewHolder(private val binding: ItemGroupToChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group) {
            binding.groupNameTextView.text = group.groupName
            Glide.with(binding.groupImageView.context)
                .load(group.groupPhotoLink)
                .into(binding.groupImageView)

            binding.root.setOnClickListener {
                onGroupClick(group)
            }
        }
    }

    class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.gid == newItem.gid
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }
}
