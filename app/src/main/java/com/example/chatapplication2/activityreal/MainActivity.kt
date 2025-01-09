package com.example.chatapplication2.activityreal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudinary.android.MediaManager
import com.example.chatapplication2.BuildConfig
import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.GroupAdapter
import com.example.chatapplication2.databinding.ActivityMainBinding
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.utils.MediaManagerState
import com.example.chatapplication2.viewmodel.BookViewModel
import com.example.chatapplication2.viewmodel.GroupViewModel
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val groupViewModel: GroupViewModel by viewModels()
    private val bookViewModel: BookViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCloudinary()
        initBottomNavigation()

        binding.imageView2.setOnClickListener {
            val intent = Intent(this@MainActivity, TestMainActivity::class.java)
            startActivity(intent)
        }

        binding.rvBookRecommendList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.ibSearch.setOnClickListener {
            val intent = Intent(this@MainActivity, ChatActivity::class.java)
            startActivity(intent)

        }
        groupViewModel.getGroups()
        bookViewModel.getBooks()
        val bookMap = HashMap<String, Book>()
        bookViewModel.booksLiveData.observe(this) { books ->
            // Handle the list of books, e.g., display in a RecyclerView
            for (book in books) {
                bookMap.put(book.bid, book);
            }
        }

        // Observe the groupsLiveData
        groupViewModel.groupsLiveData.observe(this, Observer { groups ->
            Toast.makeText(this, "co modification", Toast.LENGTH_SHORT).show()

            val adapter = GroupAdapter(groups, bookMap)
            binding.rvBookRecommendList.adapter = adapter
        })

        Log.d("duma", bookMap.toString() + "\n" + groupViewModel.getGroups().toString())

        // Observe errorLiveData for errors
        groupViewModel.errorLiveData.observe(this, Observer { error ->
            // Handle error (e.g., show a Toast or Log)
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

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
        binding.bnMain.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (R.id.nav_profile === id) {
            }
            if (R.id.nav_home === id) {
            }
            if (R.id.nav_search === id) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
            true
        })
    }


}