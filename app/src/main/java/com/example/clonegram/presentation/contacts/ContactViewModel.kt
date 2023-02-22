package com.example.clonegram.presentation.contacts

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.contacts.GetContactListUseCase
import javax.inject.Inject

class ContactViewModel @Inject constructor(
    val contactListUseCase: GetContactListUseCase
) : ViewModel() {

}