package com.domindev.ceso.presentation.state

import com.domindev.ceso.data.Notes

data class State(
    val notes: List<Notes> = emptyList(),
    val title: String = "",
    val description: String = "",
    val onEdit: Boolean = false,
    val selectedNote: Notes = Notes(id = -1, title = "", description = ""),
    val columnView: Boolean = false,
    val isFocus: Boolean = false
)
