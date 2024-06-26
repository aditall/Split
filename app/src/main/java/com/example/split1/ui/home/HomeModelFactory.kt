package com.example.split1.ui.home

import SpaceRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.split1.ui.home.HomeViewModel

class HomeModelFactory (private val spaceRepository: SpaceRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(spaceRepository = spaceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}