package com.example.shoppinglist.activeList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shoppinglist.database.FirebaseDatabaseHelper
import com.example.shoppinglist.productList.Product

class ActiveListViewModel (application: Application) : AndroidViewModel(application){
    var activeList = ArrayList<Product>()

    init {

    }

    fun addItemsToInventory(){
        val databaseHelper = FirebaseDatabaseHelper()
        databaseHelper.addItemsToInventory(activeList)
    }
}