package com.example.chatapplication2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.GroupToChatAdapter
import com.example.chatapplication2.databinding.FragmentGroupChatBinding
import com.example.chatapplication2.viewmodel.GroupViewModel

class GroupChatFragment : Fragment() {

    private lateinit var binding: FragmentGroupChatBinding
    private val groupViewModel: GroupViewModel by viewModels()
    private lateinit var groupToChatAdapter: GroupToChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        // Fetch groups from ViewModel
        groupViewModel.getGroups()
    }

    private fun setupRecyclerView() {
        groupToChatAdapter = GroupToChatAdapter { group ->
            Toast.makeText(requireContext(), group.toString(), Toast.LENGTH_SHORT).show()
        }
        binding.groupsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.groupsRecyclerView.adapter = groupToChatAdapter
    }
    private fun observeViewModel() {
        groupViewModel.groupsLiveData.observe(viewLifecycleOwner) { groups ->
            if (groups.isNullOrEmpty()) {
                binding.noGroupsTextView.visibility = View.VISIBLE
                binding.groupsRecyclerView.visibility = View.GONE
            } else {
                binding.noGroupsTextView.visibility = View.GONE
                binding.groupsRecyclerView.visibility = View.VISIBLE
                groupToChatAdapter.submitList(groups)
            }
        }

        groupViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }
}