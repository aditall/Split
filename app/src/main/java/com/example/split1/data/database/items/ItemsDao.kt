package com.example.split1.data.database.items

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.split1.data.model.RoomItem

@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: RoomItem)

    @Delete
    fun delete(item: RoomItem)

    @Query("SELECT * FROM items WHERE id = :key")
    fun get(key: String): RoomItem

    @Query("SELECT * FROM items")
    fun getAllItems(): List<RoomItem>

    @Query("SELECT * FROM items WHERE id = :creator")
    fun getMyItems(creator: String): List<RoomItem>
}