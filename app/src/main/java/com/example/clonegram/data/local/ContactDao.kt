package com.example.clonegram.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clonegram.data.local.models.UserInfoDbModel

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contactDbModel: UserInfoDbModel)

    @Query("SELECT * FROM Contacts")
    fun getContactList(): LiveData<List<UserInfoDbModel>>

}