package com.example.shoppinglist.productAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.productList.Product
import kotlinx.android.synthetic.main.item_product_view.view.labelTextView
import kotlinx.android.synthetic.main.selected_product_view.view.*

class SelectedProductAdapter(val productList: ArrayList<Product>): RecyclerView.Adapter<SelectedProductViewHolder>(){

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: SelectedProductViewHolder, position: Int) {
        val item = productList[position]
        holder.itemView.labelTextView.text = item.name
        holder.itemView.itemSelectedImage.setImageResource(item.productImageDrawable)
        holder.itemView.itemSelectedImage.setOnClickListener(){
            if (item.quantity > 0){
                item.quantity -= 1
                if (item.quantity == 0){
                    holder.itemView.quantityOuterBorder.setImageResource(android.R.drawable.presence_offline)
                } else {
                    holder.itemView.quantityOuterBorder.setImageResource(android.R.drawable.presence_online)
                }
            } else {
                productList.remove(item)
            }
            notifyDataSetChanged()
        }
        holder.itemView.quantityLabel.text = item.quantity.toString()

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedProductViewHolder {
        return SelectedProductViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.selected_product_view, parent, false)
        )
    }
}