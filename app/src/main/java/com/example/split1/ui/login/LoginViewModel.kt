package com.example.split1.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.split1.data.LoginRepository

import com.example.split1.R
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    fun login(username: String, password: String) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                    if (user != null) {
                        Log.i("Login", "signInWithEmailAndPassword:success")
                    }
                } else {
                    Log.i("Login", "Error")
                }
            }
    }

    fun updatePassword(password: String) {
        if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(loginState = R.string.invalid_password.toString(), loggedIn = false)
        } else {
            _loginForm.value = LoginFormState(loginState = R.string.login_success.toString(), loggedIn = false)
        }
    }

    fun updateUsername(username: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(loginState = R.string.invalid_username.toString() ,loggedIn = false)
        } else {
            _loginForm.value = LoginFormState(loginState = R.string.login_success.toString(), loggedIn = false)
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