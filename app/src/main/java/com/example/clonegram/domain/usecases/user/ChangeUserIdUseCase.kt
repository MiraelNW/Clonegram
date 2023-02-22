package com.example.clonegram.domain.usecases.user

import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class ChangeUserIdUseCase @Inject constructor(val repository: UserRepository) {
    operator fun invoke(userId: String, function: () -> Unit) =
        repository.changeNameId(userId, function)
}