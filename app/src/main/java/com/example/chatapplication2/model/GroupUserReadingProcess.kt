package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupUserReadingProcess(
    val gurpid: String = "",
    val readingProcess: String = "",
    val timeStamp: String = ""
) : Parcelable {
    override fun toString(): String {
        return "GroupUserReadingProcess(gurpid='$gurpid', readingProcess=$readingProcess, timeStamp=$timeStamp)"
    }
}
