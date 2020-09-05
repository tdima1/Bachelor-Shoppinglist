package com.example.shoppinglist.productAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.productList.Product
import kotlinx.android.synthetic.main.item_product_view.view.*

class ProductAdapter(val productList: List<Product>,
                     private val onClickListener: (Product) -> Unit
): RecyclerView.Adapter<ProductViewHolder>(){

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productList[position]
        holder.itemView.labelTextView.text = item.name
        holder.itemView.productImageButton.setImageResource(item.productImageDrawable)
        holder.itemView.productImageButton.setOnClickListener(){
            onClickListener.invoke(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_view, parent, false)
        )
    }
}