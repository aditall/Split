package com.example.split1.ui.home

import SpaceRepository
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.split1.data.database.spaces.SpacesDao
import com.example.split1.data.database.spaces.SpacesDatabase
import com.example.split1.data.model.FirestoreSpace
import com.example.split1.data.model.RoomSpace
import com.example.split1.data.model.toRoomSpace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HomeViewModel (spaceRepository: SpaceRepository): ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val scope = CoroutineScope(Dispatchers.IO + Job())
    val roomSpacesLiveData: LiveData<List<RoomSpace>> = spaceRepository.roomSpacesLiveData
    private val _userPhotoUri = MutableLiveData<Uri?>()
    val userPhotoUri: LiveData<Uri?> = _userPhotoUri
     init {
         _userPhotoUri.value = auth.currentUser?.photoUrl
     }


}