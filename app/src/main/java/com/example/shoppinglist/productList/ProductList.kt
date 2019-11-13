package com.example.shoppinglist.productList

class ProductList (){

    private lateinit var items: MutableList<Product>

    fun addItem(element: Product){
        items.add(element)
    }

    fun addItem(elements: MutableList<Product>){
        for ( it in elements){
            items.add(it)
        }
    }
}