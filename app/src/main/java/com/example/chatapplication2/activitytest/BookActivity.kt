package com.example.chatapplication2.activitytest

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.chatapplication2.databinding.ActivityBookBinding
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.viewmodel.BookViewModel

class BookActivity : AppCompatActivity() {

    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var binding: ActivityBookBinding
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()

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
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        }
    }
    private val REQUEST_CODE = 1

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    val PICK_IMAGES_REQUEST = 1
    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            Intent.createChooser(intent, "Select Pictures"),
            PICK_IMAGES_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                val uriList: MutableList<Uri?> = ArrayList()
                if (data.clipData != null) { // Multiple images selected
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        Log.d("URI", imageUri.toString())
                        uriList.add(imageUri)
                    }
                    uri = uriList[0]!!
                } else if (data.data != null) { // Single image selected
                    val imageUri = data.data
                    uri = imageUri!!
                }
                uploadBookCoverToCloudinary(uri)
            }
        }
    }
    private fun uploadBookCoverToCloudinary(uri: Uri) {
        bookViewModel.uploadBookCoverCloudinary(uri, object : UploadCallback {
            override fun onStart(requestId: String) {
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                val imageUrl = (resultData["secure_url"] as String?)!!
                Log.d(ContentValues.TAG, "Upload successful. Image URL: $imageUrl")
            }

            override fun onError(requestId: String, error: ErrorInfo) {
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {}
        })
    }

}
