package com.example.chatapplication2.activitytest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.chatapplication2.databinding.ActivityBookBinding
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.viewmodel.BookViewModel
import androidx.activity.viewModels

class BookActivity : AppCompatActivity() {

    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var binding: ActivityBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe books LiveData
        bookViewModel.booksLiveData.observe(this, Observer { books ->
            Toast.makeText(this, books.toString(), Toast.LENGTH_SHORT).show();
            // Handle displaying books in the UI (e.g., in a RecyclerView)
            // Example:
            // bookAdapter.submitList(books)
        })

        // Observe error messages
        bookViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Add Book button click listener
        binding.btnAddBook.setOnClickListener {
            val newBook = Book(
                bid = "", // Will be generated by Firestore
                bookTitle = binding.etBookTitle.text.toString(),
                userUploadId = "user123", // Example user ID
                bookSummary = binding.etBookSummary.text.toString(),
                fileBookLink = binding.etFileBookLink.text.toString(),
                authorName = binding.etAuthorName.text.toString()
            )
            bookViewModel.addBook(newBook)
        }

        // Get Books button click listener
        binding.btnGetBooks.setOnClickListener {
            bookViewModel.getBooks()
        }
    }
}
