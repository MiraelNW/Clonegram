package com.example.clonegram.presentation

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.user.InitUserUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    val initUserUseCase: InitUserUseCase
): ViewModel() {
}