package com.example.shoppinglist.title


import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentTitleBinding

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding: FragmentTitleBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_title, container, false)

        binding.btnInventory.setOnClickListener{
            findNavController().navigate(R.id.action_titleFragment_to_inventoryFragment)
        }

        binding.btnMakeList.setOnClickListener {
            findNavController().navigate(R.id.action_titleFragment_to_listBuilderFragment)
        }

        binding.btnResumeShopping.setOnClickListener {
            findNavController().navigate(R.id.action_titleFragment_to_activeListFragment)
        }

        return binding.root
    }


}
