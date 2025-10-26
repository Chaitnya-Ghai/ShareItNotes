package com.chaitnya.sharedNotes.domain.useCases

import com.chaitnya.sharedNotes.domain.repository.SharedNotesRepository

class GetPaginatedSharedNoteUseCase(private val repo: SharedNotesRepository) {
    suspend operator  fun invoke() = repo.getSharedNotes()
}