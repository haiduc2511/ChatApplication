package com.example.chatapplication2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication2.R
import com.example.chatapplication2.adapter.BookSearchAdapter
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.viewmodel.BookViewModel

class BookSearchFragment : Fragment() {

    private lateinit var viewModel: BookViewModel
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_search, container, false)
        viewModel = ViewModelProvider(this)[BookViewModel::class.java]

        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = BookSearchAdapter()
        recyclerView.adapter = adapter

        viewModel.getBooks()
        // Observe LiveData
        viewModel.booksLiveData.observe(viewLifecycleOwner) { books ->
            adapter.submitList(books)
        }

        // Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.getBooksByField("bookTitle", query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        var imageView3: ImageView = view.findViewById(R.id.imageView3)
        imageView3.setOnClickListener {
            val book: Book = adapter.currentList.get(0)
            Glide.with(this)
                .load(book.fileBookLink)
                .into(imageView3)
        }

        return view
    }
}