package com.example.clonegram.domain.usecases

import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(private val  repository : UserRepository){

    suspend operator fun invoke(
        userInfo: UserInfo
    ) = repository.insertUser(userInfo)
}