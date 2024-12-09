package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupUserComment(
    val gucid: String = "",
    val groupUserId: String = "",
    var comment: String = "",
    val pageNumber: String = "",
    val pagePosition: String = "",
    val timeStamp: String = ""
) : Parcelable {
    override fun toString(): String {
        return "GroupUserComment(gucid='$gucid', groupUserId=$groupUserId, comment=$comment, pageNumber=$pageNumber, pagePosition=$pagePosition, timeStamp=$timeStamp)"
    }
}
