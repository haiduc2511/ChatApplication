package com.example.chatapplication2.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.R
import com.example.chatapplication2.model.Group

class GroupSearchAdapter(
    private val onGroupClick: (Group) -> Unit
) : ListAdapter<Group, GroupSearchAdapter.GroupViewHolder>(GroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_search, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = getItem(position)
        holder.bind(group)
        holder.itemView.findViewById<Button>(R.id.bt_join).setOnClickListener {
            onGroupClick(group)
        }
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(group: Group) {
            itemView.findViewById<TextView>(R.id.groupName).text = group.groupName
        }
    }
    class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean = oldItem.gid == newItem.gid
        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean = oldItem == newItem
    }

}

