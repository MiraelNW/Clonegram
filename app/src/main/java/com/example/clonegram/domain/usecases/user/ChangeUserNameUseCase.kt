package com.example.clonegram.domain.usecases.user

import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class ChangeUserNameUseCase @Inject constructor(val repository: UserRepository) {
    operator fun invoke(name: String, function: () -> Unit) =
        repository.changeNameId(name, function)
}