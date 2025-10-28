package com.chaitnya.sharedNotes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitnya.sharedNotes.domain.model.SharedNote
import com.chaitnya.sharedNotes.domain.useCases.GetPaginatedSharedNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedNoteViewModel @Inject constructor(
    private val useCase: GetPaginatedSharedNoteUseCase
) : ViewModel(){

    private val _sharedNotes= MutableStateFlow<List<SharedNote>>(emptyList())
    val sharedNotes = _sharedNotes.asStateFlow()

    init {
        if (_sharedNotes.value.isEmpty()) {
            getPaginatedNotes()
        }
    }

    fun getPaginatedNotes() = viewModelScope.launch {
        val sharedNotes= useCase()
        _sharedNotes.update { it + sharedNotes }

    }
}