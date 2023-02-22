package com.example.clonegram.presentation.settings.viewModels

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.user.ChangeBioUseCase
import com.example.clonegram.domain.usecases.user.ChangeUserIdUseCase
import javax.inject.Inject

class ChangeBioViewModel @Inject constructor(
    val changeBioUseCase: ChangeBioUseCase
) : ViewModel() {

}