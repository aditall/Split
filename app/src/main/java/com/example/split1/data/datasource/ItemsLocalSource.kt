package com.example.split1.data.datasource

import androidx.lifecycle.LiveData
import com.example.split1.data.database.items.ItemsDao
import com.example.split1.data.model.RoomItem

class ItemsLocalSource (private val itemsDao: ItemsDao) {
    fun getAllItems(): List<RoomItem> {
        return itemsDao.getAllItems()
    }

    fun insert(space: RoomItem) {
        itemsDao.insert(space)
    }

    fun getItem(spaceId: String): RoomItem {
        return itemsDao.get(spaceId)
    }

    fun getMyItems(userId: String): List<RoomItem> {
        return itemsDao.getMyItems(userId)
    }

    fun deleteByIds(spaces: List<RoomItem>) {
        for (space in spaces) {
            itemsDao.delete(space)
        }
    }

}