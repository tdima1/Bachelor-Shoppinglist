package com.example.shoppinglist.productAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentListBuilderBinding
import com.example.shoppinglist.listBuilder.ListBuilderViewModel
import com.example.shoppinglist.productList.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.item_category_view.view.*

class CategoryAdapter(val categoriesList: ArrayList<String>,
                      val viewModel: ListBuilderViewModel,
                      val binding: FragmentListBuilderBinding,
                      private val customProductsReference: DatabaseReference): RecyclerView.Adapter<ProductViewHolder>(){

    //private val customProductsReference = FirebaseDatabase.getInstance().getReference(strCurrentUserUID).child("CUSTOM")
    private val mCustomProductsChildEventListener: ChildEventListener

    private val childEventListener = object : ChildEventListener {
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val product = p0.getValue(Product::class.java)
            if (product != null && !viewModel.productsList.contains(product)) {
                viewModel.productsList.add(product)
            }
            notifyDataSetChanged()
        }

        override fun onChildRemoved(p0: DataSnapshot) {
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    init {
        customProductsReference.addChildEventListener(childEventListener)
        mCustomProductsChildEventListener = childEventListener
        if (!categoriesList.contains("Custom")) {
            categoriesList.add("Custom")
        }
    }

    override fun getItemCount() = categoriesList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = categoriesList[position]

        holder.itemView.labelCategoryName.text = item
        holder.itemView.labelCategoryName.setOnClickListener(){
            if (holder.itemView.itemRecyclerView.visibility.equals(View.VISIBLE)){
                holder.itemView.itemRecyclerView.visibility = View.GONE
            } else {
                holder.itemView.itemRecyclerView.visibility = View.VISIBLE
            }
        }

        holder.itemView.itemRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 4)
        holder.itemView.itemRecyclerView.adapter =
            ProductAdapter(viewModel.getItemsFromCategory(item),
                onClickListener = {product -> addItemsToSelectedItemsList(product, binding, viewModel)
                })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category_view, parent, false)
        )
    }

    fun addItemsToSelectedItemsList(product: Product, binding: FragmentListBuilderBinding, viewModel: ListBuilderViewModel){
        viewModel.addItemsToSelectedItemsList(product)
        binding.recyclerViewSelectedItems.adapter =
            SelectedProductAdapter(viewModel.selectedProductsList)
    }

    fun cleanupListener() {
        if (mCustomProductsChildEventListener != null) {
            customProductsReference.removeEventListener(mCustomProductsChildEventListener)
        }
    }

}