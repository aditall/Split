package com.example.split1.ui.item

import android.net.Uri
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
import com.example.split1.databinding.FragmentEditItemBinding
import com.example.split1.databinding.FragmentEditProfileBinding
import com.example.split1.ui.ImageUtil
import com.example.split1.ui.items.ItemFragmentArgs
import com.example.split1.ui.signup.EditProfileViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditItemFragment : Fragment() {
    private lateinit var editItemViewModel: EditItemViewModel
    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!
    private val args: EditItemFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditItemBinding.inflate(inflater, container, false)
        editItemViewModel =
            ViewModelProvider(this, EditItemViewModelFactory())[EditItemViewModel::class.java]

        val spaceId: String = args.spaceId


        binding.imgEditItem.setOnClickListener {
            (activity as MainActivity).requestPermission.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        (activity as MainActivity).uriResult.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                editItemViewModel.uploadImage(uri)
            }
        }

        editItemViewModel.ImageToShow.observe(viewLifecycleOwner) { uri ->
            ImageUtil().laodImage(uri, requireContext(), binding.imgEditItem)
            editItemViewModel.ImageUri.value = uri
        }

        binding.btnSubmitEdit.setOnClickListener {
            editItemViewModel.updateItem(
                args.itemId,
                binding.etEditName.text.toString(),
                binding.etEditPrice.text.toString()
            )
            Navigation.findNavController(it)
                .navigate(EditItemFragmentDirections.actionEditItemFragmentToItemFragment(spaceId))
        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(EditItemFragmentDirections.actionEditItemFragmentToItemFragment(spaceId))
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            val imageUrl = editItemViewModel.getImageUrl(args.itemId)
            val itemPrice = editItemViewModel.getItemPrice(args.itemId)
            val itemName = editItemViewModel.getItemName(args.itemId)

            // Switch to the main thread for updating LiveData
            CoroutineScope(Dispatchers.Main).launch {
                imageUrl?.let {
                    editItemViewModel.ImageToShow.value = it
                }
                binding.etEditPrice.setText(itemPrice.toString())
                binding.etEditName.setText(itemName.toString())
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}