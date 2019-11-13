package com.example.shoppinglist.inventory


import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentInventoryBinding

/**
 * A simple [Fragment] subclass.
 */
class InventoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentInventoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_inventory, container, false)
        //binding.setLifecycleOwner(this)
        return binding.root
    }


}
