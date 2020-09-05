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
import com.example.shoppinglist.productAdapter.CategoryAdapter
import com.example.shoppinglist.productAdapter.ProductAdapter
import com.example.shoppinglist.productAdapter.SelectedProductAdapter
import com.example.shoppinglist.productList.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ListBuilderFragment : Fragment() {

    private lateinit var categoryAdapter: CategoryAdapter
    private val strCurrentUserUID = FirebaseAuth.getInstance().currentUser!!.displayName.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        val binding: FragmentListBuilderBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_builder, container, false)

        //Create an instance of the View Model Factory
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ListBuilderViewModelFactory(application)
        val listBuilderViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(ListBuilderViewModel::class.java)

        setBindingsForUIElements(binding, listBuilderViewModel)

        return binding.root
    }

    fun setBindingsForUIElements(binding: FragmentListBuilderBinding, viewModel: ListBuilderViewModel){
        //binding.setLifecycleOwner(this)
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(this.context)

        categoryAdapter = CategoryAdapter(viewModel.getCategoriesList(), viewModel, binding,
            FirebaseDatabase.getInstance().getReference(strCurrentUserUID).child("CUSTOM"))
        binding.recyclerViewCategories.adapter = categoryAdapter

        binding.recyclerViewSelectedItems.adapter =
            SelectedProductAdapter(viewModel.selectedProductsList)

        binding.listBuilderDoneButton.setOnClickListener(){
            viewModel.addItemsToActiveList()
            binding.recyclerViewSelectedItems.adapter =
                SelectedProductAdapter(viewModel.selectedProductsList)
        }

        binding.customProductsButton.setOnClickListener(){
            this.findNavController().navigate(R.id.action_listBuilderFragment_to_customProductsFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        categoryAdapter.cleanupListener()
    }
}
