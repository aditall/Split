package com.example.split1.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.split1.databinding.FragmentLoginBinding

import com.example.split1.R

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val etUsername = binding.editTextTextEmailAddress
        val etPassWord = binding.editTextTextPassword
        val btnLogin = binding.btnSignIn
        val btnSignUp = binding.btnSignUp
//        val loadingProgressBar = binding.loading


        val onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus -> // Check if the user is leaving the password EditText
            if (view.id == R.id.editTextTextPassword && !hasFocus) {
                loginViewModel.updatePassword(
                    etPassWord.text.toString()
                )
            } else if (view.id == R.id.editTextTextEmailAddress && !hasFocus) {
                loginViewModel.updateUsername(
                    etUsername.text.toString()
                )
            }
        }

        etUsername.onFocusChangeListener = onFocusChangeListener
        etPassWord.onFocusChangeListener = onFocusChangeListener

        etPassWord.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    etUsername.text.toString(),
                    etPassWord.text.toString()
                )
            }
            false
        }

        btnLogin.setOnClickListener {
//            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                etUsername.text.toString(),
                etPassWord.text.toString()
            )
        }
        
        btnSignUp.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.signUpFragment)
        }
    }

//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome) + model.displayName
//        // TODO : initiate successful logged in experience
//        val appContext = context?.applicationContext ?: return
//        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
//    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}