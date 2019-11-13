package com.example.shoppinglist.listBuilder

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import com.example.shoppinglist.database.ProductDatabaseDAO
import java.lang.IllegalArgumentException

class ListBuilderViewModelFactory(
    private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListBuilderViewModel::class.java)){
            return ListBuilderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}