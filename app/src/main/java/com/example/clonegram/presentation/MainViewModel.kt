package com.example.clonegram.presentation

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.GetContactListUseCase
import com.example.clonegram.domain.usecases.InsertContactUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
val insertContact: InsertContactUseCase
) : ViewModel() {



}