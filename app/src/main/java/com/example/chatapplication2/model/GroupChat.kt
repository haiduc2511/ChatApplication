package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupChat(
    val gcid: String = "",
    var theme: String = ""
    //TODO: need a groupId in the groupChat maofac
) : Parcelable {
    override fun toString(): String {
        return "GroupChat(gcid='$gcid', theme=$theme)"
    }
}
