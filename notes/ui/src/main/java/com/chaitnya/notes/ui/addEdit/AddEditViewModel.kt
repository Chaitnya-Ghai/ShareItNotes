package com.chaitnya.notes.ui.addEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitnya.auth.domain.model.User
import com.chaitnya.auth.domain.use_cases.GetCurrentUserUseCase
import com.chaitnya.notes.domain.model.Note
import com.chaitnya.notes.domain.useCases.CreateNoteUseCase
import com.chaitnya.notes.domain.useCases.GetNoteByIdUseCase
import com.chaitnya.notes.domain.useCases.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isError = MutableSharedFlow<String?>()
    val isError :SharedFlow<String?> = _isError

    private val _isSaved = MutableSharedFlow<Unit>()
    val navigateBack: SharedFlow<Unit> = _isSaved

    val editNote = MutableStateFlow<Note?>(null)

    private val _isEdit = MutableStateFlow<Boolean>(false)
    val isEdit = _isEdit.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()
    fun setTitle(title: String){
        _title.value = title
    }

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()
    fun setContent(content: String){
        _content.value = content
    }
    private val _imgPath = MutableStateFlow("")
    val imgPath = _imgPath.asStateFlow()

    private val _imgUrl = MutableStateFlow("")
    val imgUrl = _imgUrl.asStateFlow()

    private val _imgData = MutableStateFlow(ByteArray(0))
    val imgData = _imgData.asStateFlow()
    fun setUriAsByteArray(uri: ByteArray){
        _imgData.value = uri
    }

    private val _shared = MutableStateFlow(false)
    val shared = _shared.asStateFlow()
    fun setShared(shared: Boolean){
        _shared.value = shared
    }

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    init {
        viewModelScope.launch { _user.update { getCurrentUserUseCase.invoke() } }
    }
    fun saveNote() {
        if (_isEdit.value) updateNote(_imgData.value) else createNote()
    }


    fun createNote(){
        val note = Note(
            id= UUID.randomUUID().toString(),
            email = _user.value?.email.orEmpty(),
            title = _title.value,
            content = _content.value,
            imgUrl = "",
            imgPath = "",
            shared = _shared.value,
        )
        createNoteUseCase(_imgData.value, note)
            .onStart { _isLoading.update { true } }
            .retry(2) { cause ->
                cause is IOException
            }
            .onEach { result ->
                result.fold(
                    onSuccess = {
                        _isSaved.emit(Unit) // trigger navigation
                    },
                    onFailure = { e ->
                        _isError.emit(e.message ?: "Unknown error occurred")
                    }
                )
            }
            .catch { e ->
                _isError.emit(e.message ?: "Unknown error occurred")
                _isLoading.update { false }
            }
            .onCompletion {
                _isLoading.update { false }
                _imgData.update { ByteArray(0) }
            }
            .launchIn(viewModelScope)
    }

    fun loadNoteData(noteId: String) {
        viewModelScope.launch {
            try {
                val result = getNoteByIdUseCase(noteId)
                result.onSuccess { note ->
                    editNote.update { note }
                    _shared.update { note.shared }
                    _title.update { note.title }
                    _content.update { note.content }
                    _imgPath.update { note.imgPath }
                    _imgUrl.update { note.imgUrl }
                    _isEdit.update { true }
                }.onFailure { e ->
                    handleNoteLoadError(e.message)
                }
            } catch (e: Exception) {
                handleNoteLoadError(e.message)
            }
        }
    }

    private suspend fun handleNoteLoadError(message: String?) {
        _isError.emit(message ?: "Unknown error occurred")
        editNote.update { null }
        _title.update { "" }
        _content.update { "" }
        _imgPath.update { "" }
        _imgUrl.update { "" }
        _shared.update { false }
        _isEdit.update { false }
    }

    fun updateNote(newImageBytes: ByteArray?) {
        val currentNote = editNote.value ?: run {
            viewModelScope.launch { _isError.emit("No note loaded to update") }
            return
        }

        val updatedNote = currentNote.copy(
            title = _title.value,
            content = _content.value,
            shared = _shared.value
        )

        updateNoteUseCase(newImageBytes, updatedNote)
            .onStart { _isLoading.update { true } }
            .retry(2) { it is IOException }
            .onEach { result ->
                result.fold(
                    onSuccess = { _isSaved.emit(Unit) },
                    onFailure = { e -> _isError.emit(e.message ?: "Unknown error occurred") }
                )
            }
            .onCompletion {
                _isLoading.update { false }
                _isEdit.update { false }
                _imgData.update { ByteArray(0) }
            }
            .catch { e ->
                _isError.emit(e.message ?: "Unknown error occurred")
                _isLoading.update { false }
            }
            .launchIn(viewModelScope)
    }
}