package com.example.shoppinglist.accountManagement

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.user.User
import kotlinx.android.synthetic.main.product_image_view.view.*

class AvatarAdapter(val context: Context?,
                    private val newUser: User,
                    private var lastClickedImage: ImageView,
                    val imagesList: ArrayList<Int>): RecyclerView.Adapter<AvatarViewHolder>(){

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatar = imagesList[position]
        holder.itemView.productImageView.setImageResource(avatar)

        holder.itemView.productImageView.setOnClickListener() {
            newUser.avatar = avatar
            holder.itemView.productImageView.setBackgroundColor(
                ContextCompat.getColor(context!!, R.color.colorCustomProductImageBackGround)
            )
            if (lastClickedImage != null && lastClickedImage != holder.itemView.productImageView) {
                lastClickedImage.setBackgroundColor(
                    ContextCompat.getColor(context!!, R.color.white)
                )
            }
            lastClickedImage = holder.itemView.productImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        return AvatarViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.product_image_view, parent, false)
        )
    }
    override fun getItemCount() = imagesList.size
}