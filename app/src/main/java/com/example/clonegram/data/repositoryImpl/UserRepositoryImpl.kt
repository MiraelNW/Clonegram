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

    override fun insertImage(uri: Uri):LiveData<String>{
        return getImageUrl(uri)
    }

    override fun changeNameId(userId: String,function:()->Unit) {
        function()
        changeId(userId)
    }

    override fun changeName(name: String, function: () -> Unit) {
       changeUserName(name,function)
    }

    override fun changeBio(bio: String, function: () -> Unit) {
        changeFirebaseBio(bio,function)
    }

    override fun initUser(function: () -> Unit) {
       initFirebaseUser(function)
    }

    override fun initReceiver(userId:String,function: (UserInfo) -> Unit) {
       initFirebaseReceiver(userId, function)
    }

    override fun uploadFile(
        uri: Uri,
        messageKey: String,
        receivedId: String,
        typeMessage: String,
        filename: String
    ) {
        uploadFileToStorage(uri, messageKey, receivedId, typeMessage, filename)
    }

    override fun updateChildren(commonMap: HashMap<String, Any>,function: () -> Unit) {
        updateFirebaseChildren(commonMap,function)
    }

    override fun firstInitUser(phoneNumber:String,uid:String,dateMap:Map<String,Any>,function: () -> Unit) {
        firstUploadUser(phoneNumber, uid, dateMap, function)

    }

}