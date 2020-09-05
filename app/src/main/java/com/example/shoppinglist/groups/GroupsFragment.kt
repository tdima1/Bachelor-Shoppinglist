package com.example.shoppinglist.groups

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.set
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.GroupsFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class GroupsFragment : Fragment() {

    private lateinit var viewModel: GroupsViewModel
    private lateinit var binding: GroupsFragmentBinding
    private lateinit var groupsAdapter: GroupsAdapter

    private val usersReference = FirebaseDatabase.getInstance().getReference("USERS")
    private val currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
    private val currentUserGroupReference = FirebaseDatabase.getInstance().getReference("GROUPS").child(currentUserUID)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.groups_fragment, container, false
        )
        usersReference.child(FirebaseAuth.getInstance().currentUser!!.displayName!!).child("online").setValue(true)
        viewModel = ViewModelProviders.of(this).get(GroupsViewModel::class.java)

        binding.activeUsersRecyclerView.layoutManager = LinearLayoutManager(this.context)
        groupsAdapter = GroupsAdapter(this.context, usersReference, viewModel.userList, viewModel.currentUser )
        binding.activeUsersRecyclerView.adapter = groupsAdapter

        binding.addUserToGroupButton.setOnClickListener() {
            if (binding.addNewUserField.text.toString() != "") {
                if (groupsAdapter.currentUserData!!.sharedGroupName != groupsAdapter.currentUserData!!.personalGroupName){
                    Toast.makeText(this.context, "You are not admin over this group", Toast.LENGTH_SHORT).show()
                } else {
                    usersReference.child(binding.addNewUserField.text.toString())
                        .child("sharedGroupName").setValue(currentUserUID)
                    groupsAdapter.notifyDataSetChanged()
                }
                binding.addNewUserField.setText("")
            }
        }

        binding.makeSharedListButton.setOnClickListener(){
            this.findNavController().navigate(R.id.action_groupsFragment_to_sharedListBuilderFragment)
        }

        binding.sharedActiveListButton.setOnClickListener(){
            this.findNavController().navigate(R.id.action_groupsFragment_to_sharedActiveListFragment)
        }

        binding.sharedInventoryButton.setOnClickListener(){
            this.findNavController().navigate(R.id.action_groupsFragment_to_sharedInventoryFragment)
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        usersReference.child(FirebaseAuth.getInstance().currentUser!!.displayName!!).child("online").setValue(false)
        groupsAdapter.notifyDataSetChanged()
        groupsAdapter.cleanupListener()

    }
}

