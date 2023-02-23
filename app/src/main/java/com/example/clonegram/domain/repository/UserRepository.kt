package com.example.clonegram.domain.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.clonegram.domain.models.UserInfo

interface UserRepository {

    fun getUser(uid: String): LiveData<UserInfo>

    suspend fun insertUser(user: UserInfo)

    fun insertImage(uri: Uri): LiveData<String>

    fun changeNameId(userId: String, function: () -> Unit)

    fun changeName(name: String, function: () -> Unit)

    fun changeBio(bio: String, function: () -> Unit)

    fun initUser(function: () -> Unit)

    fun initReceiver(userId: String, function: (UserInfo) -> Unit)

    fun uploadFile(
        uri: Uri,
        messageKey: String,
        receivedId: String,
        typeMessage: String,
        filename: String = ""
    )

    fun updateChildren(commonMap: HashMap<String, Any>, function: () -> Unit)

    fun firstInitUser(
        phoneNumber: String,
        uid: String,
        dateMap: Map<String, Any>,
        function: () -> Unit
    )
}