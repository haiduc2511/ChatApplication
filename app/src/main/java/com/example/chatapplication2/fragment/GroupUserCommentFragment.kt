package com.example.chatapplication2.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.GroupUserCommentAdapter
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.model.GroupUserComment
import com.example.chatapplication2.utils.FirebaseHelper
import com.example.chatapplication2.viewmodel.GroupUserCommentViewModel
import java.util.UUID

class GroupUserCommentFragment(
    private val group: Group,
    private val pageNumber : Int,
    private val posX : Float = 0f,
    private val posY : Float = 0f) : Fragment() {

    private lateinit var groupUserCommentViewModel: GroupUserCommentViewModel
    private lateinit var adapter: GroupUserCommentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var etComment: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var clMain: ConstraintLayout
    private lateinit var cvInsideMain: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_group_user_comment, container, false)

        groupUserCommentViewModel = ViewModelProvider(this)[GroupUserCommentViewModel::class.java]
        recyclerView = view.findViewById(R.id.recyclerViewGroupComments)
        etComment = view.findViewById(R.id.etComment)
        btnSend = view.findViewById(R.id.btnSend)
        clMain = view.findViewById(R.id.main)
        cvInsideMain = view.findViewById(R.id.cv_inside_main)

        // Thiết lập adapter cho RecyclerView
        adapter = GroupUserCommentAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Lắng nghe dữ liệu từ ViewModel
        groupUserCommentViewModel.groupUserCommentsLiveData.observe(viewLifecycleOwner) { comments ->
            val filteredCommentsList: List<GroupUserComment> = comments.filter {
                Log.d("check filteredCommentsList", "$posX $posY $pageNumber  $it")
                it.pagePositionX.toFloat() == posX
                        && it.pagePositionX.toFloat() == posY
                         it.pageNumber.toInt() == pageNumber}
            adapter.submitList(filteredCommentsList)
        }

        clMain.setOnClickListener {
            removeFragment()
        }
        cvInsideMain.setOnClickListener {

        }

        // Nút gửi comment
        btnSend.setOnClickListener {
            val commentText = etComment.text.toString().trim()
            if (commentText.isNotEmpty()) {
                val newComment = GroupUserComment(
                    gucid = UUID.randomUUID().toString(),
                    userId = FirebaseHelper.instance!!.getUserId()!!, //TODO: GroupUserId chứ kp UserId
                    groupId = group.gid,
                    comment = commentText,
                    pageNumber = pageNumber.toString(),
                    pagePositionX = posX.toString(),
                    pagePositionY = posY.toString(),
                    timeStamp = System.currentTimeMillis().toString()
                )
                groupUserCommentViewModel.addGroupUserCommentInGroupUserCommentFragment(newComment, group)
                etComment.text.clear()
            }
        }

        // Lấy danh sách comments
        groupUserCommentViewModel.getGroupUserCommentsByField("groupId", group.gid)

        return view
    }
    private fun removeFragment() {
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }


}
