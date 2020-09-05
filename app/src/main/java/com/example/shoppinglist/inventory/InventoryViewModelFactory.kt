package com.example.shoppinglist.inventory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class InventoryViewModelFactory(
    private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)){
            return InventoryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}