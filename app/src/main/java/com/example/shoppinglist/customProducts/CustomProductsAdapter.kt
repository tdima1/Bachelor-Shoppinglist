package com.example.shoppinglist.customProducts

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.productList.Product
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.item_product_view.view.*

class CustomProductsAdapter(val ref: DatabaseReference, val customProductList: ArrayList<Product>): RecyclerView.Adapter<CustomProductsViewHolder>(){

    private var mChildEventListener: ChildEventListener
    private val childEventListener = object : ChildEventListener {
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val product = p0.getValue(Product::class.java)
            if (product != null) {
                customProductList.add(product)
            }
            notifyItemInserted(customProductList.size - 1)
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            val product = p0.getValue(Product::class.java)
            if (customProductList.contains(product!!)) {
                customProductList.remove(product)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    init {
        ref.addChildEventListener(childEventListener)
        mChildEventListener = childEventListener
    }

    override fun getItemCount() = customProductList.size

    override fun onBindViewHolder(holder: CustomProductsViewHolder, position: Int) {
        val item = customProductList[position]
        holder.itemView.productImageButton.setImageResource(item.productImageDrawable)
        holder.itemView.labelTextView.text = item.name
        holder.itemView.productImageButton.setOnClickListener(){
            customProductList.removeAt(position)
            ref.child(item.name).removeValue()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomProductsViewHolder {
        return CustomProductsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_view, parent, false)
        )
    }

    fun cleanupListener() {
        if (mChildEventListener != null) {
            ref.removeEventListener(mChildEventListener)
        }
    }

}