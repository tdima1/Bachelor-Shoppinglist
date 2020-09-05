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
import com.example.shoppinglist.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SharedInventoryFragment : Fragment() {

    private lateinit var binding: FragmentInventoryBinding
    private lateinit var inventoryAdapter: InventoryAdapter
    private lateinit var mInventoryViewModel: InventoryViewModel
    var currentUserData: User? =  User()
    private val usersReference = FirebaseDatabase.getInstance().getReference("USERS")
    private val groupsReference = FirebaseDatabase.getInstance().getReference("GROUPS")
    private val currentUserDisplayName = FirebaseAuth.getInstance().currentUser!!.displayName.toString()
    private lateinit var sharedActiveListReference: DatabaseReference
    private lateinit var sharedInventoryReference: DatabaseReference
    private lateinit var sharedCustomProductListReference: DatabaseReference

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
        FirebaseDatabaseHelper(sharedActiveListReference,
            sharedInventoryReference,
            sharedCustomProductListReference).updateQuantities(mInventoryViewModel.Inventory)
    }

    override fun onStop() {
        super.onStop()
        inventoryAdapter.cleanupListener()
    }

    private fun setBindingsForUIElements(binding: FragmentInventoryBinding, inventory: ArrayList<Product>){
        binding.inventoryRecyclerView.layoutManager = LinearLayoutManager(this.context)

        binding.sortCategoryButton.setOnClickListener(){
            mInventoryViewModel.sortByCategory()
            inventoryAdapter.notifyDataSetChanged()
        }

        binding.sortNameButton.setOnClickListener(){
            mInventoryViewModel.sortByName()
            inventoryAdapter.notifyDataSetChanged()
        }

        val currentUserValueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUserData = p0.getValue(User::class.java)
                sharedActiveListReference =
                    groupsReference.child(currentUserData!!.sharedGroupName).child("ACTIVE")
                sharedInventoryReference =
                    groupsReference.child(currentUserData!!.sharedGroupName).child("INVENTORY")
                sharedCustomProductListReference =
                    groupsReference.child(currentUserData!!.sharedGroupName).child("CUSTOM")

                inventoryAdapter = InventoryAdapter(parentFragment!!.context, sharedInventoryReference, inventory)

                binding.inventoryRecyclerView.adapter = inventoryAdapter

                        binding.restockButton.setOnClickListener(){
                            FirebaseDatabaseHelper(sharedActiveListReference,
                                sharedInventoryReference,
                                sharedCustomProductListReference).restock(mInventoryViewModel.Inventory)
                            findNavController().navigate(R.id.action_sharedInventoryFragment_to_sharedActiveListFragment)
                        }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        usersReference.child(currentUserDisplayName).addListenerForSingleValueEvent(currentUserValueEventListener)
    }


}
