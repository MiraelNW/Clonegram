package com.example.clonegram.di

import androidx.lifecycle.ViewModel
import com.example.clonegram.presentation.MainViewModel
import com.example.clonegram.presentation.authentication.EnterCodeViewModel
import com.example.clonegram.presentation.chats.ChatsViewModel
import com.example.clonegram.presentation.contacts.ContactViewModel
import com.example.clonegram.presentation.settings.viewModels.ChangeBioViewModel
import com.example.clonegram.presentation.settings.viewModels.ChangeUserIdViewModel
import com.example.clonegram.presentation.settings.viewModels.ChangeUserNameViewModel
import com.example.clonegram.presentation.settings.viewModels.SettingsViewModel
import com.example.clonegram.presentation.singleChat.SingleChatViewModel
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
    fun bindSettingsViewModel(impl: SettingsViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeUserIdViewModel::class)
    fun bindChangeUserIdViewModel(impl: ChangeUserIdViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeUserNameViewModel::class)
    fun bindChangeUserNameViewModel(impl: ChangeUserNameViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeBioViewModel::class)
    fun bindChangeBioViewModel(impl: ChangeBioViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(impl: MainViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SingleChatViewModel::class)
    fun bindSingleChatViewModel(impl: SingleChatViewModel):ViewModel


}