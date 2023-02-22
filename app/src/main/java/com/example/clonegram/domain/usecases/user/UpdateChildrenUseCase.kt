package com.example.clonegram.domain.usecases.user

import com.example.clonegram.domain.repository.UserRepository
import javax.inject.Inject

class UpdateChildrenUseCase @Inject constructor(val repository: UserRepository) {

    operator fun invoke(commonMap: HashMap<String, Any>, function: () -> Unit) =
        repository.updateChildren(commonMap, function)

}
