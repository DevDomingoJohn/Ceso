package com.domindev.ceso.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.sp
import com.domindev.ceso.ui.event.Events
import com.domindev.ceso.ui.state.State
import com.domindev.ceso.ui.theme.bodyFontFamily
import com.domindev.ceso.ui.theme.displayFontFamily
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
import com.domindev.ceso.ui.components.NoteScreenTopBar

@Composable
fun NoteScreen(
    state: State,
    onEvent: (Events) -> Unit,
    focusRequester: FocusRequester = FocusRequester(),
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = { NoteScreenTopBar(state = state, onEvent = onEvent) {
            navigateBack()
        } }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
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
            )
            TextField(
                value = state.description,
                onValueChange = { onEvent(Events.SetDescription(it)) },
                placeholder = {
                    Text(
                        text = "Description",
                        fontSize = 24.sp,
                        fontFamily = bodyFontFamily
                    )},
                textStyle = TextStyle(fontSize = 24.sp, fontFamily = bodyFontFamily),
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
                    .fillMaxHeight()
                    .focusRequester(focusRequester)
            )
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressHandled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (state.isFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    BackHandler(
        enabled = !backPressHandled
    ) {
        onEvent(Events.SaveNote)
        if (state.onEdit) onEvent(Events.ToggleEdit)
        if (state.isFocus) onEvent(Events.ToggleFocus)
        backPressHandled = true
        coroutineScope.launch {
            awaitFrame()
            onBackPressedDispatcher?.onBackPressed()
            backPressHandled = false
        }
    }
}