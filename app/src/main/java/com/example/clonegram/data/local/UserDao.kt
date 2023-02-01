package com.example.clonegram.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clonegram.data.local.models.ContactDbModel
import com.example.clonegram.data.local.models.UserDbModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userDbModel: UserDbModel)

    @Query("SELECT * FROM user WHERE uid=:uid")
    fun getUser(uid : String): LiveData<UserDbModel>
}