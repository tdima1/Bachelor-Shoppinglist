package com.example.shoppinglist.listBuilder

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentListBuilderBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.database.FirebaseDatabaseHelper
import com.example.shoppinglist.productAdapter.CategoryAdapter
import com.example.shoppinglist.productAdapter.SelectedProductAdapter
import com.example.shoppinglist.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SharedListBuilderFragment : Fragment() {

    private lateinit var categoryAdapter: CategoryAdapter
    var currentUserData: User? =  User()
    private val currentUserDisplayName = FirebaseAuth.getInstance().currentUser!!.displayName.toString()
    private val usersReference = FirebaseDatabase.getInstance().getReference("USERS")
    private val groupsReference = FirebaseDatabase.getInstance().getReference("GROUPS")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        val binding: FragmentListBuilderBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_builder, container, false)

        //Create an instance of the View Model Factory
        val application = requireNotNull(this.activity).application
        val viewModelFactory = SharedListBuilderViewModelFactory(application)
        val sharedListBuilderViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(SharedListBuilderViewModel::class.java)

        setBindingsForUIElements(binding, sharedListBuilderViewModel)

        return binding.root
    }

    fun setBindingsForUIElements(binding: FragmentListBuilderBinding, viewModel: SharedListBuilderViewModel){
        val currentUserValueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUserData = p0.getValue(User::class.java)
                val sharedCustomProductListReference = groupsReference.child(currentUserData!!.sharedGroupName).child("CUSTOM")

                categoryAdapter = CategoryAdapter(viewModel.getCategoriesList(), viewModel, binding,
                    sharedCustomProductListReference)
                binding.recyclerViewCategories.adapter = categoryAdapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }

        val selectedProductAdapter = SelectedProductAdapter(viewModel.selectedProductsList)
        binding.recyclerViewSelectedItems.adapter = selectedProductAdapter

        binding.listBuilderDoneButton.setOnClickListener(){
            viewModel.addItemsToActiveList()
            selectedProductAdapter.notifyDataSetChanged()
        }
        usersReference.child(currentUserDisplayName).addListenerForSingleValueEvent(currentUserValueEventListener)

        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(this.context)
        binding.customProductsButton.setOnClickListener(){
            this.findNavController().navigate(R.id.action_sharedListBuilderFragment_to_sharedCustomProductsFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        categoryAdapter.cleanupListener()
    }
}
