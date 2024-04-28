package com.example.split1.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.split1.data.LoginRepository
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginSuccessfull = MutableLiveData<Boolean>()
    val loginSuccessfull: LiveData<Boolean> = _loginSuccessfull

    private val _loginFailed = MutableLiveData<Boolean>()
    val loginFailed: LiveData<Boolean> = _loginFailed

    fun login(username: String, password: String) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                    if (user != null) {
                        _loginSuccessfull.value = true
                        Log.i("Login", "signInWithEmailAndPassword:success")
                    }
                } else {
                    _loginFailed.value = true
                    Log.i("Login", "Error")
                }
            }
    }

    fun updatePassword(password: String) {
        if (!isPasswordValid(password)) {
            // TODO:
        } else {
            // TODO:
        }
    }

    fun updateUsername(username: String) {
        if (!isUserNameValid(username)) {
            // TODO:
        } else {
           // TODO:
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}