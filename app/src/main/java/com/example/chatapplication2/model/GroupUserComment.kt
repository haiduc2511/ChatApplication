package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupUserComment(
    val gUCId: String,
    val groupUserId: String?,
    var comment: String?,
    val pageNumber: Int?,
    val pagePosition: String?,
    val timeStamp: String?
) : Parcelable {
    override fun toString(): String {
        return "GroupUserComment(gUCId='$gUCId', groupUserId=$groupUserId, comment=$comment, pageNumber=$pageNumber, pagePosition=$pagePosition, timeStamp=$timeStamp)"
    }
}
