package com.example.shoppinglist.accountManagement

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.R
import com.example.shoppinglist.user.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CreateAccountViewModel : ViewModel() {
    val avatarsList = ArrayList<Int>()

    init {

    }

    fun loadAvatars(){
        avatarsList.addAll(listOf(R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
            R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6,
            R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9,
            R.drawable.avatar10, R.drawable.avatar11, R.drawable.avatar12,
            R.drawable.avatar13, R.drawable.avatar14, R.drawable.avatar15,
            R.drawable.avatar16, R.drawable.avatar17, R.drawable.avatar18,
            R.drawable.avatar19, R.drawable.avatar20, R.drawable.avatar21,
            R.drawable.avatar22, R.drawable.avatar23, R.drawable.avatar24,
            R.drawable.avatar25, R.drawable.avatar26, R.drawable.avatar27))
    }


}
