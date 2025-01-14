package com.example.chatapplication2.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
import com.example.chatapplication2.model.GroupUserComment
import com.example.chatapplication2.utils.RandomPointsView
import com.example.chatapplication2.utils.SharedPreferenceManager
import com.example.chatapplication2.viewmodel.BookViewModel
import com.example.chatapplication2.viewmodel.GroupUserCommentViewModel
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
    private val groupUserCommentViewModel: GroupUserCommentViewModel by viewModels()
    private var bookReadingId: String = ""
    private var seeingComments: Boolean = true
    private var pageNumber: Int = 0
    private lateinit var group: Group
    private lateinit var pointView: RandomPointsView
    private var commentList = listOf<GroupUserComment>()
    val colors = mapOf(
        "Red" to Color.RED,
        "Green" to Color.GREEN,
        "Blue" to Color.BLUE,
        "Yellow" to Color.YELLOW,
        "Black" to Color.BLACK
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val xCoordinate = view.findViewById<EditText>(R.id.xCoordinate)
        val yCoordinate = view.findViewById<EditText>(R.id.yCoordinate)
        val colorSpinner = view.findViewById<Spinner>(R.id.colorSpinner)
        val addPointButton = view.findViewById<Button>(R.id.addPointButton)
        pointView = view.findViewById<RandomPointsView>(R.id.randomPointsView)

        // Color options

        // Set up Spinner with color options
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, colors.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter = adapter

        addPointButton.setOnClickListener {
            val x = xCoordinate.text.toString().toFloatOrNull()
            val y = yCoordinate.text.toString().toFloatOrNull()
            val selectedColorName = colorSpinner.selectedItem as String
            val selectedColor = colors[selectedColorName] ?: Color.RED

            if (x != null && y != null && x in 0f..100f && y in 0f..100f) {
                // Convert coordinates to screen pixels
                val screenX = pointView.width * (x / 100)
                val screenY = pointView.height * (y / 100)
                Log.d("check toạ độ point2", "${pointView.width} + ${pointView.height} + $x + $y")

                pointView.addPoint(x, y, selectedColor)
            } else {
                Toast.makeText(requireContext(), "Invalid coordinates. Enter values between 0 and 100.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivSeeComments.setOnClickListener {
            if (seeingComments) {
                seeingComments = false
                binding.ivSeeComments.setImageResource(R.drawable.img_not_see)
                binding.clComments.visibility = View.GONE
            } else {
                seeingComments = true
                binding.ivSeeComments.setImageResource(R.drawable.img_see)
                binding.clComments.visibility = View.VISIBLE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadNewGroupFromMainFragment()
    }

    fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    public fun loadNewGroupFromMainFragment() {
        val groupId: String = SharedPreferenceManager(requireContext()).getString("aboutToReadGroupId")
        val bookId: String = SharedPreferenceManager(requireContext()).getString("aboutToReadBookId")

        Log.d("check book null", bookId)
        Log.d("check group null", groupId)
        if (!bookReadingId.equals(bookId)) {
            groupViewModel.getGroupById(groupId, object : OnCompleteListener<QuerySnapshot> {
                override fun onComplete(task: Task<QuerySnapshot>) {
                    if (task.isSuccessful) {
                        Log.d("groupViewModel in load again", "groupViewModel successful")
                        val groups = task.result?.toObjects(Group::class.java) ?: emptyList()
                        if (groups.isNotEmpty()) {
                            //TODO: làm thêm phần customView cho đống comment
                            group = groups[0]
                            pointView.onPointClickListener = { x, y ->
                                showFragment(GroupUserCommentFragment(group, pageNumber ,x, y))
                                Toast.makeText(requireContext(), "Point clicked: ($x, $y)", Toast.LENGTH_SHORT).show()
                            }

                            getCommentFromGroupUser()

                        }
                    }
                }
            })

            bookViewModel.getBookById(bookId, object : OnCompleteListener<QuerySnapshot> {
                override fun onComplete(task: Task<QuerySnapshot>) {
                    if (task.isSuccessful) {
                        val books = task.result?.toObjects(Book::class.java) ?: emptyList()
                        if (books.isNotEmpty()) {
                            Log.d("downloadAndOpenPdf in load again", books.toString())
                            downloadAndOpenPdf(books[0].fileBookLink)
                        }
                    }
                }
            })

        }

        bookReadingId = bookId


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
                Log.d("downloadAndOpenPdf in ReadFragment", e.message.toString())
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
                val screenX = 100 * e.x / pointView.width
                val screenY = 100 * e.y / pointView.height
                showFragment(GroupUserCommentFragment(group, pageNumber, screenX, screenY))
            })
            .onPageChange(OnPageChangeListener { page, pageCount ->
                Toast.makeText(
                    requireContext(),
                    "Page changed: ${page + 1} / $pageCount",
                    Toast.LENGTH_SHORT
                ).show()
                binding.textView.text = "${page + 1} / $pageCount"
                pageNumber = page + 1
                pointView.clearPoints()
                drawCommentDots(pageNumber)
            })
            .load()
    }

    fun getCommentFromGroupUser() {
        groupUserCommentViewModel.getGroupUserCommentsByField("groupId", group.gid)
        groupUserCommentViewModel.groupUserCommentsLiveData.observe(viewLifecycleOwner) { comments ->
            Log.d("check drawCommentDots2", group.gid + " " + comments.toString())
            commentList = comments
        }
    }

    fun drawCommentDots(pageNumber: Int) {

        Log.d("check drawCommentDots", commentList.toString())
        for (comment in commentList) {
            if (comment.pageNumber.toInt() == pageNumber) {
                val x = comment.pagePositionX.toFloatOrNull()
                val y = comment.pagePositionY.toFloatOrNull()
//                val selectedColorName = colorSpinner.selectedItem as String
//                val selectedColor = colors[selectedColorName] ?: Color.RED
                val selectedColor = Color.RED

                if (x != null && y != null && x in 0f..100f && y in 0f..100f) {
                    // Convert coordinates to screen pixels
                    val screenX = pointView.width * (x / 100)
                    val screenY = pointView.height * (y / 100)

                    pointView.addPoint(x, y, selectedColor)
                    Log.d("check toạ độ point", "$screenX + $screenX + ${commentList.toString()}")
                } else {
                    Log.d("vẽ điểm lên pointView", "$comment invalid coordinates. Enter values between 0 and 100.")
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PICK_PDF_REQUEST = 1
    }
}
