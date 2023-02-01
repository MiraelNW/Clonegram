package com.example.clonegram.domain.usecases

import com.example.clonegram.domain.repository.ContactRepository
import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(uid : String) = repository.getUser(uid)
}