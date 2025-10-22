package com.chaitnya.notes.domain.useCases

import com.chaitnya.notes.domain.model.Note
import com.chaitnya.notes.domain.repository.NotesRepository

class CreateNoteUseCase(private val repo: NotesRepository) {
    operator fun invoke( byteArray: ByteArray, note: Note) = repo.createNotes(imgData = byteArray, note = note)
}