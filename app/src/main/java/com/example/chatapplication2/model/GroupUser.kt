package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupUser(
    val gUId: String,
    val groupId: String?,
    val userId: String?
) : Parcelable {
    override fun toString(): String {
        return "GroupUser(gUId='$gUId', groupId=$groupId, userId=$userId)"
    }
}
