package com.example.chatapplication2.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatapplication2.R
import com.example.chatapplication2.activityreal.ChatActivity
import com.example.chatapplication2.activityreal.TestMainActivity
import com.example.chatapplication2.adapter.GroupAdapter
import com.example.chatapplication2.databinding.FragmentMainBinding
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.utils.FirebaseHelper
import com.example.chatapplication2.viewmodel.BookViewModel
import com.example.chatapplication2.viewmodel.GroupViewModel
import com.example.chatapplication2.utils.SharedPreferenceManager
import com.example.chatapplication2.viewmodel.GroupUserViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class MainFragment(
    private val onGroupClick: () -> Unit
) : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val groupViewModel: GroupViewModel by viewModels()
    private val groupUserViewModel: GroupUserViewModel by viewModels()
    private val bookViewModel: BookViewModel by viewModels()
    private val bookMap = HashMap<String, Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView2.setOnClickListener {

            val intent = Intent(requireContext(), TestMainActivity::class.java)
            startActivity(intent)
        }


//        binding.ibSearch.setOnClickListener {
//            val intent = Intent(requireContext(), ChatActivity::class.java)
//            startActivity(intent)
//
//        }

        // Set up RecyclerView
        binding.rvBookRecommendList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Fetch groups and books from ViewModel
        val userId = FirebaseHelper.instance!!.getUserId()!!
        Toast.makeText(context, userId, Toast.LENGTH_SHORT).show()

        groupUserViewModel.getGroupUsersByField("userId", userId)
        groupUserViewModel.groupUsersLiveData.observe(viewLifecycleOwner) {groupUsers ->
            Log.d("checkGroupUsers", groupUsers.toString())
            val groupIds: List<String> = groupUsers.map { it.groupId }
            Log.d("checkIds", groupIds.toString())

            if (groupIds.isNotEmpty()) {
                groupViewModel.getGroupsByIds(groupIds)
            }
        }
        bookViewModel.getBooks()

        // Observe booksLiveData
        bookViewModel.booksLiveData.observe(viewLifecycleOwner) { books ->
            for (book in books) {
                bookMap[book.bid] = book
            }
        }

        // Observe groupsLiveData
        groupViewModel.groupsLiveData.observe(viewLifecycleOwner) { groups ->
            Log.d("checkGroups", groups.toString())
//            Toast.makeText(requireContext(), "Data updated", Toast.LENGTH_SHORT).show()
            val adapter = GroupAdapter(groups, bookMap) {
                onGroupClick()
            }
            binding.rvBookRecommendList.adapter = adapter
        }

        // Observe errorLiveData
        groupViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferenceManager = SharedPreferenceManager(requireContext())
        val mostRecentGroupId = sharedPreferenceManager.getString("mostRecentGroupId")

        if (mostRecentGroupId.isNullOrEmpty()) {
            Glide.with(this)
                .load(R.drawable.img_demo_book)
                .into(binding.ivRecentBookCover)
        } else {
            groupViewModel.getGroupById(mostRecentGroupId, object : OnCompleteListener<QuerySnapshot> {
                override fun onComplete(task: Task<QuerySnapshot>) {
                    if (task.isSuccessful) {
                        val groups = task.result?.toObjects(Group::class.java) ?: emptyList()
                        if (groups.isNotEmpty()) {
                            val mostRecentGroup = groups[0]
                            binding.tvRecentGroupName.text = mostRecentGroup.groupName
                            binding.tvRecentBookName.text =
                                bookMap[mostRecentGroup.bookId]?.bookTitle ?: "Unknown"
                            Glide.with(this@MainFragment)
                                .load(bookMap[mostRecentGroup.bookId]?.bookPhotoLink)
                                .into(binding.ivRecentBookCover)
                        }
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
