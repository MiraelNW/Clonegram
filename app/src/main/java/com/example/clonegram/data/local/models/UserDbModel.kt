package com.example.clonegram.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDbModel(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val bio: String = "",
    val state: String = "",
    val photoUrl: String = "",
    val phone: String = ""
)