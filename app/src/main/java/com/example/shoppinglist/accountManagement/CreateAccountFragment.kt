package com.example.shoppinglist.accountManagement

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.CreateAccountFragmentBinding
import com.example.shoppinglist.firebaseAuth.FragmentEmailPassword
import com.example.shoppinglist.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class CreateAccountFragment : Fragment() {
    private lateinit var binding: CreateAccountFragmentBinding
    private lateinit var viewModel: CreateAccountViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var avatarAdapter: AvatarAdapter
    val usersReference = FirebaseDatabase.getInstance().getReference("USERS")
    val groupsReference = FirebaseDatabase.getInstance().getReference("GROUPS")
    private var userToReturn = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.create_account_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(CreateAccountViewModel::class.java)
        viewModel.loadAvatars()

        binding.emailCreateAccountButton.setOnClickListener() {
            if (binding.fieldDisplayName.text.isEmpty()) {
                Toast.makeText(
                    this.context, "Please select a display name",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (userToReturn.avatar == 0){
                Toast.makeText(
                    this.context, "Please select a picture",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                createAccount(
                    binding.fieldEmail.text.toString(),
                    binding.fieldPassword.text.toString(),
                    binding.fieldDisplayName.text.toString()
                )

            }
        }

        val lastImageClicked = ImageView(context)
        binding.selectAvatarRecyclerView.layoutManager = GridLayoutManager(this.context, 6)
        avatarAdapter = AvatarAdapter(this.context, userToReturn, lastImageClicked, viewModel.avatarsList)
        binding.selectAvatarRecyclerView.adapter = avatarAdapter

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    private fun createAccount(email: String, password: String, displayName: String){
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this.activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User profile updated.")
                            }
                        }
                    //updateUI(user)
                    //usersReference.child(userToReturn.displayName).setValue(userToReturn)
                    //groupsReference.child(userToReturn.sharedGroupName).child(userToReturn.displayName).setValue(userToReturn)
                    signIn(email, password)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this.context, "Account creation failed.",
                        Toast.LENGTH_SHORT).show()
                }
                //binding.progressBar.visibility = View.INVISIBLE
            }
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
                    //Create group Here
                    val currentUserUID = auth.currentUser!!.uid
                    userToReturn.displayName = FirebaseAuth.getInstance().currentUser!!.displayName!!
                    userToReturn.email = FirebaseAuth.getInstance().currentUser!!.email!!
                    userToReturn.personalGroupName = currentUserUID
                    userToReturn.sharedGroupName = userToReturn.personalGroupName

                    usersReference.child(userToReturn.displayName).setValue(userToReturn)
                    //groupsReference.child(userToReturn.sharedGroupName).child(userToReturn.displayName).setValue(userToReturn)

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
    }

    private fun updateUI(user: FirebaseUser?) {
        binding.progressBar.visibility = View.INVISIBLE
        if (user != null) {
            this.findNavController().navigate(R.id.action_createAccountFragment_to_fragmentEmailPassword)
        }
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

    companion object {
        private const val TAG = "EmailPassword"
    }
}
