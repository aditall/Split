package com.example.split1.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class RoomItem(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "publisher") val publisher: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "spaceId") val spaceId: String
    )

data class FirestoreItem(
    val name: String = "",
    val imageUrl: String = "",
    val publisher: String = "",
    val price: String = "",
    val spaceId: String = ""
)

fun FirestoreItem.toRoomItem(id: String): RoomItem {
    return RoomItem(
        id = id,
        name = this.name,
        imageUrl = this.imageUrl.toString(),
        publisher = this.publisher,
        price = this.price,
        spaceId = this.spaceId
    )
}

fun RoomItem.toFirestoreItem(): FirestoreItem {
    return FirestoreItem(
        name = this.name,
        imageUrl = this.imageUrl,
        publisher = this.publisher,
        price = this.price,
        spaceId = this.spaceId
    )
}