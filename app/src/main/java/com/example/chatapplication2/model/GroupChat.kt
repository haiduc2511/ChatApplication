package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupChat(
    val gcId: String,
    var theme: String?
) : Parcelable {
    override fun toString(): String {
        return "GroupChat(gcId='$gcId', theme=$theme)"
    }
}
