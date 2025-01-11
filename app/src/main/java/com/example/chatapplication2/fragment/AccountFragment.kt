package com.example.chatapplication2.fragment

import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.AccountPagerAdapter
import de.hdodenhof.circleimageview.CircleImageView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        // Set profile picture
        val profileImage: CircleImageView = view.findViewById(R.id.profile_image)
        profileImage.setImageResource(R.drawable.ic_user)

        // Setup ViewPager and TabLayout
        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
        viewPager.adapter = AccountPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "My Posts"
                1 -> "My Groups"
                else -> null
            }
        }.attach()

        return view
    }
}
