package com.example.clonegram.presentation.authentication

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.user.FirstInitUserUseCase
import com.example.clonegram.domain.usecases.user.InsertUserUseCase
import javax.inject.Inject

class EnterCodeViewModel @Inject constructor(
    val firstInitUserUseCase: FirstInitUserUseCase,
    val insertUserUseCase: InsertUserUseCase
) : ViewModel() {

}