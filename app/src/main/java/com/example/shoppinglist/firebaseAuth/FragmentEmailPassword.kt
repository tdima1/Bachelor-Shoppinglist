package com.example.shoppinglist.firebaseAuth

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.example.shoppinglist.R
import com.example.shoppinglist.database.FirebaseDatabaseHelper
import com.example.shoppinglist.databinding.FragmentEmailPasswordFragmentBinding
import com.example.shoppinglist.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class FragmentEmailPassword : Fragment() {

    private lateinit var binding: FragmentEmailPasswordFragmentBinding
    private lateinit var viewModel: FragmentEmailPasswordViewModel
    private lateinit var auth: FirebaseAuth
    private val usersReference = FirebaseDatabase.getInstance().getReference("USERS")
    val groupsReference = FirebaseDatabase.getInstance().getReference("GROUPS")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_email_password_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(FragmentEmailPasswordViewModel::class.java)

        binding.emailSignInButton.setOnClickListener(){
            signIn(binding.fieldEmail.text.toString(), binding.fieldPassword.text.toString())
        }

        binding.emailCreateAccountButton.setOnClickListener(){
            findNavController().navigate(R.id.action_fragmentEmailPassword_to_createAccountFragment)
        }

        binding.progressBar.visibility = View.INVISIBLE

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //getUsersDisplayNames()
        updateUI(currentUser)
        //if (currentUser != null)
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this.activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this.context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    //status.setText(R.string.auth_failed)
                }
                binding.progressBar.visibility = View.INVISIBLE
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.fieldEmail.error = "Required."
            valid = false
        } else {
            binding.fieldEmail.error = null
        }

        val password = binding.fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.fieldPassword.error = "Required."
            valid = false
        } else {
            binding.fieldPassword.error = null
        }
        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        binding.progressBar.visibility = View.INVISIBLE
        if (user != null) {
            this.findNavController().navigate(R.id.action_fragmentEmailPassword_to_titleFragment)
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}
