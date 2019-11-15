package com.example.shoppinglist.listBuilder

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import android.widget.Toast

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentListBuilderBinding
import com.google.firebase.database.FirebaseDatabase

class ListBuilderFragment : Fragment() {

    //private lateinit var viewModel: ListBuilderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: FragmentListBuilderBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_builder, container, false)

        val application = requireNotNull(this.activity).application
        //Create an instance of the View Model Factory
        //val dataSource = ProductDatabase.getInstance(application).productDatabaseDAO
        val viewModelFactory = ListBuilderViewModelFactory(application)

        val listBuilderViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(ListBuilderViewModel::class.java)
        //binding.setLifecycleOwner(this)

        binding.testTextView.setOnClickListener(){
            binding.listView.visibility = View.VISIBLE
        }

        //sets onclick listener for all elements in list;
        binding.listView.setOnItemClickListener(){ parent, view, position, id ->
            val element = parent.getItemAtPosition(position)
            Toast.makeText(this.context, element.toString(), Toast.LENGTH_SHORT).show()
            listBuilderViewModel.addItemsToTemporaryList("test_category", element.toString())

        }

        binding.listBuilderDoneButton.setOnClickListener(){
            //listBuilderViewModel.addItemsToInventory()
        }

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        return binding.root
    }
}
