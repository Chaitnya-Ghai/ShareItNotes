package com.chaitnya.sharedNotes.data.di

import com.chaitnya.sharedNotes.data.repository.SharedNoteRepoImpl
import com.chaitnya.sharedNotes.domain.repository.SharedNotesRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object SharedNotesDataModule {

    @Provides
    fun provideSharedNotesRepository(firestore: FirebaseFirestore): SharedNotesRepository {
        return SharedNoteRepoImpl(firestore)
    }
}