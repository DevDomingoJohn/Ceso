package com.domindev.ceso.presentation.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domindev.ceso.presentation.ui.events.Events
import com.domindev.ceso.presentation.state.State
import com.domindev.ceso.presentation.ui.theme.bodyFontFamily
import com.domindev.ceso.presentation.ui.theme.displayFontFamily
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    state: State,
    onEvent: (Events) -> Unit,
    focusRequester: FocusRequester = FocusRequester(),
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            if (state.onEdit) {
                MyCustomTopBar(
                    title = "",
                    navigationIcon = {
                        IconButton(onClick = {
                            navigateBack()
                            onEvent(Events.SaveNote)
                            onEvent(Events.ToggleEdit)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Arrow Back"
                            )
                        }
                    },
                    actions = {
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
                    }
                )
            } else {
                MyCustomTopBar(
                    title = "",
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
                    actions = {}
                )
            }
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TextField(
                value = state.title,
                onValueChange = { onEvent(Events.SetTitle(it)) },
                placeholder = {
                    Text(
                        text = "Title",
                        fontSize = 32.sp,
                        fontFamily = displayFontFamily
                    ) },
                textStyle = TextStyle(fontSize = 32.sp,fontFamily = displayFontFamily),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 12.dp)
            )
            TextField(
                value = state.description,
                onValueChange = { onEvent(Events.SetDescription(it)) },
                placeholder = {
                    Text(
                        text = "Description",
                        fontSize = 18.sp,
                        fontFamily = bodyFontFamily
                    )},
                textStyle = TextStyle(fontSize = 18.sp, fontFamily = bodyFontFamily),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .fillMaxHeight(0.9f)
                    .focusRequester(focusRequester)
            )
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressHandled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler(
        enabled = !backPressHandled
    ) {
        onEvent(Events.SaveNote)
        if (state.onEdit) {
            onEvent(Events.ToggleEdit)
        }
        backPressHandled = true
        coroutineScope.launch {
            awaitFrame()
            onBackPressedDispatcher?.onBackPressed()
            backPressHandled = false
        }
    }
}