package com.example.split1.ui.items

import ItemsRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.split1.data.model.RoomItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ItemViewModel (itemRepository: ItemsRepository): ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val scope = CoroutineScope(Dispatchers.IO + Job())
    val roomItemsLiveData: LiveData<List<RoomItem>> = itemRepository.roomItemsLiveData

}