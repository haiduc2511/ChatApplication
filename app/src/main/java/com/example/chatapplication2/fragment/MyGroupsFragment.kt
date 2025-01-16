package com.example.chatapplication2.fragment

import com.example.chatapplication2.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.adapter.GroupSearchAdapter
import com.example.chatapplication2.model.GroupEntryRequest
import com.example.chatapplication2.utils.FirebaseHelper
import com.example.chatapplication2.viewmodel.GroupEntryRequestViewModel
import com.example.chatapplication2.viewmodel.GroupUserViewModel
import com.example.chatapplication2.viewmodel.GroupViewModel

class MyGroupsFragment : Fragment() {
    private lateinit var groupViewModel: GroupViewModel
    private lateinit var groupUserViewModel: GroupUserViewModel
    private lateinit var groupEntryRequestViewModel: GroupEntryRequestViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_groups, container, false)
        groupViewModel = ViewModelProvider(this)[GroupViewModel::class.java]
        groupUserViewModel = ViewModelProvider(this)[GroupUserViewModel::class.java]
        groupEntryRequestViewModel = ViewModelProvider(this)[GroupEntryRequestViewModel::class.java]

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = GroupSearchAdapter() {

        }
        recyclerView.adapter = adapter
        groupViewModel.getGroupsByField("adminUserId", FirebaseHelper.instance!!.getUserId()!!)

        // Observe LiveData
        groupViewModel.groupsLiveData.observe(viewLifecycleOwner) { groups ->
            adapter.submitList(groups)
        }

        return view
    }
}
