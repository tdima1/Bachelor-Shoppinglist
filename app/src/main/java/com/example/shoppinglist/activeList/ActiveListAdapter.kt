package com.example.shoppinglist.activeList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.productList.Product
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.active_list_item_view.view.*
import java.util.*
import kotlin.collections.ArrayList

class ActiveListAdapter(val ref: DatabaseReference, val activeList: ArrayList<Product>): RecyclerView.Adapter<ActiveListViewHolder>(){

    private val activeListReference = ref
    private var mActiveListChildEventListener: ChildEventListener

    private val activeListChildEventListener = object :  ChildEventListener {
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }
        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            val changedProductCurrentData = p0.getValue(Product::class.java)

            if (activeList.contains(changedProductCurrentData)) {
                if (changedProductCurrentData!!.checked != activeList.elementAt(activeList.indexOf(changedProductCurrentData)).checked) {
                    if (changedProductCurrentData.checked) {
                        activeList.add(changedProductCurrentData)
                        activeList.remove(changedProductCurrentData)
                    } else {
                        activeList.remove(changedProductCurrentData)
                        activeList.add(0, changedProductCurrentData)
                    }
                }
                notifyDataSetChanged()
            }
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val product = p0.getValue(Product::class.java)
                if (product != null) {
                    if (product.checked){
                        activeList.add(product)
                        notifyItemInserted(activeList.size - 1)
                    } else {
                        activeList.add(0, product)
                        notifyItemInserted(0)
                    }
                }
            notifyItemInserted(activeList.size - 1)
        }

        override fun onChildRemoved(p0: DataSnapshot) {
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    init {
        ref.addChildEventListener(activeListChildEventListener)
        mActiveListChildEventListener = activeListChildEventListener
    }

    override fun getItemCount() = activeList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveListViewHolder {
        return ActiveListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.active_list_item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActiveListViewHolder, position: Int) {
        val item = activeList[position]

        holder.itemView.labelItemName.text = item.name
        holder.itemView.labelQuantity.text = item.quantity.toString()
        holder.itemView.productImage.setImageResource(item.productImageDrawable)
        holder.itemView.inCartCheckBox.isChecked = item.checked

        holder.itemView.increaseButton.setOnClickListener(){
            item.quantity += 1
            activeListReference.child(item.name).child("quantity").setValue(item.quantity)
            //notifyDataSetChanged()
        }
        holder.itemView.decreaseButton.setOnClickListener(){
            if (item.quantity > 0){
                item.quantity -= 1
                activeListReference.child(item.name).child("quantity").setValue(item.quantity)
                //notifyDataSetChanged()
            }
        }

        holder.itemView.inCartCheckBox.setOnClickListener(){
            activeListReference.child(item.name).child("checked").setValue(!item.checked)
        }
    }

    fun cleanupListener() {
        if (mActiveListChildEventListener != null) {
            activeListReference.removeEventListener(mActiveListChildEventListener)
        }
    }
}