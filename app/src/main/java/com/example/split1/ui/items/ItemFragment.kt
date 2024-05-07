package com.example.split1.ui.items

import ItemAdapter
import ItemsRepository
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.split1.data.database.items.ItemsDatabase
import com.example.split1.data.datasource.ItemsLocalSource
import com.example.split1.data.model.RoomItem
import com.example.split1.databinding.FragmentItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class ItemFragment : Fragment() {

    private lateinit var itemViewModel: ItemViewModel
    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!
    val args: ItemFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemBinding.inflate(inflater, container, false)


        val spaceId = args.spaceId

        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val roomItemsDb: ItemsDatabase = ItemsDatabase.getInstance(requireContext())
        val itemsLocalSource = ItemsLocalSource(roomItemsDb.itemsDao)
        val itemRepository = ItemsRepository(firestoreDb, itemsLocalSource, spaceId)
        itemViewModel = ViewModelProvider(
            this,
            ItemModelFactory(itemRepository = itemRepository)
        )[ItemViewModel::class.java]


        var itemList = MutableLiveData<ArrayList<RoomItem>>()
        val adapter = ItemAdapter(ArrayList(), object : ItemAdapter.ItemClickListener {
            override fun onDeleteButtonClick(position: Int) {
                itemList.value?.get(position)
                    ?.let {
                        firestoreDb.collection("items").document(it.id).delete()
                    }
            }

            override fun onEditButtonClick(position: Int) {
                itemList.value?.get(position)?.let { item ->
                    val action = ItemFragmentDirections.actionItemFragmentToEditItemFragment(
                        spaceId = spaceId,
                        itemId = item.id
                    )
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
        })
        val recyclerView: RecyclerView = binding.itemsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        itemViewModel.roomItemsLiveData.observe(viewLifecycleOwner) {
            itemList.value = ArrayList(it)
            adapter.submitList(itemList.value!!)
        }

        binding.fbtnAddItem.addSpaceButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(ItemFragmentDirections.actionItemFragmentToAddItemFragment(spaceId))
        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(ItemFragmentDirections.actionItemFragmentToHomeFragment())
        }

        binding.switchItems.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                itemViewModel.getMyItems()
            } else {
                itemViewModel.getAllItems()
            }
        }

        binding.currencySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                itemViewModel.convertToUsd()
            } else {
                itemViewModel.convertToNis()
            }
        }

        return binding.root
    }

}