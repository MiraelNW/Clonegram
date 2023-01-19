package com.example.clonegram.data.mapper

import com.example.clonegram.data.local.models.ContactDbModel
import com.example.clonegram.domain.models.Contact
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun mapContactToContactDbModel(
        contact: Contact
    ) = ContactDbModel(
        id = contact.id,
        name = contact.name,
        number = contact.number
    )

    fun mapContactDbModelToContact(
        contactDbModel: ContactDbModel
    ) = Contact(
        id = contactDbModel.id,
        name = contactDbModel.name,
        number = contactDbModel.number
    )
}