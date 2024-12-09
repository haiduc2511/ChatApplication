package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = "",
    var name: String = "",
    val email: String = "",
    val avaLink: String = "",
): Parcelable {
    override fun toString(): String {
        return "GroupUser(uid='$uid', name=$name, email=$email, avaLink=$avaLink, )"
    }
}
