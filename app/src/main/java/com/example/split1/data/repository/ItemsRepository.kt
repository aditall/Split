import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.split1.data.database.items.ItemsDao
import com.example.split1.data.datasource.ItemsLocalSource
import com.example.split1.data.model.RoomItem
import com.example.split1.data.model.FirestoreItem
import com.example.split1.data.model.toRoomItem
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemsRepository(val firestoreDb: FirebaseFirestore, val itemsLocalSource: ItemsLocalSource, val spaceId: String) {

    private val ITEMS_COLLECTION = "items"

    private var itemsListenerRegistration: ListenerRegistration? = null

    private val _roomItemsLiveData = MutableLiveData<List<RoomItem>>()
    val serverItems: LiveData<List<RoomItem>>
        get() = _roomItemsLiveData

    init {
        startListeningForSpacesUpdates()
    }

    fun startListeningForSpacesUpdates() {
        itemsListenerRegistration = firestoreDb.collection(ITEMS_COLLECTION).whereEqualTo("spaceId", spaceId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val roomItems = mutableListOf<RoomItem>()

                    snapshot?.documents?.forEach { document ->
                        val firestoreItem = document.toObject(FirestoreItem::class.java)
                        firestoreItem?.let { fsItem ->
                            val roomItem = fsItem.toRoomItem(document.id)
                            saveOrUpdateRoomItem(roomItem)
                            roomItems.add(roomItem)
                        }
                    }

                    _roomItemsLiveData.postValue(roomItems)
                }
            }
        // Listen for document deletions in the Firestore collection
        firestoreDb.collection(ITEMS_COLLECTION).whereEqualTo("spaceId", spaceId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val deletedItems = snapshot?.documentChanges
                        ?.filter { it.type == DocumentChange.Type.REMOVED }
                        ?.mapNotNull { change ->
                            change.document.toObject(FirestoreItem::class.java)
                                .toRoomItem(change.document.id)
                        }

                    deletedItems?.let { items ->
                        deletedItems(items)
                    }
                }
            }
    }

    private suspend fun deletedItems(items: List<RoomItem>) {
        itemsLocalSource.deleteByIds(items)
    }

    private suspend fun saveOrUpdateRoomItem(roomItem: RoomItem) {
        itemsLocalSource.insert(roomItem)
    }

    fun getAllItems(): List<RoomItem> {
        return itemsLocalSource.getAllItems(spaceId)
    }

    fun getMyItems(userId: String): List<RoomItem> {
        return itemsLocalSource.getMyItems(userId)
    }

    fun stopListeningForSpacesUpdates() {
        itemsListenerRegistration?.remove()
        itemsListenerRegistration = null
    }
}
