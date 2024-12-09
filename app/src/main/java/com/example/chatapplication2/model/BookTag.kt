package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookTag(
    val btid: String = "",
    val tagId: String = "",
    val bookId: String = ""
) : Parcelable {
    override fun toString(): String {
        return "BookTag(btid='$btid', tagId=$tagId, bookId=$bookId)"
    }
}
