package com.example.clonegram.domain.usecases

import android.net.Uri
import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class InsertImageUseCase @Inject constructor(val repository: UserRepository) {

    operator fun invoke(uri: Uri,function: () -> Unit) = repository.insertImage(uri,function)

}