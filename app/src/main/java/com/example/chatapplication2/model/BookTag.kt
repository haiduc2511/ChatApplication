package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookTag(
    val bTId: String,
    val tagId: String?,
    val bookId: String?
) : Parcelable {
    override fun toString(): String {
        return "BookTag(bTId='$bTId', tagId=$tagId, bookId=$bookId)"
    }
}
