package com.chaitnya.notes.data.repository

import com.chaitnya.notes.domain.model.Note
import com.chaitnya.notes.domain.repository.NotesRepository
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.pow

class NotesRepoImpl(
    private val db: FirebaseFirestore,
    private val supabase: SupabaseClient
) : NotesRepository {

    companion object {
        private const val NOTES = "notes"
        private const val SUPABASE_URL = "https://klumnmagqsrzvahkvnyq.supabase.co"
        private const val DELETE_RETRY_COUNT = 2
    }

    private val notesCollection by lazy { db.collection(NOTES) }

    override suspend fun uploadImageToSupabase(
        byteArray: ByteArray
    ): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        try {
            val fileName = "images/${System.currentTimeMillis()}.jpg"
            val result = supabase.storage.from(NOTES).upload(fileName, byteArray)
            val publicUrl = "${SUPABASE_URL}/storage/v1/object/public/$NOTES/$fileName"
            Result.success(result.path to publicUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteImage(imgPath: String): Result<Boolean> {
        if (imgPath.isEmpty()) return Result.success(false)

        val baseDelay = 500L // start with 500ms
        repeat(DELETE_RETRY_COUNT) { attempt ->
            try {
                supabase.storage.from(NOTES).delete(imgPath)
                return Result.success(true)
            } catch (e: Exception) {
                println("Attempt ${attempt + 1} failed to delete image: $imgPath, reason: ${e.message}")
                if (attempt == DELETE_RETRY_COUNT - 1) {
                    return Result.failure(e)
                }
                // Exponential backoff delay
                val delayTime = baseDelay * (2.0.pow(attempt.toDouble())).toLong()
                delay(delayTime)
            }
        }
        return Result.failure(Exception("Failed to delete image after $DELETE_RETRY_COUNT attempts"))
    }

    override fun createNotes(imgData: ByteArray, note: Note): Flow<Result<Unit>> = flow {
        val uploadResult = uploadImageToSupabase(imgData)
        if (uploadResult.isFailure) {
            emit(Result.failure(uploadResult.exceptionOrNull()!!))
            return@flow
        }

        val uploaded = uploadResult.getOrNull()
        if (uploaded == null) {
            emit(Result.failure(Exception("Image upload failed")))
            return@flow
        }

        val (imgPath, imgUrl) = uploaded
        val docRef = notesCollection.document()
        val data = note.copy(id = docRef.id, imgPath = imgPath, imgUrl = imgUrl)

        try {
            docRef.set(data).await()
            emit(Result.success(Unit))
        } catch (e: Exception) {
            deleteImage(imgPath)
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateNotes(newImageBytes: ByteArray?, note: Note): Flow<Result<Unit>> = flow {
        val (imgPath, imgUrl) = if (newImageBytes != null) {
            val uploadResult = uploadImageToSupabase(newImageBytes)
            if (uploadResult.isFailure) {
                emit(Result.failure(uploadResult.exceptionOrNull()!!))
                return@flow
            }

            val uploaded = uploadResult.getOrNull()
            if (uploaded == null) {
                emit(Result.failure(Exception("Image upload failed")))
                return@flow
            }

            val (newPath, newUrl) = uploaded

            // Delete old image asynchronously with retry
            coroutineScope {
                launch(Dispatchers.IO) {
                    val res = deleteImage(note.imgPath)
                    if (res.isFailure) println("Failed to delete old image: ${note.imgPath}")
                }
            }


            newPath to newUrl
        } else {
            note.imgPath to note.imgUrl
        }

        try {
            notesCollection.document(note.id).update(
                mapOf(
                    "title" to note.title,
                    "content" to note.content,
                    "shared" to note.shared,
                    "imgPath" to imgPath,
                    "imgUrl" to imgUrl,
                    "email" to note.email // Ensuring email is always present
                )
            ).await()
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteNotes(noteId: String): Flow<Result<Unit>> = flow {
        try {
            val querySnapshot = notesCollection
                .whereEqualTo("id", noteId)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                emit(Result.failure(Exception("Note not found")))
                return@flow
            }

            val document = querySnapshot.documents[0]
            val imgPath = document.getString("imgPath")

            // Delete image with retry
            imgPath?.let { path ->
                val res = deleteImage(path)
                if (res.isFailure) println("Failed to delete image for note $noteId")
            }

            document.reference.delete().await()
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getNotes(email: String): Flow<List<Note>> = callbackFlow {
        val listener = notesCollection.whereEqualTo("email", email)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val notes = value?.toObjects(Note::class.java) ?: emptyList()
                trySend(notes).isSuccess
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getNote(id: String): Result<Note> {
        return try {
            val noteSnapshot = db.collection("notes").document(id).get().await()
            val note = noteSnapshot.toObject(Note::class.java)
            note?.let { Result.success(it) } ?: Result.failure(Exception("Note not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

