package com.example.shoppinglist.listBuilder

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class SharedListBuilderViewModelFactory(
    private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedListBuilderViewModel::class.java)){
            return SharedListBuilderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}