package com.example.shoppinglist.customProducts

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager

import com.example.shoppinglist.R
import com.example.shoppinglist.database.FirebaseDatabaseHelper
import com.example.shoppinglist.databinding.CustomProductsFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.custom_products_fragment.*

class CustomProductsFragment() : Fragment() {

    private lateinit var binding: CustomProductsFragmentBinding
    private lateinit var mCustomProductsViewModel: CustomProductsViewModel
    private lateinit var mCustomProductsAdapter: CustomProductsAdapter
    private val strCurrentUserUID = FirebaseAuth.getInstance().currentUser!!.displayName.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.custom_products_fragment, container, false)

        //Create an instance of the View Model Factory
        val application = requireNotNull(this.activity).application
        val viewModelFactory = CustomProductsViewModelFactory(application)

        val customProductsViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(CustomProductsViewModel::class.java)
        mCustomProductsViewModel = customProductsViewModel

        setBindingsForUIElements(binding, mCustomProductsViewModel)

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        mCustomProductsAdapter.cleanupListener()
    }

    fun setBindingsForUIElements(binding: CustomProductsFragmentBinding, customProductsViewModel: CustomProductsViewModel){

        var lastClickedImage = ImageView(context)

        binding.productImagesRecyclerView.layoutManager = GridLayoutManager(this.context, 6)
        val productImagesAdapter = ProductImagesAdapter(this.context,
            customProductsViewModel.customProduct,
            lastClickedImage,
            customProductsViewModel.productImagesDrawables)
        binding.productImagesRecyclerView.adapter = productImagesAdapter

        binding.customProductsRecyclerView.layoutManager = GridLayoutManager(this.context, 4)
        mCustomProductsAdapter = CustomProductsAdapter(
            FirebaseDatabase.getInstance().getReference(strCurrentUserUID).child("CUSTOM"),
            customProductsViewModel.customProductList)
        binding.customProductsRecyclerView.adapter = mCustomProductsAdapter

        binding.createCustomProductButton.setOnClickListener(){
            if (customProductNameInput.text.toString() != "") {
                customProductsViewModel.customProduct.name = customProductNameInput.text.toString()
                FirebaseDatabaseHelper().createCustomProduct(customProductsViewModel.customProduct)
                customProductNameInput.text!!.clear()
                lastClickedImage.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
            }
        }
    }

}
