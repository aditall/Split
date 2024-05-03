package com.example.split1

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    var uriResult: MutableLiveData<Uri?> = MutableLiveData<Uri?>()
    var isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var currentUser: MutableLiveData<FirebaseUser> = MutableLiveData<FirebaseUser>()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val requestPermission =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                uriResult.value = uri
                Log.d("Picturerequest", "$uri")
            } else {
                Log.d("Picturerequest", "No media selected")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public override fun onStart() {
        super.onStart()
        currentUser.value = auth.currentUser
    }

    fun isLoggedin(): Boolean {
        return auth.currentUser != null
    }
}