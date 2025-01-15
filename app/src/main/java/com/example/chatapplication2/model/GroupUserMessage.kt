package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupUserMessage(
    val gumid: String = "",
    val groupChatId: String = "",
    val groupUserId: String = "",
    var message: String = "",
    val timeStamp: String = ""
    //TODO: groupChatId cho tam la groupId
) : Parcelable {
    override fun toString(): String {
        return "GroupUserMessage(gumid='$gumid', groupChatId=$groupChatId, groupUserId=$groupUserId, message=$message, timeStamp=$timeStamp)"
    }
}
