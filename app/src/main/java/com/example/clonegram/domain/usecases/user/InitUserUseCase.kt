package com.example.clonegram.domain.usecases.user

import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class InitUserUseCase @Inject constructor(val repository: UserRepository) {
    operator fun invoke(function: () -> Unit) =
        repository.initUser(function)
}