package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPost(
    val upid: String = "",
    val groupId: String = "",
    val userId: String = "",
    val imagePost: String = "",
    val captionPost: String = ""
) : Parcelable {
    override fun toString(): String {
        return "UserPost(upid='$upid', groupId=$groupId," +
                " userId=$userId, imagePost=$imagePost, captionPost=$captionPost)"
    }
}
