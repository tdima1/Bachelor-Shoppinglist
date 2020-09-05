package com.example.shoppinglist.productList

import com.example.shoppinglist.R
import java.util.HashMap

class Product(){

    var category: String = ""
    var name: String = ""
    var quantity: Int = 0
    var checked: Boolean = false
    var isCustom: Boolean = false
    var previousQuantity: Int = 0
    var productImageDrawable: Int = R.drawable.ic_apple_1
    var categoryImageDrawable: Int = R.drawable.category_bakery

    constructor(category: String, name: String, quantity: Int): this(){
        this.category = category
        this.name = name
        this.quantity = quantity
//        if (!isCustom) {
//            this.productImageDrawable =
//                R.drawable::class.java.getField(name.toLowerCase()).getInt(null)
//        }
    }

    constructor(category: String, name: String, productImage: String):this(){
        this.category = category
        this.name = name
        this.quantity = 1
        if (!isCustom) {
            this.productImageDrawable =
                R.drawable::class.java.getField(productImage.toLowerCase()).getInt(null)
            this.categoryImageDrawable =
                R.drawable::class.java.getField("category_" + category.toLowerCase()).getInt(null)
        }
    }

    init {
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["category"] = category
        result["name"] = name
        result["quantity"] = quantity

        return result
    }

    override fun equals(other: Any?): Boolean {
        super.equals(other)
        val ot = other as Product
        return this.name.equals(ot.name) && this.category.equals(ot.category)
    }

}