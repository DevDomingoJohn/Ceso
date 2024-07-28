package com.domindev.ceso.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Notes::class],
    version = 1
)
abstract class Database: RoomDatabase() {
    abstract val dao: NotesDao
}