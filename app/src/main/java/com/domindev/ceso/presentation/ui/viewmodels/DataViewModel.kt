package com.domindev.ceso.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domindev.ceso.presentation.ui.events.Events
import com.domindev.ceso.presentation.state.State
import com.domindev.ceso.data.Notes
import com.domindev.ceso.data.NotesDao
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
                val title = state.value.title
                val desc = state.value.description
                if (title.isBlank() || desc.isBlank()) {
                    return
                }
                val data = Notes(
                    title = title,
                    description = desc
                )
                viewModelScope.launch {
                    dao.upsert(data)
                }
                _state.update { it.copy(
                    title = "",
                    description = ""
                ) }
            }
        }
    }
}