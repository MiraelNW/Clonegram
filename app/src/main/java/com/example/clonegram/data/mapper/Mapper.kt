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
        phone = contact.phone,
        photoUrl = contact.photoUrl
    )

    fun mapContactDbModelToContact(
        contactDbModel: ContactDbModel
    ) = Contact(
        id = contactDbModel.id,
        name = contactDbModel.name,
        phone = contactDbModel.phone,
        photoUrl = contactDbModel.photoUrl
    )

    fun mapUserInfoToUserDbModel(
        userInfo: UserInfo
    ) = UserDbModel(
        id = userInfo.id,
        name = userInfo.name,
        bio = userInfo.bio,
        state = userInfo.state,
        photoUrl = userInfo.photoUrl,
        phone = userInfo.phone
    )

    fun mapUserDbModelToUserInfo(
        userDbModel: UserDbModel
    ) = UserInfo(
        id = userDbModel.id,
        name = userDbModel.name,
        bio = userDbModel.bio,
        state = userDbModel.state,
        photoUrl = userDbModel.photoUrl,
        phone = userDbModel.phone
    )
}