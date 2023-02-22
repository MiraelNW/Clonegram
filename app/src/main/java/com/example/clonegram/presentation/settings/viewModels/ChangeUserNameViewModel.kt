package com.example.clonegram.presentation.settings.viewModels

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.user.ChangeUserNameUseCase
import javax.inject.Inject

class ChangeUserNameViewModel @Inject constructor(
    val changeUserNameUseCase: ChangeUserNameUseCase
) : ViewModel() {

}