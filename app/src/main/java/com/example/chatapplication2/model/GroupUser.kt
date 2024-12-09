package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupUser(
    val guid: String = "",
    val groupId: String = "",
    val userId: String = ""
) : Parcelable {
    override fun toString(): String {
        return "GroupUser(guid='$guid', groupId=$groupId, userId=$userId)"
    }
}
