package com.example.shoppinglist.database

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentInventoryBinding
import com.example.shoppinglist.productList.Product
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class FirebaseDatabaseHelper (var activeListReference: DatabaseReference =
                                  FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().currentUser?.displayName.toString()).child("ACTIVE"),
                              var inventoryReference: DatabaseReference =
                                  FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().currentUser?.displayName.toString()).child("INVENTORY"),
                              var customProductsReference: DatabaseReference =
                                  FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().currentUser?.displayName.toString()).child("CUSTOM")){

    init {

    }

    fun addItemsToActiveList(items: ArrayList<Product>){
        if (items.size > 0) {
            for (product in items) {
                if (product.quantity > 0) {
                    activeListReference.child(product.name).setValue(product)
                }
            }
            items.removeAll(items)
        }
    }

    fun addItemsToInventory(activeList: ArrayList<Product>){
        val inventoryValueEventListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var inventory = ArrayList<Product?>()
                for (data in p0.children){
                    val item = data.getValue(Product::class.java)
                    inventory.add(item)
                }

                if (activeList.size > 0) {
                    for (product in activeList) {
                        if (product.checked) {

                            if (inventory.contains(product)){
                                product.quantity += inventory.elementAt(inventory.indexOf(product))!!.quantity
                            }
                            product.previousQuantity = product.quantity

                            inventoryReference.child(product.name).setValue(product)
                            inventoryReference.child(product.name).child("checked").removeValue()
                            activeListReference.child(product.name).removeValue()
                        }
                    }
                    activeList.removeAll(activeList)
                }
            }
        }

        inventoryReference.addListenerForSingleValueEvent(inventoryValueEventListener)
    }

    fun updateQuantities(items: ArrayList<Product>){
        for (product in items){
            inventoryReference.child(product.name).child("quantity").setValue(product.quantity)
            inventoryReference.child(product.name).child("previousQuantity").setValue(product.previousQuantity)
        }
    }

    fun restock(inventory: ArrayList<Product>){
        val generatedActiveList = ArrayList<Product>()
        for (product in inventory){
            val lowStock = product.quantity <= (product.previousQuantity * 1/3)
            if (lowStock){
                product.quantity = product.previousQuantity - product.quantity
                generatedActiveList.add(product)
            }
        }
        addItemsToActiveList(generatedActiveList)
    }

    fun createCustomProduct(product: Product){
        try {
            product.isCustom = true
            product.quantity = 1
            product.categoryImageDrawable =
                R.drawable::class.java.getField("category_custom")
                    .getInt(null)
            customProductsReference.child(product.name).setValue(product)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}