package com.example.clonegram.presentation.chats

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.InsertContactListUseCase
import javax.inject.Inject

class ChatsViewModel @Inject constructor(
    val insertContactListUseCase: InsertContactListUseCase
) :ViewModel() {
}