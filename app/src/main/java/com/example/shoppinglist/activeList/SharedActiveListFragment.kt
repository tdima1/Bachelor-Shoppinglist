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
import com.example.shoppinglist.database.FirebaseDatabaseHelper
import com.example.shoppinglist.databinding.FragmentActiveListBinding
import com.example.shoppinglist.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SharedActiveListFragment : Fragment() {

    private lateinit var binding: FragmentActiveListBinding
    private lateinit var activeListAdapter: ActiveListAdapter
    var currentUserData: User? =  User()
    private val currentUserDisplayName = FirebaseAuth.getInstance().currentUser!!.displayName.toString()
    private val usersReference = FirebaseDatabase.getInstance().getReference("USERS")
    private val groupsReference = FirebaseDatabase.getInstance().getReference("GROUPS")
    private val mContext = context

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

        binding.activeListRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val currentUserValueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUserData = p0.getValue(User::class.java)
                val sharedActiveListReference =
                    groupsReference.child(currentUserData!!.sharedGroupName).child("ACTIVE")
                val sharedInventoryReference =
                    groupsReference.child(currentUserData!!.sharedGroupName).child("INVENTORY")
                val sharedCustomProductListReference =
                    groupsReference.child(currentUserData!!.sharedGroupName).child("CUSTOM")

                activeListAdapter = ActiveListAdapter(sharedActiveListReference, viewModel.activeList)
                binding.activeListRecyclerView.adapter = activeListAdapter

                binding.activeListDoneButton.setOnClickListener(){
                    FirebaseDatabaseHelper(sharedActiveListReference,
                        sharedInventoryReference,
                        sharedCustomProductListReference).addItemsToInventory(viewModel.activeList)
                    //Toast.makeText(mContext, "Items sent to Inventory", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_sharedActiveListFragment_to_groupsFragment)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        usersReference.child(currentUserDisplayName).addListenerForSingleValueEvent(currentUserValueEventListener)
    }

}
