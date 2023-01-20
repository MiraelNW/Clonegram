package com.example.clonegram.di

import androidx.lifecycle.ViewModel
import com.example.clonegram.presentation.contacts.ContactViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(ContactViewModel::class)
    fun bindContactViewModel(impl:ContactViewModel):ViewModel
}