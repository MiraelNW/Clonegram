package com.example.clonegram.di

import android.app.Application
import com.example.clonegram.ClonegramApp
import com.example.clonegram.presentation.chats.ChatsFragment
import com.example.clonegram.presentation.MainActivity
import com.example.clonegram.presentation.MainFragment
import com.example.clonegram.presentation.authentication.EnterCodeFragment
import com.example.clonegram.presentation.authentication.EnterPhoneNumberFragment
import com.example.clonegram.presentation.authentication.StartCommunicationFragment
import com.example.clonegram.presentation.settings.SettingsFragment
import com.example.clonegram.presentation.contacts.ContactsFragment
import com.example.clonegram.presentation.settings.ChangeBioFragment
import com.example.clonegram.presentation.settings.ChangeNameFragment
import com.example.clonegram.presentation.settings.ChangeUserIdFragment
import com.example.clonegram.presentation.singleChat.SingleChatFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DomainModule::class,DataModule::class,ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: ChatsFragment)
    fun inject(fragment: ContactsFragment)
    fun inject(fragment: ChangeBioFragment)
    fun inject(fragment: ChangeUserIdFragment)
    fun inject(fragment: StartCommunicationFragment)
    fun inject(fragment: EnterPhoneNumberFragment)
    fun inject(fragment: EnterCodeFragment)
    fun inject(fragment: ChangeNameFragment)
    fun inject(fragment: MainFragment)
    fun inject(fragment: SingleChatFragment)
    fun inject(app: ClonegramApp)

    @Component.Factory
    interface Factory {
        fun create( @BindsInstance application: Application):ApplicationComponent
    }
}