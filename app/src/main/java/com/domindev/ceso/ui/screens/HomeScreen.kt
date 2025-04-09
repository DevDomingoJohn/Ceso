package com.domindev.ceso.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.domindev.ceso.NoteScreen
import com.domindev.ceso.ui.event.Events
import com.domindev.ceso.ui.state.State
import com.domindev.ceso.ui.components.CesoHomeTopBar
import com.domindev.ceso.ui.components.CesoNavigationDrawer
import com.domindev.ceso.ui.components.NoteItem
import com.domindev.ceso.ui.components.SearchBar
import com.domindev.ceso.ui.viewmodel.DataViewModel
import com.domindev.ceso.util.getFileNameFromUri
import com.domindev.ceso.util.readTextFileFromUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: DataViewModel,
    state: State,
    navController: NavHostController,
    onEvent: (Events) -> Unit,
    navigateTo: (Any) -> Unit
) {
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                val fileName = getFileNameFromUri(context = context, it)
                val fileContent = readTextFileFromUri(context = context, uri = it)
                onEvent(Events.SetTitle("$fileName"))
                onEvent(Events.SetDescription("$fileContent"))
                navigateTo(NoteScreen)
            }
        }
    )
    val notes by viewModel.notes.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // onClick = { filePickerLauncher.launch(arrayOf("text/plain"))}
        CesoNavigationDrawer(state, navController) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        navigateTo(NoteScreen)
                        onEvent(Events.ToggleFocus)
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
                    }
                },
                topBar = {
                    if (state.isSearching) {
                        SearchBar(
                            state = state,
                            searchQuery = searchText,
                            searchResult = notes,
                            onEvent = onEvent
                        ) {
                            navigateTo(NoteScreen)
                        }
                    } else {
                        CesoHomeTopBar(
                            state = state,
                            onEvent = onEvent,
                            scrollBehavior = scrollBehavior
                        )
                    }

                },
                modifier = if (!state.isSearching) {
                    Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                } else {
                    Modifier
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
    }
}