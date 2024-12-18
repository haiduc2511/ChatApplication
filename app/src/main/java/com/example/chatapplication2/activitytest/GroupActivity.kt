package com.example.chatapplication2.activitytest

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapplication2.R

import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.chatapplication2.databinding.ActivityGroupBinding
import com.example.chatapplication2.model.Group
import com.example.chatapplication2.viewmodel.GroupViewModel

class GroupActivity : AppCompatActivity() {

    private val groupViewModel: GroupViewModel by viewModels()
    private lateinit var binding: ActivityGroupBinding
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe groups LiveData
        groupViewModel.groupsLiveData.observe(this, Observer { groups ->
            // Handle displaying groups in the UI (e.g., in a RecyclerView)
            // Example:
            // groupAdapter.submitList(groups)
        })

        // Observe error messages
        groupViewModel.errorLiveData.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Add Group button click listener
        binding.btnAddGroup.setOnClickListener {
            val newGroup = Group(
                gid = "", // ID will be generated by Firestore
                groupName = binding.etGroupName.text.toString(),
                bookId = binding.etBookId.text.toString(),
                privacyMode = binding.etPrivacyMode.text.toString(),
                groupPhotoLink = binding.etGroupPhotoLink.text.toString(),
                adminUserId = binding.etAdminUserId.text.toString()
            )
            groupViewModel.addGroup(newGroup)
        }

        // Get Groups button click listener
        binding.btnGetGroups.setOnClickListener {
            groupViewModel.getGroups()
        }

        // Update Group button click listener
        binding.btnUpdateGroup.setOnClickListener {
            val groupId = binding.etGroupId.text.toString()
            val updatedGroup = Group(
                gid = groupId,
                groupName = binding.etGroupName.text.toString(),
                bookId = binding.etBookId.text.toString(),
                privacyMode = binding.etPrivacyMode.text.toString(),
                groupPhotoLink = binding.etGroupPhotoLink.text.toString(),
                adminUserId = binding.etAdminUserId.text.toString()
            )
            groupViewModel.updateGroup(groupId, updatedGroup)
        }

        // Delete Group button click listener
        binding.btnDeleteGroup.setOnClickListener {
            val groupId = binding.etGroupId.text.toString()
            groupViewModel.deleteGroup(groupId)
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
        groupViewModel.uploadGroupPhotoCloudinary(uri, object : UploadCallback {
            override fun onStart(requestId: String) {
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                val imageUrl = (resultData["secure_url"] as String?)!!
                binding.etGroupPhotoLink.setText(imageUrl)
                Glide.with(this@GroupActivity).asBitmap().load(imageUrl).into(binding.imageView4)
                Log.d(ContentValues.TAG, "Upload successful. Image URL: $imageUrl")
            }

            override fun onError(requestId: String, error: ErrorInfo) {
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {}
        })
    }

}
