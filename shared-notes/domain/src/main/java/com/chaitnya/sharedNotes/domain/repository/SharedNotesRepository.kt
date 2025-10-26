package com.chaitnya.sharedNotes.domain.repository

import com.chaitnya.sharedNotes.domain.model.SharedNote

interface SharedNotesRepository {

    suspend fun getSharedNotes(): List<SharedNote>

}