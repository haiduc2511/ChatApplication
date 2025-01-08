package com.example.chatapplication2.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chatapplication2.fragment.BookSearchFragment
import com.example.chatapplication2.fragment.GroupSearchFragment

class SearchPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) BookSearchFragment() else GroupSearchFragment()
    }
}
