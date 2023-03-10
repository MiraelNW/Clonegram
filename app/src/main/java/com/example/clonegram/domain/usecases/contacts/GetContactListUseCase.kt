package com.example.clonegram.domain.usecases.contacts

import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.domain.repository.ContactRepository
import javax.inject.Inject

class GetContactListUseCase @Inject constructor(private val repository: ContactRepository) {

    operator fun invoke() =
        repository.getContactList()
}