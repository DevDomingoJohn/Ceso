package com.domindev.ceso.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domindev.ceso.presentation.ui.events.Events
import com.domindev.ceso.presentation.state.State
import com.domindev.ceso.data.Notes
import com.domindev.ceso.presentation.ui.navigation.NoteScreen
import com.domindev.ceso.presentation.ui.theme.bodyFontFamily
import com.domindev.ceso.presentation.ui.theme.displayFontFamily
import com.domindev.ceso.R

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
        },
        topBar = {
            MyCustomTopBar(
                title = "Search Notes",
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    if (!state.columnView) {
                        IconButton(onClick = { onEvent(Events.ToggleView) }) {
                            Icon(painter = painterResource(id = R.drawable.baseline_grid_view_24), contentDescription = "Account")
                        }
                    } else {
                        IconButton(onClick = { onEvent(Events.ToggleView) }) {
                            Icon(painter = painterResource(id = R.drawable.baseline_view_agenda_24), contentDescription = "Account")
                        }
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Account")
                    }
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                    .shadow(elevation = 3.dp, shape = RectangleShape)
                    .clickable { /*TODO*/ }
            )
        }
    ) { padding ->
        if (!state.columnView) {
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
        } else {
            LazyColumn(
                contentPadding = padding
            ) {
                items(state.notes) { note ->
                    NoteItem(note = note, onEvent = onEvent) {
                        navigateTo(NoteScreen)
                    }
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

            .heightIn(min = Dp.Unspecified,max = 300.dp)
            .clickable {
                onEvent(Events.SetSelectedNote(note))
                onEvent(Events.ToggleEdit)
                onClick()
            }
    ) {
        Text(
            text = note.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = displayFontFamily,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = note.description,
            fontFamily = bodyFontFamily,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCustomTopBar(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        title = { Text(text = title)},
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = modifier
    )
}