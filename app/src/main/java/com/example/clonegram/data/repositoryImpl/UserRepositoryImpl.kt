package com.example.clonegram.data.repositoryImpl

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.clonegram.data.local.UserDao
import com.example.clonegram.data.mapper.Mapper
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.domain.repository.UserRepository
import com.example.clonegram.utils.*
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val mapper: Mapper,
    private val userDao: UserDao
) : UserRepository {

    override fun getUser(uid: String): LiveData<UserInfo> {
        return Transformations.map(userDao.getUser(uid)) {
            mapper.mapUserDbModelToUserInfo(it)

        }
    }

    override suspend fun insertUser(userInfo: UserInfo) {
        userDao.insertUser(
            mapper.mapUserInfoToUserDbModel(userInfo)
        )
    }

    override fun insertImage(uri: Uri, function: (String) -> Unit){
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
            .child(UID)
        putFileToStorage(uri, path) {
            getUrlFromStorage(path) {
                 putUrlToDatabase(it){

                 }
            }
        }
    }
}