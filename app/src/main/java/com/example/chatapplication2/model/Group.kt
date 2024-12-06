package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val gId: String,
    var groupName: String?,
    val bookId: String?,
    var privacyMode: String?,
    var adminUserId: String?
) : Parcelable {
    override fun toString(): String {
        return "Group(gId='$gId', groupName=$groupName, bookId=$bookId, privacyMode=$privacyMode, adminUserId=$adminUserId)"
    }
}
