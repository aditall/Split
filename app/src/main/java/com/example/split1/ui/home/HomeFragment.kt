package com.example.split1.ui.home

import SpaceRepository
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.split1.MainActivity
import com.example.split1.adapters.SpaceAdapter
import com.example.split1.data.database.spaces.SpacesDatabase
import com.example.split1.data.datasource.SpacesLocalSource
import com.example.split1.data.model.RoomSpace
import com.example.split1.databinding.FragmentHomeBinding
import com.example.split1.ui.ImageUtil
import com.google.firebase.firestore.FirebaseFirestore

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
        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val roomSpacesDb: SpacesDatabase = SpacesDatabase.getInstance(requireContext())
        val spacesLocalSource = SpacesLocalSource(roomSpacesDb.spacesDao)
        val spaceRepository = SpaceRepository(firestoreDb, spacesLocalSource)
        homeViewModel = ViewModelProvider(
            this,
            HomeModelFactory(spaceRepository = spaceRepository)
        )[HomeViewModel::class.java]


        var spaceList = MutableLiveData<ArrayList<RoomSpace>>()
        val adapter = SpaceAdapter(ArrayList(), object : SpaceAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(
                    requireContext(),
                    "Item at position $position clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        val recyclerView: RecyclerView = binding.spacesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        homeViewModel.roomSpacesLiveData.observe(viewLifecycleOwner) {
            spaceList.value = ArrayList(it)
            adapter.submitList(spaceList.value!!)
        }

        binding.btnEditProfile.editProfileButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(HomeFragmentDirections.actionHomeFragmentToEditProfileFragment())
        }

        homeViewModel.userPhotoUri.observe(viewLifecycleOwner) {
            ImageUtil().laodImage(it, requireContext(), binding.btnEditProfile.editProfileButton)
        }

        binding.fbtnAddSpace.addSpaceButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(HomeFragmentDirections.actionHomeFragmentToSpaceFragment())
        }

        return binding.root
    }

}