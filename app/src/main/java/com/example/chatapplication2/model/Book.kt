package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val bid: String = "",
    var bookTitle: String = "",
    val userUploadId: String = "",
    var bookSummary: String = "",
    val fileBookLink: String = "",
    val bookPhotoLink: String = "",
    var authorName: String = ""
) : Parcelable {
    override fun toString(): String {
        return "Book(bid='$bid', bookTitle=$bookTitle, userUploadId=$userUploadId, bookSummary=$bookSummary, fileBookLink=$fileBookLink, bookPhotoLink=$bookPhotoLink authorName=$authorName)"
    }
}
