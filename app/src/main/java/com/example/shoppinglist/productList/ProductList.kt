package com.example.shoppinglist.productList

class ProductList (){

    public lateinit var items: ArrayList<Product>

    constructor(prodList: ArrayList<Product>) : this() {
        addItems(prodList)
    }

    fun addItem(element: Product){
        items.add(element)
    }

    fun addItems(elements: ArrayList<Product>){
        for ( it in elements){
            items.add(it)
        }
    }
}