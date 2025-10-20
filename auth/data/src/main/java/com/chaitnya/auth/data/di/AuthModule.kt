package com.chaitnya.auth.data.di

import com.chaitnya.auth.data.repository.AuthRepoImpl
import com.chaitnya.auth.domain.repoistory.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthRepositoryInstance(auth: FirebaseAuth): AuthRepository{
        return AuthRepoImpl(auth)
    }

}