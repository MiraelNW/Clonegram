package com.example.clonegram.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.InsertImageUseCase
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val insertImage : InsertImageUseCase
) : ViewModel() {
}