package com.example.chatapplication2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.databinding.ItemGroupBinding
import com.example.chatapplication2.model.Group

class GroupAdapter(private val groups: List<Group>) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvGroupName.setText("${groups[position].groupName}")
        holder.binding.tvBookName.setText("${groups[position].bookId}")

        Log.d("duma groupName", groups[position].groupName)
        Log.d("duma privacyMode", groups[position].privacyMode)
    }

    override fun getItemCount(): Int = groups.size
}
