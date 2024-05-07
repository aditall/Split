package com.example.split1.ui.profile

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.split1.MainActivity
import com.example.split1.databinding.FragmentEditProfileBinding
import com.example.split1.ui.ImageUtil
import com.example.split1.ui.signup.EditProfileViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage


class EditProfileFragment : Fragment() {
    private lateinit var editProfileViewModel: EditProfileViewModel
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        editProfileViewModel =
            ViewModelProvider(this, EditProfileViewModelFactory())[EditProfileViewModel::class.java]

        binding.imgEditAvatar.setOnClickListener {
            (activity as MainActivity).requestPermission.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        (activity as MainActivity).uriResult.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                editProfileViewModel.uploadImage(uri)
            }
        }

        editProfileViewModel.ImageToShow.observe(viewLifecycleOwner) { uri ->
            ImageUtil().laodImage(uri, requireContext(), binding.imgEditAvatar)
            editProfileViewModel.ImageUri.value = uri
        }

        binding.btnSubmitEdit.setOnClickListener {
            editProfileViewModel.updateProfile(binding.etEditName.text.toString())
        }

        binding.btnBackToMain.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(EditProfileFragmentDirections.actionEditProfileFragmentToHomeFragment())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}