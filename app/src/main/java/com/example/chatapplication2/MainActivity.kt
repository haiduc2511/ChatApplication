package com.example.chatapplication2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudinary.android.MediaManager
import com.example.chatapplication2.adapter.GroupAdapter
import com.example.chatapplication2.databinding.ActivityMainBinding
import com.example.chatapplication2.utils.MediaManagerState
import com.example.chatapplication2.viewmodel.GroupViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val groupViewModel: GroupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCloudinary()

        binding.button.setOnClickListener {
            val intent = Intent(this@MainActivity, TestMainActivity::class.java)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.ibSearch.setOnClickListener {
//            Toast.makeText(this, "duma", Toast.LENGTH_SHORT).show()
            groupViewModel.getGroups()
        }

        // Observe the groupsLiveData
        groupViewModel.groupsLiveData.observe(this, Observer { groups ->
            Toast.makeText(this, "co modification", Toast.LENGTH_SHORT).show()

            val adapter = GroupAdapter(groups)
            binding.recyclerView.adapter = adapter
        })

        // Observe errorLiveData for errors
        groupViewModel.errorLiveData.observe(this, Observer { error ->
            // Handle error (e.g., show a Toast or Log)
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch sample groups
        groupViewModel.getGroups()
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


}