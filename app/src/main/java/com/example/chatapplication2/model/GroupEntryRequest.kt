package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupEntryRequest(
    val gerid: String = "",
    val userId: String = "",
    val groupId: String = "",
    val requestComment: String = ""
) : Parcelable {
    override fun toString(): String {
        return "GroupEntryRequest(gerid='$gerid', userId=$userId, groupId=$groupId, requestComment=$requestComment)"
    }
}

