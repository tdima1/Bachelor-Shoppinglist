package com.example.shoppinglist.inventory

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.shoppinglist.R
import com.example.shoppinglist.database.FirebaseDatabaseHelper
import com.example.shoppinglist.databinding.FragmentInventoryBinding
import com.example.shoppinglist.productList.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase



class InventoryFragment : Fragment() {

    private lateinit var binding: FragmentInventoryBinding
    private lateinit var inventoryAdapter: InventoryAdapter
    private lateinit var mInventoryViewModel: InventoryViewModel
    private val strCurrentUserUID = FirebaseAuth.getInstance().currentUser!!.displayName.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_inventory, container, false)

        //Create an instance of the View Model Factory
        val application = requireNotNull(this.activity).application
        val viewModelFactory = InventoryViewModelFactory(application)

        val inventoryViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(InventoryViewModel::class.java)
        mInventoryViewModel = inventoryViewModel

        setBindingsForUIElements(binding, inventoryViewModel.Inventory)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        FirebaseDatabaseHelper().updateQuantities(mInventoryViewModel.Inventory)
    }

    override fun onStop() {
        super.onStop()
        inventoryAdapter.cleanupListener()
    }

    fun setBindingsForUIElements(binding: FragmentInventoryBinding, inventory: ArrayList<Product>){
        binding.setLifecycleOwner(this)
        binding.inventoryRecyclerView.layoutManager = LinearLayoutManager(this.context)

        inventoryAdapter = InventoryAdapter(
            this.context,
            FirebaseDatabase.getInstance().getReference(strCurrentUserUID).child("INVENTORY"),
            inventory
        )
        binding.inventoryRecyclerView.adapter = inventoryAdapter

        binding.restockButton.setOnClickListener(){
            FirebaseDatabaseHelper().restock(mInventoryViewModel.Inventory)
            this.findNavController().navigate(R.id.action_inventoryFragment_to_activeListFragment)
        }

        binding.sortCategoryButton.setOnClickListener(){
            mInventoryViewModel.sortByCategory()
            inventoryAdapter.notifyDataSetChanged()
        }

        binding.sortNameButton.setOnClickListener(){
            mInventoryViewModel.sortByName()
            inventoryAdapter.notifyDataSetChanged()
        }
    }


}
