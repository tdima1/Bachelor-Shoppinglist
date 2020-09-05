package com.example.shoppinglist.inventory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shoppinglist.database.FirebaseDatabaseHelper
import com.example.shoppinglist.databinding.FragmentInventoryBinding
import com.example.shoppinglist.productList.Product


class InventoryViewModel (application: Application) : AndroidViewModel(application){
    var Inventory = ArrayList<Product>()

    init {
        //retrieveInventory()
    }

    fun sortByCategory(){
        Inventory.sortBy { product: Product -> product.category }
    }

    fun sortByName(){
        Inventory.sortBy { product: Product -> product.name }
    }
}