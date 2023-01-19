package com.example.clonegram.presentation.contacts

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.GetContactListUseCase
import com.example.clonegram.domain.usecases.InsertContactUseCase
import javax.inject.Inject

class ContactViewModel @Inject constructor(
val contactList : GetContactListUseCase,
val insertContact: InsertContactUseCase
) : ViewModel() {


}