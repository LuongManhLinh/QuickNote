package com.example.quicknote.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quicknote.data.constant.DatabaseConstant
import com.example.quicknote.data.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = true)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var Instance: NoteDatabase? = null

        fun getDatabase(context: Context) : NoteDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                        context = context,
                        klass = NoteDatabase::class.java,
                        name = DatabaseConstant.DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}