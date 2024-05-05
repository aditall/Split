package com.example.split1.data.model

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spaces")
data class RoomSpace(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
)

data class FirestoreSpace(
    val name: String = "",
    val imageUrl: String = "",
    val spaceFriend: String = ""
)

fun FirestoreSpace.toRoomSpace(id: String): RoomSpace {
    return RoomSpace(
        id = id,
        name = this.name,
        imageUrl = this.imageUrl.toString(),
    )
}

fun RoomSpace.toFirestoreSpace(spaceFriend: String): FirestoreSpace {
    return FirestoreSpace(
        name = this.name,
        imageUrl = this.imageUrl,
        spaceFriend = spaceFriend
    )
}