package com.chaitnya.notes.domain.useCases

import com.chaitnya.notes.domain.repository.NotesRepository

class GetAllNoteUseCase(private val repo: NotesRepository) {
    operator fun invoke( email: String) = repo.getNotes(email)
}