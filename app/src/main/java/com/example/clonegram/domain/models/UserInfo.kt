package com.example.clonegram.domain.models

data class UserInfo(
    var id: String = "",
    var name: String = "Your name",
    var bio: String = "",
    var state: String = "",
    var photoUrl: String = "https://firebasestorage.googleapis.com/v0/b/clom-39fb6.appspot.com/o/profile_image%2Fuser.png?alt=media&token=eac02847-5586-4bc0-bcea-1cc205400832",
    var phone: String = ""
)