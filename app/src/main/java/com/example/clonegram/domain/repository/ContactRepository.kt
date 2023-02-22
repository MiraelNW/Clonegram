package com.example.clonegram.domain.repository

import androidx.lifecycle.LiveData
import com.example.clonegram.domain.models.UserInfo

interface ContactRepository {

    fun getContactList(): LiveData<List<UserInfo>>

    fun insertContactList(contacts : ArrayList<UserInfo>)
}