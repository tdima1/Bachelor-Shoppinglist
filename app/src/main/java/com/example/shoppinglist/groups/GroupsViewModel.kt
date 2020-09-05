package com.example.shoppinglist.groups

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shoppinglist.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GroupsViewModel(application: Application) : AndroidViewModel(application) {
    var currentUser = User()
    var userList = ArrayList<User>()

    init {
        //groupName = FirebaseAuth.getInstance().currentUser!!.displayName.toString() + "_group"
    }



}
