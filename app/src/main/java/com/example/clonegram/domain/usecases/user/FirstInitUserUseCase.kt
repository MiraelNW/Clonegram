package com.example.clonegram.domain.usecases.user

import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class FirstInitUserUseCase @Inject constructor(val repository: UserRepository) {
    operator fun invoke(phoneNumber:String,uid:String,dateMap:Map<String,Any>,function: () -> Unit) =
        repository.firstInitUser(phoneNumber, uid, dateMap, function)
}