package com.example.split1.ui.space

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.split1.ui.profile.EditProfileViewModel

class AddSpaceViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddSpaceViewModel::class.java)) {
            return AddSpaceViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}