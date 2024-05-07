package com.example.split1.ui.home

import SpaceRepository
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.split1.data.model.RoomSpace
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel (private val spaceRepository: SpaceRepository): ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val scope = CoroutineScope(Dispatchers.IO + Job())
    private val _roomSpacesLiveData = MutableLiveData<List<RoomSpace>>()
    val roomSpacesLiveData: LiveData<List<RoomSpace>>
        get() = _roomSpacesLiveData

    private val _userPhotoUri = MutableLiveData<Uri?>()
    val userPhotoUri: LiveData<Uri?> = _userPhotoUri
     init {
         _userPhotoUri.value = auth.currentUser?.photoUrl
     }


    fun getSpaces() {
        scope.launch {
            val spaces = spaceRepository.getAllSpaces()
            _roomSpacesLiveData.postValue(spaces)
        }
    }

}