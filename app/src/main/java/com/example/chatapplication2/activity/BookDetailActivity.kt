package com.example.chatapplication2.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chatapplication2.databinding.ActivityBookDetailBinding
import com.example.chatapplication2.model.Book

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