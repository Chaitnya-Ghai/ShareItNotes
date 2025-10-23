package com.chaitnya.shareitnotes.di

import com.chaitnya.notes.domain.repository.NotesRepository
import com.chaitnya.notes.domain.useCases.CreateNoteUseCase
import com.chaitnya.notes.domain.useCases.DeleteNoteUseCase
import com.chaitnya.notes.domain.useCases.GetAllNoteUseCase
import com.chaitnya.notes.domain.useCases.GetNoteByIdUseCase
import com.chaitnya.notes.domain.useCases.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotesDomainModule {
    @Provides
    fun provideGetAllNoteUseCase(notesRepository: NotesRepository): GetAllNoteUseCase {
        return GetAllNoteUseCase(notesRepository)
    }

    @Provides
    fun provideDeleteNoteUseCase(notesRepo: NotesRepository) : DeleteNoteUseCase{
        return DeleteNoteUseCase(notesRepo)
    }
    @Provides
    fun provideCreateNoteUseCase(notesRepo: NotesRepository) : CreateNoteUseCase {
        return CreateNoteUseCase(notesRepo)
    }
    @Provides
    fun provideUpdateNoteUseCase(notesRepo: NotesRepository): UpdateNoteUseCase{
        return UpdateNoteUseCase(notesRepo)
    }
    @Provides
    fun provideGetNoteByIdUseCase(notesRepo: NotesRepository): GetNoteByIdUseCase{
        return GetNoteByIdUseCase(notesRepo)
    }
}