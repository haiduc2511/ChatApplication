package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupUserComment(
    val gucid: String = "",
    val userId: String = "",
    val groupId: String = "",
    var comment: String = "",
    val pageNumber: String = "",
    val pagePositionX: String = "",
    val pagePositionY: String = "",
    val timeStamp: String = ""
) : Parcelable {
    override fun toString(): String {
        return "GroupUserComment(gucid='$gucid', groupId=$groupId userId=$userId, comment=$comment, pageNumber=$pageNumber, pagePositionX=$pagePositionX, pagePositionY=$pagePositionY timeStamp=$timeStamp)"
    }
}
