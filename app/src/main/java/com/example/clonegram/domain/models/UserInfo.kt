package com.example.clonegram.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class UserInfo(
    var id: String = "",
    var idName: String = "",
    var name: String = "Your name",
    var bio: String = "",
    var state: String = "",
    var photoUrl: String = "https://firebasestorage.googleapis.com/v0/b/clom-39fb6.appspot.com/o/profile_image%2Fuser.png?alt=media&token=eac02847-5586-4bc0-bcea-1cc205400832",
    var fileUrl: String = "empty",
    var phone: String = "",

    var text: String = "",
    var type: String = "",
    var from: String = "",
    var timeStamp: @RawValue Any = "",


    var lastMessage: String = ""
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return (other as UserInfo).id == id
    }
}