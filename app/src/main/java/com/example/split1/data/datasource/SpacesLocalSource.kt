package com.example.split1.data.datasource

import com.example.split1.data.database.spaces.SpacesDao
import com.example.split1.data.model.RoomSpace

class SpacesLocalSource (private val spacesDao: SpacesDao) {
    fun getAllSpaces(): List<RoomSpace> {
        return spacesDao.getAllSpaces()
    }

    fun insert(space: RoomSpace) {
        spacesDao.insert(space)
    }

    fun getSpace(spaceId: String): RoomSpace {
        return spacesDao.get(spaceId)
    }

    fun getMySpaces(userId: String): List<RoomSpace> {
        return spacesDao.getMySpaces(userId)
    }

    fun deleteByIds(spaces: List<RoomSpace>) {
        for (space in spaces) {
            spacesDao.delete(space)
        }
    }

}