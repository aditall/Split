package com.example.split1.ui.items

import ItemsRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.split1.data.model.RoomItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ItemViewModel (private val itemRepository: ItemsRepository): ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val scope = CoroutineScope(Dispatchers.IO + Job())
    val roomItemsLiveData: LiveData<List<RoomItem>> = itemRepository.roomItemsLiveData

    fun getMyItems() {
        FirebaseAuth.getInstance().currentUser?.uid?.let { itemRepository.getMyItems(it) }
    }

    fun getAllItems() {
        //TODO("Not yet implemented")
    }

    fun convertToUsd() {
        TODO("Not yet implemented")
    }

    fun convertToNis() {
        TODO("Not yet implemented")
    }

}