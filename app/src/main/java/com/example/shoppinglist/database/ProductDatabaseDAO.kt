/*
package com.example.shoppinglist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shoppinglist.productList.Product

@Dao
interface ProductDatabaseDAO{
    @Insert
    fun insert(productEntity: ProductEntity)

    @Update
    fun update(productEntity: ProductEntity)

    @Query("DELETE FROM table_product")
    fun clear()

    @Query("SELECT * FROM table_product WHERE category = :key")
    fun selectAllProductsByCategory(key: String): LiveData<ProductEntity>
}*/
