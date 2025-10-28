package com.chaitnya.sharedNotes.data.repository

import com.chaitnya.sharedNotes.domain.model.SharedNote
import com.chaitnya.sharedNotes.domain.repository.SharedNotesRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class SharedNoteRepoImpl(
    private val db : FirebaseFirestore
) : SharedNotesRepository{
    companion object{
        private const val NOTES = "notes"
    }
    private val notesCollection by lazy { db.collection(NOTES) }

    private var docSnapshot : DocumentSnapshot?=null
    private var _isLoading = false
    private var _endPagination : Boolean = false
    private var pageSize = 4
    override suspend fun getSharedNotes(): List<SharedNote> {
        if (_isLoading || _endPagination) return emptyList()
        _isLoading = true

        var query = notesCollection
            .whereEqualTo("shared", true)
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(pageSize.toLong())
        docSnapshot?.let {
            query = query.startAfter(it.get("timeStamp"), it.get("id"))
        }

        try {
            val querySnapshot = query.get().await()
            val notes = querySnapshot.toObjects(SharedNote::class.java)

            if (querySnapshot.isEmpty.not()) {
                docSnapshot = querySnapshot.documents.last()
            } else {
                _endPagination = true
            }
            return notes
        } finally {
            _isLoading = false
        }
    }
}