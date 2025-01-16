package com.example.chatapplication2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.GroupAdapter
import com.example.chatapplication2.adapter.GroupSearchAdapter
import com.example.chatapplication2.model.GroupEntryRequest
import com.example.chatapplication2.model.GroupUser
import com.example.chatapplication2.utils.FirebaseHelper
import com.example.chatapplication2.viewmodel.BookViewModel
import com.example.chatapplication2.viewmodel.GroupEntryRequestViewModel
import com.example.chatapplication2.viewmodel.GroupUserViewModel
import com.example.chatapplication2.viewmodel.GroupViewModel

class GroupSearchFragment : Fragment() {

    private lateinit var groupViewModel: GroupViewModel
    private lateinit var groupUserViewModel: GroupUserViewModel
    private lateinit var groupEntryRequestViewModel: GroupEntryRequestViewModel
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_group_search, container, false)
        groupViewModel = ViewModelProvider(this)[GroupViewModel::class.java]
        groupUserViewModel = ViewModelProvider(this)[GroupUserViewModel::class.java]
        groupEntryRequestViewModel = ViewModelProvider(this)[GroupEntryRequestViewModel::class.java]

        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = GroupSearchAdapter() {
//            groupUserViewModel.addGroupUser(GroupUser(
//                groupId = it.gid,
//                userId = FirebaseHelper.instance!!.getUserId()!!
//            ))
            groupEntryRequestViewModel.addGroupEntryRequest(
                GroupEntryRequest(
                    userId = FirebaseHelper.instance!!.getUserId()!!,
                    groupId = it.gid,
                    requestComment = "Làm ơn cho tôi vào"
                    )
            )
        }
        recyclerView.adapter = adapter
        groupViewModel.getGroups()

        // Observe LiveData
        groupViewModel.groupsLiveData.observe(viewLifecycleOwner) { groups ->
            adapter.submitList(groups)
        }

        // Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    groupViewModel.getGroupsByField("groupName", query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return view
    }
}
