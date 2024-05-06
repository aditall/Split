package com.example.split1.ui.items

import ItemsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ItemModelFactory (private val itemRepository: ItemsRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            return ItemViewModel(itemRepository = itemRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}