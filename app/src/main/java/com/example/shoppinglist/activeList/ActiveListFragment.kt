package com.example.shoppinglist.activeList


import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentActiveListBinding

/**
 * A simple [Fragment] subclass.
 */
class ActiveListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentActiveListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_active_list, container, false)

        return binding.root
    }


}
