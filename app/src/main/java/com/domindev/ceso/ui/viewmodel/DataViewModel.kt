package com.domindev.ceso.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domindev.ceso.ui.event.Events
import com.domindev.ceso.ui.state.State
import com.domindev.ceso.data.Notes
import com.domindev.ceso.data.NotesDao
import com.domindev.ceso.core.util.exportNote
import com.domindev.ceso.core.util.exportNotes
import com.domindev.ceso.core.util.readZipFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataViewModel(
    private val dao: NotesDao
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _notes = dao.getNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private val _state = MutableStateFlow(State())
    val state = combine(_state,_notes) { state, notes ->
        state.copy(notes = notes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), State())

    val notes = searchText
        .combine(_notes) { text, notes ->
            if (text.isBlank()) {
                notes
            } else {
                notes.filter {
                    it.doesMatchSearchingQuery(text)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L),_notes.value)

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

            Events.ToggleFocus -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        isFocus = !state.value.isFocus
                    ) }
                }
            }

            is Events.SetSearchText -> {
                viewModelScope.launch {
                    _searchText.value = event.text
                }
            }

            is Events.SetSearch -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        isSearching = event.state
                    ) }
                }
            }

            is Events.ShareNote -> {
                viewModelScope.launch {
                    exportNote(
                        context = event.context,
                        title = _state.value.title,
                        description = _state.value.description
                    )
                }
            }

            is Events.ExportNotes -> {
                viewModelScope.launch {
                    exportNotes(
                        context = event.context,
                        notes = state.value.notes
                    )
                }
            }

            is Events.ImportNotes -> {
                viewModelScope.launch {
                    try {
                        val contents = withContext(Dispatchers.IO) {
                            readZipFile(event.context.contentResolver, event.uri)
                        }
                        contents.forEach { note ->
                            val data = Notes(
                                title = note.title,
                                description = note.description
                            )
                            dao.upsert(data)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(event.context,"Error reading ZIP file ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}