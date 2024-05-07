package com.example.split1.ui.space

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.split1.MainActivity
import com.example.split1.databinding.FragmentSpaceBinding
import com.example.split1.ui.ImageUtil


class SpaceFragment : Fragment() {
    private lateinit var addSpaceViewModel: AddSpaceViewModel
    private var _binding: FragmentSpaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSpaceBinding.inflate(inflater, container, false)
        addSpaceViewModel =
            ViewModelProvider(this, AddSpaceViewModelFactory())[AddSpaceViewModel::class.java]

        binding.spaceImg.setOnClickListener {
            (activity as MainActivity).requestPermission.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        (activity as MainActivity).uriResult.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                addSpaceViewModel.uploadImage(uri)
            }
        }

        addSpaceViewModel.ImageToShow.observe(viewLifecycleOwner) { uri ->
            ImageUtil().laodImage(uri, requireContext(), binding.spaceImg)
            addSpaceViewModel.ImageUri.value = uri
        }

        binding.btnAddSpace.setOnClickListener {
            addSpaceViewModel.addSpace(
                binding.etSpaceName.text.toString(),
                binding.etFriendMail.text.toString()
            )
            Navigation.findNavController(it)
                .navigate(SpaceFragmentDirections.actionSpaceFragmentToHomeFragment())
        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(SpaceFragmentDirections.actionSpaceFragmentToHomeFragment())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}