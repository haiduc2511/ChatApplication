package com.example.chatapplication2.activitytest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.R

import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.chatapplication2.databinding.ActivityTagBinding
import com.example.chatapplication2.model.Tag
import com.example.chatapplication2.viewmodel.TagViewModel

class TagActivity : AppCompatActivity() {

    private val tagViewModel: TagViewModel by viewModels()
    private lateinit var binding: ActivityTagBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe the list of tags
        tagViewModel.tagsLiveData.observe(this, Observer { tags ->
            // Update the UI with the list of tags (e.g., in a RecyclerView)
            // Example:
            // tagAdapter.submitList(tags)
        })

        // Observe error messages
        tagViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Add Tag button click listener
        binding.btnAddTag.setOnClickListener {
            val newTag = Tag(
                tagName = binding.etTagName.text.toString()
            )
            tagViewModel.addTag(newTag)
        }

        // Get Tags button click listener
        binding.btnGetTags.setOnClickListener {
            tagViewModel.getTags()
        }

        // Update Tag button click listener
        binding.btnUpdateTag.setOnClickListener {
            val updatedTag = Tag(
                tid = binding.etTagId.text.toString(),
                tagName = binding.etTagName.text.toString()
            )
            tagViewModel.updateTag(updatedTag.tid, updatedTag)
        }

        // Delete Tag button click listener
        binding.btnDeleteTag.setOnClickListener {
            val tagId = binding.etTagId.text.toString()
            tagViewModel.deleteTag(tagId)
        }
    }
}
