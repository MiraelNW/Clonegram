package com.example.clonegram.domain.repository

import androidx.lifecycle.LiveData
import com.example.clonegram.data.local.models.ContactDbModel
import com.example.clonegram.domain.models.Contact

interface ContactRepository {

    fun getContactList(): LiveData<List<Contact>>

    suspend fun insertContact(contact: Contact)
}