package com.example.chatapplication2.activityreal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
        val uri: Uri = Uri.parse(book!!.fileBookLink)
        openPdf(uri)

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
                // Use the uri to open the PDF
                openPdf(uri)
            }
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