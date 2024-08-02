package com.domindev.ceso.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.domindev.ceso.presentation.ui.events.Events
import com.domindev.ceso.presentation.state.State
import com.domindev.ceso.data.Notes
import com.domindev.ceso.presentation.ui.navigation.NoteScreen

@Composable
fun HomeScreen(
    state: State,
    onEvent: (Events) -> Unit,
    navigateTo: (Any) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigateTo(NoteScreen)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        LazyVerticalStaggeredGrid(
            contentPadding = padding,
            columns = StaggeredGridCells.Fixed(2)
        ) {
            items(state.notes) { note ->
                NoteItem(note = note, onEvent = onEvent) {
                    navigateTo(NoteScreen)
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Notes,
    onEvent: (Events) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onEvent(Events.SetSelectedNote(note))
                onClick()
            }
    ) {
        Text(
            text = note.title,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = note.description,
            modifier = Modifier.padding(8.dp)
        )
    }
}

