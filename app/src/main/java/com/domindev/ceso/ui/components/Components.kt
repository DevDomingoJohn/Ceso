package com.domindev.ceso.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domindev.ceso.R
import com.domindev.ceso.data.Notes
import com.domindev.ceso.ui.event.Events
import com.domindev.ceso.ui.state.State
import com.domindev.ceso.ui.theme.bodyFontFamily
import com.domindev.ceso.ui.theme.displayFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    state: State,
    searchQuery: String,
    searchResult: List<Notes>,
    onEvent: (Events) -> Unit,
    onClick: () -> Unit
) {
    androidx.compose.material3.SearchBar(
        query = searchQuery,
        onQueryChange = { onEvent(Events.SetSearchText(it)) },
        onSearch = {},
        placeholder = {
            Text(text = "Search Notes", fontFamily = displayFontFamily)
        },
        active = state.isSearching,
        onActiveChange = {
            onEvent(Events.SetSearch(it))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back Icon",
                modifier = Modifier.clickable {
                    onEvent(Events.SetSearchText(""))
                    onEvent(Events.SetSearch(false))
                }
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon",
                    modifier = Modifier.clickable {
                        onEvent(Events.SetSearchText(""))
                    }
                )
            }
        }
    ) {
        if (!state.columnView) {
            LazyVerticalStaggeredGrid(
                contentPadding = PaddingValues(vertical = 8.dp),
                columns = StaggeredGridCells.Fixed(2)
            ) {
                items(searchResult) { note ->
                    NoteItem(note = note, onEvent = onEvent) {
                        onClick()
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(searchResult) { note ->
                    NoteItem(note = note, onEvent = onEvent) {
                        onClick()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CesoHomeTopBar(
    state: State,
    onEvent: (Events) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(text = "Search Notes")},
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    state.drawerState.open()
                }
            }) {
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
                    Icon(painter = painterResource(id = R.drawable.outline_view_agenda_24), contentDescription = "Account")
                }
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Account")
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
            .shadow(elevation = 3.dp, shape = RectangleShape)
            .clickable { onEvent(Events.SetSearch(true)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenTopBar(
    state: State,
    onEvent: (Events) -> Unit,
    navigateBack: () -> Unit
) {
    if (state.onEdit) {
        TopAppBar(
            title = {  Text(text = "") },
            navigationIcon = {
                IconButton(onClick = {
                    navigateBack()
                    onEvent(Events.SaveNote)
                    onEvent(Events.ToggleEdit)
                    if (state.isFocus) onEvent(Events.ToggleFocus)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Arrow Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    /*TODO*/
                }) {
                    Icon(painter = painterResource(id = R.drawable.outline_push_pin_24), contentDescription = "Pin Icon")
                }
                IconButton(onClick = {
                    /*TODO*/
                }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share Icon")
                }
                IconButton(onClick = {
                    navigateBack()
                    onEvent(Events.DeleteNote)
                    onEvent(Events.ToggleEdit)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            },
            scrollBehavior = null
        )
    } else {
        TopAppBar(
            title = { Text(text = "") },
            navigationIcon = {
                IconButton(onClick = {
                    navigateBack()
                    onEvent(Events.SaveNote)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Arrow Back"
                    )
                }
            },
            actions = {},
            scrollBehavior = null
        )
    }
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
)

@Composable
fun CesoNavigationDrawer(
    state: State,
    content: @Composable () -> Unit
) {
    val items = listOf(
        NavigationItem(
            title = "Home",
            icon = Icons.Outlined.Home
        ),
        NavigationItem(
            title = "Trash",
            icon = Icons.Outlined.Delete
        ),
        NavigationItem(
            title = "Settings",
            icon = Icons.Outlined.Settings
        ),
        NavigationItem(
            title = "Help & Feedback",
            icon = Icons.Outlined.Info
        )
    )
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Ceso",
                    fontFamily = displayFontFamily,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(16.dp)
                )
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title)},
                        selected = index == state.selectedItem.value,
                        onClick = {
                            state.selectedItem.value = index
                            coroutineScope.launch {
                                state.drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = state.drawerState
    ) {
        content()
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
            .heightIn(min = Dp.Unspecified, max = 300.dp)
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