package com.example.clonegram.domain.repository

import androidx.lifecycle.LiveData
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.domain.models.UserInfo

interface UserRepository {

    fun getUser(uid : String): LiveData<UserInfo>

    suspend fun insertUser(user : UserInfo)
}