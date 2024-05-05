package com.example.split1.ui.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.storage

class EditProfileViewModel : ViewModel() {
    private val TAG = "SignUpViewModel"
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val profileImageRef =  Firebase.storage.reference.child("profileImages")
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

    fun updateProfile(name: String) {
        var imageUri: Uri? = null
        if (ImageUri.value != null) {
            imageUri = ImageUri.value
        } else {
            //TODO:: prev image
        }
        var displayName = name
        if (name.isBlank()) {
            displayName = auth.currentUser?.displayName ?: ""
        }

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .setPhotoUri(imageUri)
            .build()

        // Update the user's profile
        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //TODO:: Profile update successful
                    // You can perform any additional actions here upon successful update
                } else {
                    //TODO:: Profile update failed
                    // Handle the failure scenario (e.g., log an error, show a message to the user)
                }
            }
    }
}