package com.example.clonegram.data.repositoryImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.clonegram.data.local.ContactDao
import com.example.clonegram.data.mapper.Mapper
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.domain.repository.ContactRepository
import com.example.clonegram.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor() : ContactRepository {
    override fun getContactList(): LiveData<List<UserInfo>> {
       return  getContactListFromFirebase()
    }

    override fun insertContactList(contacts: ArrayList<UserInfo>) {
        updatePhonesList(contacts)
    }
}