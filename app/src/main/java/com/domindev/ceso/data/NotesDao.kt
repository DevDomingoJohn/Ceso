package com.domindev.ceso.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDao {
    @Upsert
    suspend fun upsert(notes: Notes)

    @Delete
    suspend fun delete(notes: Notes)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Notes>>
}