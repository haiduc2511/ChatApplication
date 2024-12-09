package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupChat(
    val gcid: String,
    var theme: String?
) : Parcelable {
    override fun toString(): String {
        return "GroupChat(gcid='$gcid', theme=$theme)"
    }
}
