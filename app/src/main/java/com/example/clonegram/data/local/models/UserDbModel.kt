package com.example.clonegram.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDbModel(
    @PrimaryKey
    val uid: String = "",
    val name: String = "",
    val lastName: String = "",
    val bio: String = "",
    val isOnline: String = "",
    val photoUrl: String = "",
    val phone: String = ""
)