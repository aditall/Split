package com.example.split1.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddItemViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddItemViewModel::class.java)) {
            return AddItemViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}