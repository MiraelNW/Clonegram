package com.example.clonegram.domain.usecases.user

import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class ChangeBioUseCase @Inject constructor(val repository: UserRepository) {
    operator fun invoke(bio: String, function: () -> Unit) =
        repository.changeBio(bio, function)
}