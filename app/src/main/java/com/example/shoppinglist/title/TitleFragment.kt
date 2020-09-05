package com.example.shoppinglist.title

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentTitleBinding
import com.example.shoppinglist.firebaseAuth.FragmentEmailPassword
import com.google.firebase.auth.FirebaseAuth

class TitleFragment : Fragment() {

    private var auth = FirebaseAuth.getInstance()
    private lateinit var binding: FragmentTitleBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
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

        binding.signOutButton.setOnClickListener(){
            signOut()
        }

        binding.verifyEmailButton.setOnClickListener(){
            sendEmailVerification()
        }

        binding.groupsButton.setOnClickListener(){
            findNavController().navigate(R.id.action_titleFragment_to_groupsFragment)
        }

        return binding.root
    }

    private fun signOut() {
        auth.signOut()
        findNavController().navigate(R.id.action_titleFragment_to_fragmentEmailPassword)
    }

    private fun sendEmailVerification() {
        // Disable button
        binding.verifyEmailButton.isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this.activity!!) { task ->
                // [START_EXCLUDE]
                // Re-enable button
                binding.verifyEmailButton.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(this.context,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(this.context,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
                // [END_EXCLUDE]
            }
        // [END send_email_verification]
    }

    companion object {
        private const val TAG = "EmailPassword"
    }


}
