package com.chaitnya.notes.domain.useCases

import com.chaitnya.notes.domain.model.Note
import com.chaitnya.notes.domain.repository.NotesRepository

class UpdateNoteUseCase (private val repo: NotesRepository){
    operator fun invoke(newImageBytes : ByteArray?, note: Note) = repo.updateNotes(newImageBytes ,note)
}