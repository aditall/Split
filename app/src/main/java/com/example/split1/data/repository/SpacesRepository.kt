import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.split1.data.database.spaces.SpacesDao
import com.example.split1.data.datasource.SpacesLocalSource
import com.example.split1.data.model.RoomSpace
import com.example.split1.data.model.FirestoreSpace
import com.example.split1.data.model.toRoomSpace
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpaceRepository (val firestoreDb: FirebaseFirestore, val spacesLocalSource: SpacesLocalSource){

    private val SPACES_COLLECTION = "spaces"

    private var spacesListenerRegistration: ListenerRegistration? = null

    private val _roomSpacesLiveData = MutableLiveData<List<RoomSpace>>()
    val serverSpaces: LiveData<List<RoomSpace>>
        get() = _roomSpacesLiveData

    init {
        startListeningForSpacesUpdates()
    }

    fun startListeningForSpacesUpdates() {
        spacesListenerRegistration = firestoreDb.collection(SPACES_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val roomSpaces = mutableListOf<RoomSpace>()

                    snapshot?.documents?.forEach { document ->
                        val firestoreSpace = document.toObject(FirestoreSpace::class.java)
                        firestoreSpace?.let { fsSpace ->
                            val roomSpace = fsSpace.toRoomSpace(document.id)
                            saveOrUpdateRoomSpace(roomSpace)
                            roomSpaces.add(roomSpace)
                        }
                    }

                    _roomSpacesLiveData.postValue(roomSpaces)
                }
            }
        // Listen for document deletions in the Firestore collection
        firestoreDb.collection(SPACES_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val deletedSpaces = snapshot?.documentChanges
                        ?.filter { it.type == DocumentChange.Type.REMOVED }
                        ?.mapNotNull { change ->
                            // Retrieve the deleted FirestoreSpace and convert to RoomSpace
                            change.document.toObject(FirestoreSpace::class.java)?.let { fsSpace ->
                                fsSpace.toRoomSpace(change.document.id)
                            }
                        }

                    deletedSpaces?.let { spaces ->
                        deleteRoomSpaces(spaces)
                    }
                }
            }
    }

    private suspend fun deleteRoomSpaces(sapces: List<RoomSpace>) {
        spacesLocalSource.deleteByIds(sapces)
    }

    private suspend fun saveOrUpdateRoomSpace(roomSpace: RoomSpace) {
        spacesLocalSource.insert(roomSpace)
    }

    fun insertSpace(space: FirestoreSpace){
        CoroutineScope(Dispatchers.IO).launch {
            //TODO:: spacesLocalSource.insert(space.toRoomSpace())
        }
    }

    fun getAllSpaces() : List<RoomSpace>{
        return spacesLocalSource.getAllSpaces()
    }

    fun stopListeningForSpacesUpdates() {
        spacesListenerRegistration?.remove()
        spacesListenerRegistration = null
    }
}
