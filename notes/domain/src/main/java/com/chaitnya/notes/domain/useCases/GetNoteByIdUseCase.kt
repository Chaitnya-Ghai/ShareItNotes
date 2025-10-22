package com.chaitnya.notes.domain.useCases

import com.chaitnya.notes.domain.repository.NotesRepository

class GetNoteByIdUseCase(private val repo: NotesRepository) {
    suspend operator fun invoke( id: String) = repo.getNote(id)
}