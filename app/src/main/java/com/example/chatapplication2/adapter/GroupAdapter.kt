package com.example.chatapplication2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication2.databinding.ItemGroupBinding
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.model.Group

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
            Glide.with(holder.itemView.context).asBitmap().load(book.fileBookLink).into(holder.binding.ivBookCover)
        }
        Log.d("duma groupName", groups[position].groupName)
        Log.d("duma privacyMode", groups[position].privacyMode)
    }

    override fun getItemCount(): Int = groups.size
}
