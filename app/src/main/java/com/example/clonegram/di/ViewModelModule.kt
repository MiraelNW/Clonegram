package com.example.clonegram.di

import androidx.lifecycle.ViewModel
import com.example.clonegram.presentation.authentication.EnterCodeViewModel
import com.example.clonegram.presentation.chats.ChatsViewModel
import com.example.clonegram.presentation.contacts.ContactViewModel
import com.example.clonegram.presentation.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(ContactViewModel::class)
    fun bindContactViewModel(impl:ContactViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EnterCodeViewModel::class)
    fun bindEnterCodeViewModel(impl:EnterCodeViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatsViewModel::class)
    fun bindChatsViewModel(impl:ChatsViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(impl:SettingsViewModel):ViewModel

}