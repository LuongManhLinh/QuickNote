package com.example.quicknote.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quicknote.data.entity.NoteEntity

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("SELECT * FROM note")
    suspend fun getAll(): List<NoteEntity>

}