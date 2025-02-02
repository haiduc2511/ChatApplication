package com.example.chatapplication2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication2.databinding.ItemGroupBinding
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.utils.SharedPreferenceManager

class GroupAdapter(private val groups: List<Group>,
                   private val books: HashMap<String, Book>,
                   private val onGroupClick: () -> Unit
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
            holder.binding.tvBookName.setText("Tên sách: " + book!!.bookTitle)
            Glide.with(holder.itemView.context).asBitmap().load(book.bookPhotoLink).into(holder.binding.ivBookCover)
        } else {
            holder.binding.tvBookName.setText("Sách không tồn tại")

        }
        holder.itemView.setOnClickListener {
            SharedPreferenceManager(holder.itemView.context).apply {
                setString("mostRecentGroupId", groups[position].gid)
                setString("aboutToReadGroupId", groups[position].gid)
                setString("aboutToReadBookId", book!!.bid)
            }
            onGroupClick()
//            Intent(it.context, ReadActivity::class.java).apply {
//                putExtra("group", groups[position]) // Replace "key_name" and "YourStringValue" with your key and value
//                putExtra("book", book)
//                it.context.startActivity(this)
//            }
        }
    }

    override fun getItemCount(): Int = groups.size
}
