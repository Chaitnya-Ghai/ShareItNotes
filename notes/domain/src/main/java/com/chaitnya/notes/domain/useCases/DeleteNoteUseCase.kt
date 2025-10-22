package com.chaitnya.notes.domain.useCases

import com.chaitnya.notes.domain.repository.NotesRepository

class DeleteNoteUseCase(private val repo: NotesRepository) {
    operator fun invoke( noteId: String) = repo.deleteNotes(noteId)
}