package com.example.clonegram.di

import android.app.Application
import com.example.clonegram.data.local.AppDatabase
import com.example.clonegram.data.local.ContactDao
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {
        @Provides
        fun provideContactDao(application: Application): ContactDao {
            return AppDatabase.getInstance(application).contactDao()
        }
    }

}