package com.example.chatapplication2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uId: String = "",
    var name: String = "",
    val email: String = "",
    val avaLink: String = "",
): Parcelable {
    override fun toString(): String {
        return "GroupUser(uId='$uId', name=$name, email=$email, avaLink=$avaLink, )"
    }
}
