package com.domindev.ceso.presentation.ui.events

sealed interface Events {
    data class SetTitle(val title: String): Events
    data class SetDescription(val desc: String): Events
    data object SaveNote: Events
}