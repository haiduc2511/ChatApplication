package com.example.chatapplication2.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.R
import com.example.chatapplication2.databinding.ActivityReadBinding
import com.example.chatapplication2.model.Book
import com.example.chatapplication2.model.Group
import com.github.barteksc.pdfviewer.listener.OnLongPressListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnTapListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class ReadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val group: Group? = intent.getParcelableExtra("group")
        val book: Book? = intent.getParcelableExtra("book") //
        //TODO: chỉ tạo group khi đã có book nha, cần tạo search book khi tạo group đã
        downloadAndOpenPdf(book!!.fileBookLink)

        binding.tvChooseUri.setText(group.toString())
        binding.tvChooseUri.setOnClickListener { v -> openFileChooser() }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("application/pdf") // Specify the type for PDF files
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(intent, "Select PDF"),
            PICK_PDF_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                val uri = data.data
                openPdf(uri)
            }
        }
    }
    fun downloadAndOpenPdf(pdfUrl: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val pdfFile = withContext(Dispatchers.IO) {
                    downloadPdfFromUrl(pdfUrl)
                }

                openPdf(Uri.fromFile(pdfFile))

            } catch (e: Exception) {
                Log.d("d", e.message.toString())
                Toast.makeText(this@ReadActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun downloadPdfFromUrl(url: String): File {
        // Create a temporary file to store the PDF
        val pdfFile = File.createTempFile("downloaded", ".pdf", cacheDir)

        withContext(Dispatchers.IO) {
            try {
                // Open a connection to the URL
                val connection = URL(url).openStream()
                val output = FileOutputStream(pdfFile)

                // Copy the input stream to the output file
                connection.use { input ->
                    output.use { outputStream ->
                        input.copyTo(outputStream)
                    }
                }

                // Check if the file is a valid PDF
                if (!isPdfFile(pdfFile)) {
                    throw IOException("File is not a valid PDF")
                }

            } catch (e: Exception) {
                // Handle download errors
                throw IOException("Failed to download PDF: ${e.message}")
            }
        }

        return pdfFile
    }
    fun isPdfFile(file: File): Boolean {
        return try {
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(4)
            inputStream.read(buffer)
            inputStream.close()

            // Check if the first 4 bytes match the PDF signature
            val fileHeader = String(buffer)
            fileHeader == "%PDF"
        } catch (e: Exception) {
            false
        }
    }
    private fun openPdf(uri: Uri?) {
        binding.pdfView
            .fromUri(uri) //                .pages(1,1,1,1,10)
            .swipeHorizontal(true)
            .pageSnap(true) //                .autoSpacing(true)
            .pageFling(true)
            .onTap(OnTapListener { //                        Toast.makeText(MainActivity.this, e.getX() + " " + e.getY(), Toast.LENGTH_SHORT).show();
                false
            })
            .onLongPress(OnLongPressListener { e ->
                Toast.makeText(
                    this@ReadActivity,
                    "longPress" + e.x + " " + e.y,
                    Toast.LENGTH_SHORT
                ).show()
            })
            .onPageChange(OnPageChangeListener { page, pageCount ->
                Toast.makeText(
                    this@ReadActivity,
                    "pageChanged$page $pageCount",
                    Toast.LENGTH_SHORT
                ).show()
            })
            .load()
    }

    companion object {
        private const val PICK_PDF_REQUEST = 1
    }
}