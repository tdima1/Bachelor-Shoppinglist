package com.example.shoppinglist.listBuilder

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.productList.Product
import com.example.shoppinglist.productList.ProductList

class ListBuilderViewModel(
    application: Application) : AndroidViewModel(application) {

    var tempProductList = MutableLiveData<ProductList>()

    init {

    }

    fun addItemsToTemporaryList(category: String, name: String){
        //tempProductList.value?.addItem(Product(category, name))
        //database.insert(Product(category, name))
    }

    fun addItemsToTemporaryList(){
        //database code for adding items in inventory
    }

    override fun onCleared() {
        super.onCleared()
    }
}