package com.example.split1.data.database.spaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.split1.data.model.RoomSpace
import java.util.concurrent.Flow

@Dao
interface SpacesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(space: RoomSpace)

    @Delete
    fun delete(space: RoomSpace)

    @Query("SELECT * FROM spaces WHERE id = :key")
    fun get(key: String): RoomSpace

    @Query("SELECT * FROM spaces")
    fun getAllSpaces(): List<RoomSpace>

    @Query("SELECT * FROM spaces WHERE id = :creator")
    fun getMySpaces(creator: String): List<RoomSpace>
}