package com.example.clonegram.di

import com.example.clonegram.data.repositoryImpl.ContactRepositoryImpl
import com.example.clonegram.domain.repository.ContactRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindRepository(impl: ContactRepositoryImpl):ContactRepository
}