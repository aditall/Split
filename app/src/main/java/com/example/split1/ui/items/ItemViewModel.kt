package com.example.split1.ui.items

import ItemsRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.split1.data.model.RoomItem
import com.example.split1.data.network.CurrencyConverter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ItemViewModel(private val itemRepository: ItemsRepository) : ViewModel() {

    private val _roomItemsLiveData = MutableLiveData<List<RoomItem>>()
    val roomItemsLiveData: LiveData<List<RoomItem>>
        get() = _roomItemsLiveData

    private val _sum = MutableLiveData<Double>()
    val sum: LiveData<Double> = _sum

    private val currencyConverter = CurrencyConverter()

    fun getMyItems() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val myItems = itemRepository.getMyItems(userId)
                    _roomItemsLiveData.postValue(myItems)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun getAllItems() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val allItems = itemRepository.getAllItems()
                _roomItemsLiveData.postValue(allItems)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun calculateSum() {
        var totalSum = 0.0
        _roomItemsLiveData.value?.forEach { item ->
            totalSum += item.price.toDouble()
        }
        _sum.value = totalSum
    }

    fun convertToUsd() {
        _sum.value?.let {
            currencyConverter.convertNisToUsd(it) { usdAmount ->
                _sum.value = usdAmount
            }
        }
    }

    fun convertToNis() {
        sum.value?.let {
            currencyConverter.convertUsdToNis(it) { nisAmount ->
                _sum.value = nisAmount
            }
        }
    }
}
