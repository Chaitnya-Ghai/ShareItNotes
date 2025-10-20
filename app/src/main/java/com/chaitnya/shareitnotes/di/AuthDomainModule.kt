package com.chaitnya.shareitnotes.di

import com.chaitnya.auth.domain.repoistory.AuthRepository
import com.chaitnya.auth.domain.use_cases.GetCurrentUserUseCase
import com.chaitnya.auth.domain.use_cases.LogInUseCase
import com.chaitnya.auth.domain.use_cases.LogoutUseCase
import com.chaitnya.auth.domain.use_cases.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AuthDomainModule {
    @Provides
    fun provideGetCurrentUserUseCase(auth: AuthRepository): GetCurrentUserUseCase{
        return GetCurrentUserUseCase(auth)
    }
    @Provides
    fun provideLogInUseCase(auth: AuthRepository): LogInUseCase{
        return LogInUseCase(auth)
    }
    @Provides
    fun provideLogOutUseCase(auth: AuthRepository): LogoutUseCase{
        return LogoutUseCase(auth)
    }
    @Provides
    fun provideRegisterUserUseCase(auth: AuthRepository): RegisterUseCase{
        return RegisterUseCase(auth)
    }


}