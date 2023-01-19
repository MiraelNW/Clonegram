package com.example.clonegram.di

import android.app.Application
import com.example.clonegram.ClonegramApp
import com.example.clonegram.presentation.ChatsFragment
import com.example.clonegram.presentation.MainActivity
import com.example.clonegram.presentation.SettingsFragment
import com.example.clonegram.presentation.contacts.ContactsFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DomainModule::class,DataModule::class,ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: ChatsFragment)
    fun inject(fragment: ContactsFragment)
    fun inject(app: ClonegramApp)

    @Component.Factory
    interface Factory {
        fun create( @BindsInstance application: Application):ApplicationComponent
    }
}