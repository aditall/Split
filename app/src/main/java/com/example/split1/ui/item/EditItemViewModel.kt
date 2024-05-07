package com.example.split1.ui.item

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class EditItemViewModel : ViewModel() {
    private val TAG = "EditItemViewModel"
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val profileImageRef =  Firebase.storage.reference.child("items")
    val db = FirebaseFirestore.getInstance().collection("items")

    val name: MutableLiveData<String> = MutableLiveData<String>()
    val ImageToShow: MutableLiveData<Uri?> = MutableLiveData<Uri?>()
    val ImageUri: MutableLiveData<Uri?> = MutableLiveData<Uri?>()

    fun uploadImage(imageUri: Uri) {
        val imageRef = profileImageRef.child(auth.currentUser?.uid ?: "")
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

    fun updateItem(itemId: String, name: String, price: String) {
        var imageUri: Uri? = null
        if (ImageUri.value != null) {
            imageUri = ImageUri.value
        } else {
            //TODO:: prev image
        }

        val item = hashMapOf<String, Any>(
            "name" to name,
            "price" to price,
            "imageUrl" to imageUri.toString()
        )

        db.document(itemId).update(item)
    }

    suspend fun getImageUrl(itemId: String): Uri? {
        val item = db.document(itemId).get().await()

        if (item.exists()) {
            val imageUrl = item.getString("imageUrl")
            return imageUrl?.let { Uri.parse(it) }
        } else {
            return null
        }
    }

    suspend fun getItemName(itemId: String): String? {
        val item = db.document(itemId).get().await()

        if (item.exists()) {
            val itemName = item.getString("name")
            return itemName
        } else {
            return null
        }
    }

    suspend fun getItemPrice(itemId: String): String? {
        val item = db.document(itemId).get().await()

        if (item.exists()) {
            val itemPrice = item.getString("price")
            return itemPrice
        } else {
            return null
        }
    }
}