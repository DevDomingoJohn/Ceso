package com.domindev.ceso.ui.event

import android.content.Context
import android.net.Uri
import com.domindev.ceso.data.Notes

sealed interface Events {
    data class SetTitle(val title: String): Events
    data class SetDescription(val desc: String): Events
    data class SetSelectedNote(val note: Notes): Events
    data class SetSearchText(val text: String): Events
    data class SetSearch(val state: Boolean): Events
    data object ToggleEdit: Events
    data object ToggleView: Events
    data object ToggleFocus: Events
    data object SaveNote: Events
    data object DeleteNote: Events

    data class ShareNote(val context: Context): Events
    data class ExportNotes(val context: Context): Events
    data class ImportNotes(val context: Context, val uri: Uri): Events
}