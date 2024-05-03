package com.example.split1.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.split1.data.LoginDataSource
import com.example.split1.data.LoginRepository
import com.example.split1.ui.profile.EditProfileViewModel

class EditProfileViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}