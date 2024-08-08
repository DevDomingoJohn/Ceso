package com.domindev.ceso.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domindev.ceso.presentation.ui.events.Events
import com.domindev.ceso.presentation.state.State
import com.domindev.ceso.data.Notes
import com.domindev.ceso.data.NotesDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DataViewModel(
    private val dao: NotesDao
) : ViewModel() {

    private val _notes = dao.getNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    private val _state = MutableStateFlow(State())
    val state = combine(_state,_notes) { state, notes ->
        state.copy(notes = notes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), State())

    fun onEvent(event: Events) {
        when(event) {
            is Events.SetTitle -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        title = event.title
                    ) }
                }
            }
            is Events.SetDescription -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        description = event.desc
                    ) }
                }
            }
            is Events.SaveNote -> {
                val id = state.value.selectedNote.id
                val title = state.value.title
                val desc = state.value.description
                if (title.isBlank() || desc.isBlank()) {
                    return
                }
                val data = if (id == -1) {
                    Notes(
                        title = title,
                        description = desc
                    )
                } else {
                    Notes(
                        id = id,
                        title = title,
                        description = desc
                    )
                }

                viewModelScope.launch {
                    dao.upsert(data)
                    delay(1000L)
                    _state.update { it.copy(
                        selectedNote = Notes(id = -1, title = "", description = ""),
                        title = "",
                        description = ""
                    ) }
                }
            }
            is Events.SetSelectedNote -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        title = event.note.title,
                        description = event.note.description,
                        selectedNote = event.note
                    ) }
                }
            }

            Events.DeleteNote -> {
                val selectedNote = state.value.selectedNote
                viewModelScope.launch {
                    dao.delete(selectedNote)
                    delay(1000L)
                    _state.update { it.copy(
                        selectedNote = Notes(id = -1, title = "", description = ""),
                        title = "",
                        description = ""
                    ) }
                }
            }

            Events.ToggleEdit -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        onEdit = !state.value.onEdit
                    ) }
                }
            }

            Events.ToggleView -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        columnView = !state.value.columnView
                    ) }
                }
            }
        }
    }
}