package com.example.chatapplication2.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chatapplication2.fragment.MyGroupsFragment
import com.example.chatapplication2.fragment.MyPostsFragment

class AccountPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyPostsFragment()
            1 -> MyGroupsFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
