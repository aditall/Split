package com.example.split1.ui.item

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.split1.data.model.FirestoreItem
import com.example.split1.data.model.FirestoreSpace
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.security.SecureRandom
import java.util.UUID

class AddItemViewModel : ViewModel() {
    private val TAG = "AddItemViewModel"

    private val itemImageRef = Firebase.storage.reference.child("items")
    private val firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    val ImageToShow: MutableLiveData<Uri> = MutableLiveData<Uri>()
    val ImageUri: MutableLiveData<Uri?> = MutableLiveData<Uri?>()

    private val itemUid = generateRandomUid()

    fun uploadImage(imageUri: Uri) {
        val imageRef = itemImageRef.child(itemUid ?: "")
        val uploadTask = imageRef.putFile(imageUri)

        // Monitor the upload progress.
        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress: Double =
                100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
        }.addOnSuccessListener { taskSnapshot ->
            // This listener is triggered when the file is uploaded successfully.
            // Using the below code you can get the download url of the file
            imageRef.downloadUrl
                .addOnSuccessListener { uri: Uri ->
                    ImageToShow.value = uri
                }
        }.addOnFailureListener { exception -> }
    }

    private fun generateRandomUid(): String {
        val random = SecureRandom()
        val uidBytes = ByteArray(16) // 16 bytes (128 bits) for a UUID

        // Generate random bytes
        random.nextBytes(uidBytes)

        // Create UUID from bytes
        val uuid = UUID.nameUUIDFromBytes(uidBytes)

        // Convert UUID to string (remove hyphens for a compact representation)
        return uuid.toString().replace("-", "")
    }

    fun addItem(itemName: String, price: String, relatedSpaceUid: String) {
        FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
            FirestoreItem(
                itemName,
                ImageToShow.value.toString(),
                userId,
                price,
                relatedSpaceUid
            )
            }?.let { firebaseDb.collection("items").document(itemUid).set(it) }
    }
}