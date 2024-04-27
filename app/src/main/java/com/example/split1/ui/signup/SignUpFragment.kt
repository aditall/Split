package com.example.split1.ui.signup
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.split1.R
import com.example.split1.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener {
            onSignUp()
        }

        binding.btnBackToMain.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signUpFragment_to_LoginFragment)
        }

        return binding.root
    }

    private fun onSignUp() {
        TODO("Not yet implemented")
        val message = "Button Clicked!"
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}