package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupUserMessage(
    val gUmId: String,
    val groupChatId: String?,
    val groupUserId: String?,
    var message: String?,
    val replyMessageId: String?
) : Parcelable {
    override fun toString(): String {
        return "GroupUserMessage(gUmId='$gUmId', groupChatId=$groupChatId, groupUserId=$groupUserId, message=$message, replyMessageId=$replyMessageId)"
    }
}
