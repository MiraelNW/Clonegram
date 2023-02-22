package com.example.clonegram.domain.usecases.contacts

import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.domain.repository.ContactRepository
import javax.inject.Inject

class InsertContactListUseCase @Inject constructor (val repository: ContactRepository){

    operator fun invoke(contacts : ArrayList<UserInfo>) = repository.insertContactList(contacts)
}