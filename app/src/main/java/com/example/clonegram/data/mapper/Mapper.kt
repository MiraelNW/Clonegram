package com.example.clonegram.data.mapper

import com.example.clonegram.data.local.models.ContactDbModel
import com.example.clonegram.data.local.models.UserDbModel
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.domain.models.UserInfo
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun mapContactToContactDbModel(
        contact: Contact
    ) = ContactDbModel(
        id = contact.id,
        name = contact.name,
        number = contact.number
    )

    fun mapContactDbModelToContact(
        contactDbModel: ContactDbModel
    ) = Contact(
        id = contactDbModel.id,
        name = contactDbModel.name,
        number = contactDbModel.number
    )
    fun mapUserInfoToUserDbModel(
        userInfo: UserInfo
    ) = UserDbModel(
        uid = userInfo.id,
        name = userInfo.name,
        bio = userInfo.bio,
        isOnline = userInfo.isOnline,
        photoUrl = userInfo.photoUrl,
        phone = userInfo.phone
    )
    fun mapUserDbModelToUserInfo(
        userDbModel: UserDbModel
    ) = UserInfo(
        id = userDbModel.uid,
        name = userDbModel.name,
        bio = userDbModel.bio,
        isOnline = userDbModel.isOnline,
        photoUrl = userDbModel.photoUrl,
        phone = userDbModel.phone
    )
}