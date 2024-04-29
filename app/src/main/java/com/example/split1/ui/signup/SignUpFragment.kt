package com.example.split1.ui.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.split1.MainActivity
import com.example.split1.R
import com.example.split1.databinding.FragmentSignUpBinding
import com.example.split1.ui.login.LoginViewModel
import com.example.split1.ui.login.LoginViewModelFactory

class SignUpFragment : Fragment() {
    private lateinit var signupViewModel: SignUpViewModel

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        signupViewModel = ViewModelProvider(this, SignUpModelFactory())[SignUpViewModel::class.java]

        val etPassWord = binding.etPassword
        val etUsername = binding.etName
        val etEmail = binding.etEmail
        val btnSignUp = binding.btnSignUp

        etPassWord.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signupViewModel.createUser(
                    etEmail.text.toString(),
                    etPassWord.text.toString(),
                    etUsername.text.toString()
                )
            }
            false
        }

        btnSignUp.setOnClickListener {
            signupViewModel.createUser(
                etEmail.text.toString(),
                etPassWord.text.toString(),
                etUsername.text.toString()
            )
        }

        binding.btnBackToMain.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }

        binding.imgAvatar.setOnClickListener {
            (activity as MainActivity).requestPermission.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        (activity as MainActivity).uriResult.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                signupViewModel.UplaodImage(uri)
            }
        }

        signupViewModel.ImageToShow.observe(viewLifecycleOwner) { uri ->
            Glide.with(this)
                .load(uri)
                .into(binding.imgAvatar);
        }

        signupViewModel.loginSuccessfull.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
            }
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}