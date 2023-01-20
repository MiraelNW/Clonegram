package com.example.clonegram.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Contacts")
data class ContactDbModel(
    @PrimaryKey
    val id: String,
    val name: String,
    val number: String
)