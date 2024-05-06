package com.example.split1.data.database.items

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.split1.data.model.RoomItem

@Database(entities = [RoomItem::class], version = 5, exportSchema = false)
abstract class ItemsDatabase : RoomDatabase() {

    abstract val itemsDao: ItemsDao
    companion object {
        @Volatile
        private var INSTANCE: ItemsDatabase? = null

        fun getInstance(context: Context) : ItemsDatabase {
            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemsDatabase::class.java,
                    "items"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
            }

            return instance
        }
    }
}