package com.example.split1.ui.signup

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.split1.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class SignUpViewModel() : ViewModel() {
    private val TAG = "SignUpViewModel"
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val profileImageRef =  Firebase.storage.reference.child("profileImages")
    val name: MutableLiveData<String> = MutableLiveData<String>()
    val ImageToShow: MutableLiveData<Uri?> = MutableLiveData<Uri?>()

    private val _loginSuccessfull = MutableLiveData<Boolean>()
    val loginSuccessfull: LiveData<Boolean> = _loginSuccessfull
    fun createUser(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                            photoUri = ImageToShow.value
                        }

                        it.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileUpdateTask ->
                                if (profileUpdateTask.isSuccessful) {
                                    // Profile updated successfully
                                    Log.d(TAG, "User profile updated.")
                                } else {
                                    // Handle the error
                                }
                            }
                        storeUserData(it.uid, it.email)
                    }

                    _loginSuccessfull.value = true
                } else {
                    // TODO:: Handle the error
                }
            }
    }

    // Function to store user data in Firestore
    private fun storeUserData(userId: String?, email: String?) {
        val db = Firebase.firestore

        val userData = hashMapOf(
            "userId" to userId
        )

        db.collection("users").document(email ?: "").set(userData)
    }
    fun UplaodImage(imageUri: Uri) {
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

}