package com.example.split1.data.database.spaces

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.split1.data.model.RoomSpace

@Database(entities = [RoomSpace::class], version = 4, exportSchema = false)
abstract class SpacesDatabase : RoomDatabase() {

    abstract val spacesDao: SpacesDao
    companion object {
        @Volatile
        private var INSTANCE: SpacesDatabase? = null

        fun getInstance(context: Context) : SpacesDatabase {
            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpacesDatabase::class.java,
                    "spaces"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
            }

            return instance
        }
    }
}