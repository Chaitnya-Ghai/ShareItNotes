package com.chaitnya.notes.domain.repository

import com.chaitnya.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun uploadImageToSupabase(byteArray: ByteArray): Result<Pair<String, String>>
    suspend fun deleteImage(imgPath: String) : Result<Boolean>

    fun createNotes(imgData: ByteArray, note: Note): Flow<Result<Unit>>
    fun updateNotes(newImageBytes: ByteArray?=null, note: Note): Flow<Result<Unit>>
    fun deleteNotes(noteId: String): Flow<Result<Unit>>
    fun getNotes(email: String): Flow<List<Note>>
    suspend fun getNote(id: String): Result<Note>
    }