package com.example.shoppinglist.customProducts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.productList.Product
import kotlinx.android.synthetic.main.product_image_view.view.*

class ProductImagesAdapter(val context: Context?,
                           private val customProduct: Product,
                           private var lastClickedImage: ImageView,
                           val imagesList: ArrayList<Int>): RecyclerView.Adapter<ProductImagesViewHolder>(){
    override fun getItemCount() = imagesList.size

    override fun onBindViewHolder(holder: ProductImagesViewHolder, position: Int) {
        val item = imagesList[position]
        holder.itemView.productImageView.setImageResource(item)
        holder.itemView.productImageView.setOnClickListener(){
            customProduct.category = "Custom"
            customProduct.productImageDrawable = item
            holder.itemView.productImageView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorCustomProductImageBackGround))
            if (lastClickedImage != null && lastClickedImage != holder.itemView.productImageView){
                lastClickedImage.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
            }
            lastClickedImage = holder.itemView.productImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImagesViewHolder {
        return ProductImagesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product_image_view, parent, false)
        )
    }
}