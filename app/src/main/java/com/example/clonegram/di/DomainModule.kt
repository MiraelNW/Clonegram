package com.example.clonegram.di

import com.example.clonegram.data.repositoryImpl.ContactRepositoryImpl
import com.example.clonegram.data.repositoryImpl.UserRepositoryImpl
import com.example.clonegram.domain.repository.ContactRepository
import com.example.clonegram.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindContactRepository(impl: ContactRepositoryImpl):ContactRepository

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
