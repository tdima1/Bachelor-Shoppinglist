package com.example.shoppinglist.activeList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ActiveListViewModelFactory(
    private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActiveListViewModel::class.java)){
            return ActiveListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}