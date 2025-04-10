package com.domindev.ceso.di

import android.content.Context
import androidx.room.Room
import com.domindev.ceso.data.Database
import com.domindev.ceso.data.NotesDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.core.content.edit
import com.domindev.ceso.ThemeState
import kotlinx.coroutines.flow.update

interface AppModule {
    val db: Database
    val dao: NotesDao
    val themeState: StateFlow<ThemeState>
    fun setDarkTheme(isDark: Boolean)
}

class AppModuleImpl(
    private val appContext: Context
): AppModule {
    override val db: Database by lazy {
        Room.databaseBuilder(
            appContext,
            Database::class.java,
            "data.db"
        ).build()
    }
    override val dao: NotesDao by lazy {
        db.dao
    }

    private val sharedPref = appContext.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    override val themeState: StateFlow<ThemeState> = MutableStateFlow(
        ThemeState(
            isDarkTheme = sharedPref.getBoolean("is_dark_theme", false)
        )
    )

    override fun setDarkTheme(isDark: Boolean) {
        (themeState as MutableStateFlow).update { it.copy(
            isDarkTheme = isDark
        ) }
        sharedPref.edit() { putBoolean("is_dark_theme", isDark) }
    }

}
