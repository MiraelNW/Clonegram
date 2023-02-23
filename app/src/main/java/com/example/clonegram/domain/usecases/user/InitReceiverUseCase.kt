package com.example.clonegram.domain.usecases.user

import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class InitReceiverUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(
       userId:String, function: (UserInfo) -> Unit
    ) = repository.initReceiver(userId,function)
}