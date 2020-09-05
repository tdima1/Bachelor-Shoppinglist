package com.example.shoppinglist.customProducts

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class CustomProductsViewModelFactory(
    private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomProductsViewModel::class.java)){
            return CustomProductsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}