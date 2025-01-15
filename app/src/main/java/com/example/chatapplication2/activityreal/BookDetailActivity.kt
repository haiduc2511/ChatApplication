package com.example.chatapplication2.activityreal

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.chatapplication2.R
import com.example.chatapplication2.activitytest.GroupActivity
import com.example.chatapplication2.databinding.ActivityBookDetailBinding
import com.example.chatapplication2.databinding.ActivityReadBinding
import com.example.chatapplication2.model.Book
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        val book = intent.extras!!.getParcelable<Book>("book")!!

        binding.fabCreateGroup.setOnClickListener {
            Intent(this@BookDetailActivity, GroupActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putParcelable("book", book)
                putExtras(bundle)
                startActivity(this)
            }
        }

        Glide.with(this).load(book.bookPhotoLink).into(binding.ivBookCover)
        binding.tvBookSummary.text = book.bookSummary
        binding.tvBookTitle.text = book.bookTitle
        binding.tvAuthorName.text = book.authorName
    }
}