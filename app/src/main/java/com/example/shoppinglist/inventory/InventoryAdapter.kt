package com.example.shoppinglist.inventory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.productList.Product
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.item_inventory_view.view.*

class InventoryAdapter (val context: Context?, val ref: DatabaseReference, val productList: ArrayList<Product>): RecyclerView.Adapter<InventoryViewHolder>(){

    private val inventoryReference = ref
    private var mChildEventListener: ChildEventListener

    private val childEventListener = object :  ChildEventListener {
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            val changedProductCurrentData = p0.getValue(Product::class.java)
            if (productList.contains(changedProductCurrentData)) {
                productList.set(
                    productList.indexOf(changedProductCurrentData),
                    changedProductCurrentData!!
                )
            }
            notifyDataSetChanged()
        }


        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val product = p0.getValue(Product::class.java)
            if (product != null) {
                productList.add(product)
            }
            notifyItemInserted(productList.size - 1)
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            val product = p0.getValue(Product::class.java)
            if (productList.contains(product)){
                productList.remove(product)
            }
            notifyDataSetChanged()
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    init {
        ref.addChildEventListener(childEventListener)
        mChildEventListener = childEventListener
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = productList[position]

        holder.itemView.labelCategoryName.text = item.category
        holder.itemView.labelItemName.text = item.name
        holder.itemView.labelQuantity.text = item.quantity.toString()
        holder.itemView.productImage.setImageResource(item.productImageDrawable)
        holder.itemView.categoryImage.setImageResource(item.categoryImageDrawable)

        colorBlockForEmptyProduct(holder, item)

        holder.itemView.increaseButton.setOnClickListener(){
            item.quantity += 1
            if (item.quantity > item.previousQuantity) {
                item.previousQuantity = item.quantity
            }
            inventoryReference.child(item.name).child("quantity").setValue(item.quantity)
            inventoryReference.child(item.name).child("previousQuantity").setValue(item.previousQuantity)
        }
        holder.itemView.decreaseButton.setOnClickListener(){
            if (item.quantity > 0){
                item.quantity -= 1
                inventoryReference.child(item.name).child("quantity").setValue(item.quantity)
                inventoryReference.child(item.name).child("previousQuantity").setValue(item.previousQuantity)
            }
        }

        holder.itemView.deleteButton.setOnClickListener(){
            inventoryReference.child(item.name).setValue(null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_inventory_view, parent, false)
        )
    }

    fun colorBlockForEmptyProduct(holder: InventoryViewHolder, product: Product){
        if (product.quantity == 0) {
            holder.itemView.constraintLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorInventoryBackGround))
        } else {
            holder.itemView.constraintLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
        }
    }

    fun cleanupListener() {
        if (mChildEventListener != null) {
            inventoryReference.removeEventListener(mChildEventListener)
        }
    }

    fun sortByCategory(){

    }
}


