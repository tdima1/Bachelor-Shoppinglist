package com.example.shoppinglist.activeList

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentActiveListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ActiveListFragment : Fragment() {

    private lateinit var binding: FragmentActiveListBinding
    private lateinit var activeListAdapter: ActiveListAdapter
    private val strCurrentUserUID = FirebaseAuth.getInstance().currentUser!!.displayName.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_active_list, container, false)

        //Create an instance of the View Model Factory
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ActiveListViewModelFactory(application)

        val inventoryViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(ActiveListViewModel::class.java)

        setBindingsForUIElements(binding, inventoryViewModel)

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        activeListAdapter.cleanupListener()
    }

    fun setBindingsForUIElements(binding: FragmentActiveListBinding, viewModel: ActiveListViewModel){
        binding.setLifecycleOwner(this)

        binding.activeListRecyclerView.layoutManager = LinearLayoutManager(this.context)

        activeListAdapter = ActiveListAdapter(
            FirebaseDatabase.getInstance().getReference(strCurrentUserUID).child("ACTIVE"),
            viewModel.activeList
        )
        binding.activeListRecyclerView.adapter = activeListAdapter

        binding.activeListDoneButton.setOnClickListener(){
            viewModel.addItemsToInventory()
            Toast.makeText(this.context, "Items sent to Inventory", Toast.LENGTH_LONG).show()
            this.findNavController().navigate(R.id.action_activeListFragment_to_titleFragment)
        }

    }

}
