package com.example.split1.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.split1.adapters.SpaceAdapter
import com.example.split1.data.model.Spaces
import com.example.split1.databinding.FragmentHomeBinding
import com.example.split1.ui.ImageUtil
import com.example.split1.ui.login.LoginFragmentDirections

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this, HomeModelFactory())[HomeViewModel::class.java]
        val spaceList = MutableLiveData<ArrayList<Spaces>>()

        val adapter = spaceList.value?.let { SpaceAdapter(it) }
        val recyclerView: RecyclerView = binding.spacesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        binding.btnEditProfile.editProfileButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(HomeFragmentDirections.actionHomeFragmentToEditProfileFragment())
        }

        homeViewModel.userPhotoUri.observe(viewLifecycleOwner) {
            ImageUtil().laodImage(it, this, binding.btnEditProfile.editProfileButton)
        }

        binding.fbtnAddSpace.addSpaceButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(HomeFragmentDirections.actionHomeFragmentToSpaceFragment())
        }

        return binding.root
    }

}