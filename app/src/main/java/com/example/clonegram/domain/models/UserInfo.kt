package com.example.clonegram.domain.models

data class UserInfo(
    val id: String = "",
    val name: String = "",
    val bio: String = "",
    val isOnline: String = "",
    val photoUrl: String = "",
    val phone: String = ""
)