package com.example.chatapplication2.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication2.activityreal.ReadActivity
import com.example.chatapplication2.databinding.ItemGroupBinding
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.utils.SharedPreferenceManager

class GroupAdapter(private val groups: List<Group>,
                   private val books: HashMap<String, Book>
    ) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvGroupName.setText("${groups[position].groupName}")
        val book = books.get("${groups[position].bookId}")
        if (book != null) {
            holder.binding.tvBookName.setText("" + book!!.bookTitle)
        }
        Glide.with(holder.itemView.context).asBitmap().load(groups[position].groupPhotoLink).into(holder.binding.ivBookCover)
        holder.itemView.setOnClickListener {
            SharedPreferenceManager(holder.itemView.context).apply {
                setString("mostRecentGroupId", groups[position].gid)
                setString("aboutToReadGroupId", groups[position].gid)
                setString("aboutToReadBookId", book!!.bid)
            }
            Intent(it.context, ReadActivity::class.java).apply {
                putExtra("group", groups[position]) // Replace "key_name" and "YourStringValue" with your key and value
                putExtra("book", book)
                it.context.startActivity(this)
            }
        }
    }

    override fun getItemCount(): Int = groups.size
}
