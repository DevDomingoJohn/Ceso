package com.domindev.ceso.ui.state

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import com.domindev.ceso.data.Notes

data class State(
    val notes: List<Notes> = emptyList(),
    val title: String = "",
    val description: String = "",
    val onEdit: Boolean = false,
    val selectedNote: Notes = Notes(id = -1, title = "", description = ""),
    val columnView: Boolean = false,
    val isFocus: Boolean = false,
    val isSearching: Boolean = false,

    val drawerState: DrawerState = DrawerState(initialValue = DrawerValue.Closed),
    var selectedItem: MutableState<Int> = mutableIntStateOf(0)
)
