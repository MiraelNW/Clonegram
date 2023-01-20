package com.example.clonegram.data.repositoryImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.clonegram.data.local.ContactDao
import com.example.clonegram.data.mapper.Mapper
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.domain.repository.ContactRepository
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val mapper: Mapper,
    private val contactDao: ContactDao
) : ContactRepository {
    override fun getContactList(): LiveData<List<Contact>> {
        return Transformations.map(contactDao.getContactList()) {
            it.map {
                mapper.mapContactDbModelToContact(it)
            }
        }
    }

    override suspend fun insertContact(contact: Contact) {
        contactDao.insertContact(
            mapper.mapContactToContactDbModel(contact)
        )
    }
}