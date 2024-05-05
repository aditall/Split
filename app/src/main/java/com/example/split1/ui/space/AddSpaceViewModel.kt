package com.example.split1.ui.space

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.split1.data.model.FirestoreSpace
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.security.SecureRandom
import java.util.UUID

class AddSpaceViewModel : ViewModel() {
    private val TAG = "AddSpaceViewModel"

    private val spaceImageRef = Firebase.storage.reference.child("spaces")
    private val firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    val ImageToShow: MutableLiveData<Uri?> = MutableLiveData<Uri?>()
    val ImageUri: MutableLiveData<Uri?> = MutableLiveData<Uri?>()

    private val spaceUid = generateRandomUid()

    fun uploadImage(imageUri: Uri) {
        val imageRef = spaceImageRef.child(spaceUid ?: "")
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

    fun addSpace(spaceName: String, friendName: String?) {
        ImageToShow.value?.let {
            FirestoreSpace(
                spaceName,
                it.toString(),
                friendName?:""
            )
        }?.let { firebaseDb.collection("spaces").document(spaceUid).set(it) }
    }
}