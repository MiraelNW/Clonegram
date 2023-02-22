package com.example.clonegram.presentation.settings.viewModels

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.user.InsertImageUseCase
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val insertImage : InsertImageUseCase
) : ViewModel() {
}