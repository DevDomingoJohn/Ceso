package com.domindev.ceso

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.domindev.ceso.data.NotesDao
import com.domindev.ceso.presentation.ui.viewmodel.DataViewModel

class ViewModelFactoryHelper(
    private val dao: NotesDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DataViewModel(dao) as T
    }
}