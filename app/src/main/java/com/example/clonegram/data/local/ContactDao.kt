package com.example.clonegram.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clonegram.data.local.models.ContactDbModel

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contactDbModel: ContactDbModel)

    @Query("SELECT * FROM Contacts")
    fun getContactList(): LiveData<List<ContactDbModel>>

}