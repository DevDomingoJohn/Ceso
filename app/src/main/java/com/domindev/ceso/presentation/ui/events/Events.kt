package com.domindev.ceso.presentation.ui.events

import com.domindev.ceso.data.Notes

sealed interface Events {
    data class SetTitle(val title: String): Events
    data class SetDescription(val desc: String): Events
    data class SetSelectedNote(val note: Notes): Events
    data object SaveNote: Events
    data object DeleteNote: Events
}