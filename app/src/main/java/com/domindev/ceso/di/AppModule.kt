package com.domindev.ceso.di

import android.content.Context
import androidx.room.Room
import com.domindev.ceso.data.Database
import com.domindev.ceso.data.NotesDao

interface AppModule {
    val db: Database
    val dao: NotesDao
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
}
