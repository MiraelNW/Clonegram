package com.example.clonegram.domain.usecases.user

import android.net.Uri
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(
        uri: Uri,
        messageKey: String,
        receivedId: String,
        typeMessage: String,
        filename: String = ""
    ) = repository.uploadFile(uri, messageKey, receivedId, typeMessage, filename)
}