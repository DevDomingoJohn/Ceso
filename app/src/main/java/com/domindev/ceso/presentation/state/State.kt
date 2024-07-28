package com.domindev.ceso.presentation.state

import com.domindev.ceso.data.Notes

data class State(
    val notes: List<Notes> = emptyList(),
    val title: String = "",
    val description: String = "",
)
