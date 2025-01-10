package com.example.chatapplication2.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.chatapplication2.R
import com.example.chatapplication2.databinding.FragmentReadBinding
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.utils.SharedPreferenceManager
import com.example.chatapplication2.viewmodel.BookViewModel
import com.example.chatapplication2.viewmodel.GroupViewModel
import com.github.barteksc.pdfviewer.listener.OnLongPressListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnTapListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class ReadFragment : Fragment() {

    private var _binding: FragmentReadBinding? = null
    private val binding get() = _binding!!
    private val groupViewModel: GroupViewModel by viewModels()
    private val bookViewModel: BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val groupId: String = SharedPreferenceManager(requireContext()).getString("aboutToReadGroupId")
        val bookId: String = SharedPreferenceManager(requireContext()).getString("aboutToReadBookId")
        Log.d("check book null", bookId.toString())
        Log.d("check group null", groupId.toString())

        groupViewModel.getGroupById(groupId, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val groups = task.result?.toObjects(Group::class.java) ?: emptyList()
                    if (groups.isNotEmpty()) {
                    }
                }
            }
        })

        bookViewModel.getBookById(bookId, object : OnCompleteListener<QuerySnapshot> {
            override fun onComplete(task: Task<QuerySnapshot>) {
                if (task.isSuccessful) {
                    val books = task.result?.toObjects(Book::class.java) ?: emptyList()
                    if (books.isNotEmpty()) {
                        downloadAndOpenPdf(books[0].fileBookLink)
                    }
                }
            }
        })


        binding.tvChooseUri.text = groupId.toString()
        binding.tvChooseUri.setOnClickListener {
            openFileChooser()
        }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.let { uri ->
                openPdf(uri)
            }
        }
    }

    private fun downloadAndOpenPdf(pdfUrl: String) {
        lifecycleScope.launch {
            try {
                val pdfFile = withContext(Dispatchers.IO) {
                    downloadPdfFromUrl(pdfUrl)
                }
                openPdf(Uri.fromFile(pdfFile))
            } catch (e: Exception) {
                Log.d("ReadFragment", e.message.toString())
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun downloadPdfFromUrl(url: String): File {
        val pdfFile = File.createTempFile("downloaded", ".pdf", requireContext().cacheDir)
        withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openStream()
                val output = FileOutputStream(pdfFile)
                connection.use { input ->
                    output.use { outputStream ->
                        input.copyTo(outputStream)
                    }
                }

                if (!isPdfFile(pdfFile)) {
                    throw IOException("File is not a valid PDF")
                }
            } catch (e: Exception) {
                throw IOException("Failed to download PDF: ${e.message}")
            }
        }
        return pdfFile
    }

    private fun isPdfFile(file: File): Boolean {
        return try {
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(4)
            inputStream.read(buffer)
            inputStream.close()
            val fileHeader = String(buffer)
            fileHeader == "%PDF"
        } catch (e: Exception) {
            false
        }
    }

    private fun openPdf(uri: Uri?) {
        binding.pdfView
            .fromUri(uri)
            .swipeHorizontal(true)
            .pageSnap(true)
            .pageFling(true)
            .onTap(OnTapListener { false })
            .onLongPress(OnLongPressListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Long press: ${e.x}, ${e.y}",
                    Toast.LENGTH_SHORT
                ).show()
            })
            .onPageChange(OnPageChangeListener { page, pageCount ->
                Toast.makeText(
                    requireContext(),
                    "Page changed: $page / $pageCount",
                    Toast.LENGTH_SHORT
                ).show()
            })
            .load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PICK_PDF_REQUEST = 1
    }
}
