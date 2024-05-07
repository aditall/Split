package com.example.split1.ui.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.split1.MainActivity
import com.example.split1.databinding.FragmentAddItemBinding
import com.example.split1.ui.ImageUtil


class AddItemFragment : Fragment() {
    private lateinit var addItemViewModel: AddItemViewModel
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    val args: AddItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        addItemViewModel =
            ViewModelProvider(this, AddItemViewModelFactory())[AddItemViewModel::class.java]


        val spaceId = args.spaceId

        binding.itemImg.setOnClickListener {
            (activity as MainActivity).requestPermission.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        (activity as MainActivity).uriResult.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                addItemViewModel.uploadImage(uri)
            }
        }

        addItemViewModel.ImageToShow.observe(viewLifecycleOwner) { uri ->
            ImageUtil().laodImage(uri, requireContext(), binding.itemImg)
            addItemViewModel.ImageUri.value = uri
        }

        binding.btnAddItem.setOnClickListener {
            addItemViewModel.addItem(
                binding.etItemName.text.toString(),
                binding.etPrice.text.toString(),
                spaceId
            )
            Navigation.findNavController(it)
                .navigate(AddItemFragmentDirections.actionAddItemFragmentToItemFragment(spaceId))
        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(AddItemFragmentDirections.actionAddItemFragmentToItemFragment(spaceId))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}