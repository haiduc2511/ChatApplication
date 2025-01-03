package com.example.chatapplication2.activitytest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.R
import com.example.chatapplication2.model.GroupUser

import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.chatapplication2.databinding.ActivityGroupUserBinding
import com.example.chatapplication2.viewmodel.GroupUserViewModel

class GroupUserActivity : AppCompatActivity() {

    private val groupUserViewModel: GroupUserViewModel by viewModels()
    private lateinit var binding: ActivityGroupUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe the list of group users
        groupUserViewModel.groupUsersLiveData.observe(this, Observer { users ->
            // Update the UI with the list of group users (e.g., in a RecyclerView)
            // Example:
            // groupUserAdapter.submitList(users)
        })

        // Observe error messages
        groupUserViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Add GroupUser button click listener
        binding.btnAddGroupUser.setOnClickListener {
            val newGroupUser = GroupUser(
                guid = "", // ID will be generated by Firestore
                groupId = binding.etGroupId.text.toString(),
                userId = binding.etUserId.text.toString()
            )
            groupUserViewModel.addGroupUser(newGroupUser)
        }

        // Get GroupUsers button click listener
        binding.btnGetGroupUsers.setOnClickListener {
            groupUserViewModel.getGroupUsers()
        }

        // Update GroupUser button click listener
        binding.btnUpdateGroupUser.setOnClickListener {
            val userId = binding.etUserId.text.toString()
            val updatedGroupUser = GroupUser(
                guid = userId,
                groupId = binding.etGroupId.text.toString(),
                userId = userId
            )
            groupUserViewModel.updateGroupUser(userId, updatedGroupUser)
        }

        // Delete GroupUser button click listener
        binding.btnDeleteGroupUser.setOnClickListener {
            val userId = binding.etUserId.text.toString()
            groupUserViewModel.deleteGroupUser(userId)
        }
    }
}
