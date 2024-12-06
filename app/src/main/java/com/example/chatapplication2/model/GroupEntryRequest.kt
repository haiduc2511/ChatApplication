package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupEntryRequest(
    val gERId: String,
    val userId: String?,
    val groupId: String?,
    val requestComment: String?
) : Parcelable {
    override fun toString(): String {
        return "GroupEntryRequest(gERId='$gERId', userId=$userId, groupId=$groupId, requestComment=$requestComment)"
    }
}

