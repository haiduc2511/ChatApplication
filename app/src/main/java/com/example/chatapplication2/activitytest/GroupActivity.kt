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
import com.example.chatapplication2.databinding.ActivityGroupBinding
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.viewmodel.GroupViewModel

class GroupActivity : AppCompatActivity() {

    private val groupViewModel: GroupViewModel by viewModels()
    private lateinit var binding: ActivityGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe groups LiveData
        groupViewModel.groupsLiveData.observe(this, Observer { groups ->
            // Handle displaying groups in the UI (e.g., in a RecyclerView)
            // Example:
            // groupAdapter.submitList(groups)
        })

        // Observe error messages
        groupViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Add Group button click listener
        binding.btnAddGroup.setOnClickListener {
            val newGroup = Group(
                gid = "", // ID will be generated by Firestore
                groupName = binding.etGroupName.text.toString(),
                bookId = binding.etBookId.text.toString(),
                privacyMode = binding.etPrivacyMode.text.toString(),
                adminUserId = binding.etAdminUserId.text.toString()
            )
            groupViewModel.addGroup(newGroup)
        }

        // Get Groups button click listener
        binding.btnGetGroups.setOnClickListener {
            groupViewModel.getGroups()
        }

        // Update Group button click listener
        binding.btnUpdateGroup.setOnClickListener {
            val groupId = binding.etGroupId.text.toString()
            val updatedGroup = Group(
                gid = groupId,
                groupName = binding.etGroupName.text.toString(),
                bookId = binding.etBookId.text.toString(),
                privacyMode = binding.etPrivacyMode.text.toString(),
                adminUserId = binding.etAdminUserId.text.toString()
            )
            groupViewModel.updateGroup(groupId, updatedGroup)
        }

        // Delete Group button click listener
        binding.btnDeleteGroup.setOnClickListener {
            val groupId = binding.etGroupId.text.toString()
            groupViewModel.deleteGroup(groupId)
        }
    }
}
