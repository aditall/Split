package com.example.split1.ui.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userPhotoUri = MutableLiveData<Uri?>()
    val userPhotoUri: LiveData<Uri?> = _userPhotoUri
     init {
         _userPhotoUri.value = auth.currentUser?.photoUrl
     }

}