package com.example.chatapplication2.activityreal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cloudinary.Cloudinary
import com.cloudinary.Transformation
import com.cloudinary.android.MediaManager
import com.example.chatapplication2.BuildConfig
import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.GroupAdapter
import com.example.chatapplication2.databinding.ActivityMainBinding
import com.example.chatapplication2.fragment.AccountFragment
import com.example.chatapplication2.fragment.ChatFragment
import com.example.chatapplication2.fragment.GroupChatFragment
import com.example.chatapplication2.fragment.MainFragment
import com.example.chatapplication2.fragment.ReadFragment
import com.example.chatapplication2.fragment.SearchFragment
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.utils.MediaManagerState
import com.example.chatapplication2.utils.SharedPreferenceManager
import com.example.chatapplication2.viewmodel.BookViewModel
import com.example.chatapplication2.viewmodel.GroupViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.firestore.QuerySnapshot

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val groupViewModel: GroupViewModel by viewModels()
    private val bookViewModel: BookViewModel by viewModels()
    private val bookMap = HashMap<String, Book>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCloudinary()
        initBottomNavigation()

    }


    private fun initCloudinary() {
        if (!MediaManagerState.isInitialized) {
            val config: MutableMap<String?, Any?> = HashMap()
            config["cloud_name"] = BuildConfig.CLOUD_NAME
            config["api_key"] = BuildConfig.API_KEY
            config["api_secret"] = BuildConfig.API_SECRET
            //        config.put("secure", true);
            MediaManager.init(this, config)
            MediaManagerState.initialize()
        }
    }
    private fun initBottomNavigation() {
        val fragmentManager = supportFragmentManager
        val fragments = mutableMapOf<Int, Fragment>()

        // Create the first fragment (MainFragment) and add it to the container

        val readFragment = ReadFragment()
        fragments[R.id.nav_read] = readFragment
        val groupChatFragment = GroupChatFragment()
        fragments[R.id.nav_chat] = groupChatFragment
        val searchFragment = SearchFragment()
        fragments[R.id.nav_search] = searchFragment
        val accountFragment = AccountFragment()
        fragments[R.id.nav_profile] = accountFragment
        val mainFragment = MainFragment() {
            readFragment.loadNewGroupFromMainFragment()
            binding.bnMain.selectedItemId = R.id.nav_read
        }
        fragments[R.id.nav_home] = mainFragment

        fragmentManager.beginTransaction()
            .add(R.id.fragment_container, mainFragment)
            .add(R.id.fragment_container, readFragment)
            .hide(readFragment)
            .add(R.id.fragment_container, groupChatFragment)
            .hide(groupChatFragment)
            .add(R.id.fragment_container, searchFragment)
            .hide(searchFragment)
            .add(R.id.fragment_container, accountFragment)
            .hide(accountFragment)
            .commit()

        var activeFragment: Fragment = mainFragment

        binding.bnMain.setOnItemSelectedListener { menuItem ->
            val selectedFragment = when (menuItem.itemId) {
                R.id.nav_home -> fragments.get(R.id.nav_home)
                R.id.nav_read -> fragments.get(R.id.nav_read)
                R.id.nav_chat -> fragments.get(R.id.nav_chat)
                R.id.nav_search -> fragments.get(R.id.nav_search)
                R.id.nav_profile -> fragments.get(R.id.nav_profile)
                else -> return@setOnItemSelectedListener false
            }!!

            // Show the selected fragment and hide the current one
            if (selectedFragment != activeFragment) {
                fragmentManager.beginTransaction()
                    .hide(activeFragment)
                    .show(selectedFragment)
                    .commit()
                activeFragment = selectedFragment
            } else {
                // Remove fragment hiện tại
                fragmentManager.beginTransaction().remove(activeFragment).commit()
                fragmentManager.executePendingTransactions()

                // Tạo lại fragment mới
                val newFragment = when (menuItem.itemId) {
                    R.id.nav_home -> MainFragment() {
                        readFragment.loadNewGroupFromMainFragment()
                        binding.bnMain.selectedItemId = R.id.nav_read
                    }
                    R.id.nav_read -> ReadFragment()
                    R.id.nav_chat -> GroupChatFragment()
                    R.id.nav_search -> SearchFragment()
                    R.id.nav_profile -> AccountFragment()
                    else -> return@setOnItemSelectedListener false
                }
                fragments[menuItem.itemId] = newFragment

                // Thêm lại fragment vào container
                fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, newFragment)
                    .commit()

                activeFragment = newFragment

                }

            true
        }

    }

}