package com.chaitnya.notes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitnya.auth.domain.model.User
import com.chaitnya.auth.domain.use_cases.GetCurrentUserUseCase
import com.chaitnya.notes.domain.model.Note
import com.chaitnya.notes.domain.useCases.DeleteNoteUseCase
import com.chaitnya.notes.domain.useCases.GetAllNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNoteUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)

    init {
        viewModelScope.launch {
            _user.update { getCurrentUserUseCase() }
        }
        getNotes()
    }
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    fun getNotes(){
        getAllNotesUseCase(email = _user.value?.email.orEmpty())
            .onEach {
                _notes.update { it }
        }.launchIn(viewModelScope)
    }

    fun deleteNote(id:String){
        deleteNoteUseCase.invoke(id)
            .onEach { result->
                result.onSuccess {  }
                result.onFailure {  }
            }
            .launchIn(viewModelScope)
    }


}