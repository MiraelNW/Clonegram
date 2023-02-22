package com.example.clonegram.presentation.authentication

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.InsertUserUseCase
import javax.inject.Inject

class EnterCodeViewModel @Inject constructor(
    val insertUserUseCase: InsertUserUseCase
) : ViewModel() {

}