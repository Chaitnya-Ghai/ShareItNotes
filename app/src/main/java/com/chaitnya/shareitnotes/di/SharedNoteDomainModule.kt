package com.chaitnya.shareitnotes.di

import com.chaitnya.sharedNotes.domain.repository.SharedNotesRepository
import com.chaitnya.sharedNotes.domain.useCases.GetPaginatedSharedNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedNoteDomainModule {

    @Provides
    fun provideGetSharedNotesUseCase( sharedNotesRepository: SharedNotesRepository ) : GetPaginatedSharedNoteUseCase {
        return GetPaginatedSharedNoteUseCase(sharedNotesRepository)
    }

}