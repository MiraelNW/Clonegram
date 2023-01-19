package com.example.clonegram.domain.usecases

import androidx.core.content.contentValuesOf
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.domain.repository.ContactRepository
import javax.inject.Inject

class InsertContactUseCase @Inject constructor(private val  repository :  ContactRepository){

    suspend operator fun invoke(
        contact: Contact
    ) = repository.insertContact(contact)
}