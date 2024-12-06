package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
    val tId: String,
    val tagName: String?
) : Parcelable {
    override fun toString(): String {
        return "Tag(tId='$tId', tagName=$tagName)"
    }
}
