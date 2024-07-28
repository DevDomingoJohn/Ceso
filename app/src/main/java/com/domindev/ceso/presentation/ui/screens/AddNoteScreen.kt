package com.domindev.ceso.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domindev.ceso.Events
import com.domindev.ceso.presentation.state.State
import com.domindev.ceso.presentation.ui.theme.CesoTheme

@Composable
fun AddNoteScreen(
    state: State,
    onEvent: (Events) -> Unit,
    focusRequester: FocusRequester = FocusRequester()
) {
    CesoTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Add Note",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(
                value = state.title,
                onValueChange = { onEvent(Events.SetTitle(it)) },
                placeholder = { Text(text = "Title")},
                textStyle = TextStyle(fontSize = 24.sp),
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
            )
            TextField(
                value = state.description,
                onValueChange = { onEvent(Events.SetDescription(it)) },
                placeholder = { Text(text = "Description")},
                textStyle = TextStyle(fontSize = 18.sp),
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
                    .fillMaxHeight(0.5f)
                    .focusRequester(focusRequester)
            )
            Button(onClick = {
                onEvent(Events.SaveNote)
            }) {
                Text(text = "Save Note")
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}