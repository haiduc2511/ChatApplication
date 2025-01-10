package com.example.chatapplication2.activityreal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

    override fun onResume() {
        SharedPreferenceManager(this).getString("mostRecentGroupId").apply {
            if (this.equals("")) {
                Glide.with(this@MainActivity).load(R.drawable.img_demo_book).into(binding.ivRecentBookCover)
            } else {
                groupViewModel.getGroupById(this, object :
                    OnCompleteListener<QuerySnapshot> {
                    override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful) {
                            val groups = task.result?.toObjects(Group::class.java) ?: emptyList()
                            Log.d("mostRecentGroupId", this@apply + " " + groups.toString())
                            if (!groups.isEmpty()) {
                                val mostRecentGroup = groups[0]
                                Log.d("mostRecentGroupId", mostRecentGroup.toString())
                                binding.tvRecentGroupName.text = mostRecentGroup.groupName
                                binding.tvRecentBookName.text = if (bookMap.containsKey(mostRecentGroup.bookId)) bookMap[mostRecentGroup.bookId]!!.bookTitle else "duma"
                                Glide.with(this@MainActivity).load(groups[0].groupPhotoLink).into(binding.ivRecentBookCover)
                            }
                        }
                    }
                })
            }
        }

        super.onResume()
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