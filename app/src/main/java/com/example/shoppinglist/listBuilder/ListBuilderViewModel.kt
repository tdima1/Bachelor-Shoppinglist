package com.example.shoppinglist.listBuilder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shoppinglist.database.FirebaseDatabaseHelper
import com.example.shoppinglist.productList.Product
import org.w3c.dom.Node
import java.io.InputStream
import java.lang.Exception
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.time.microseconds
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import org.w3c.dom.Element

open class ListBuilderViewModel(
    application: Application) : AndroidViewModel(application) {

    val selectedProductsList = ArrayList<Product>()
    val productsList = parseProductsXML(application)

    open fun addItemsToSelectedItemsList(product: Product){
        if (selectedProductsList.contains(product)){
            selectedProductsList.elementAt(selectedProductsList.indexOf(product)).quantity += 1
        } else {
            selectedProductsList.add(product)
        }
    }

    open fun addItemsToActiveList(){
        //database code for adding items in inventory
        val databaseHelper = FirebaseDatabaseHelper()
        databaseHelper.addItemsToActiveList(selectedProductsList)
    }

    open fun getCategoriesList(): ArrayList<String>{
        val list = ArrayList<String>()
        for (product in productsList){
            if (!list.contains(product.category)){
                list.add(product.category)
            }
        }
        return list
    }

    fun getItemsFromCategory(category: String): List<Product>{
        val list = ArrayList<Product>()
        for (product in productsList){
            if (product.category == category){
                list.add(product)
            }
        }
        return list
    }

    private fun parseProductsXML(app: Application): ArrayList<Product>{
        val productList = ArrayList<Product>()

        try {
            val inputStream: InputStream = app.assets.open("products.xml")
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc = dBuilder.parse(inputStream)

            val element = doc.documentElement
            element.normalize()

            val nodeList = doc.getElementsByTagName("product")

            for (i in 0..nodeList.length){
                val node = nodeList.item(i)
                if (node.nodeType == Node.ELEMENT_NODE){
                    val el = node as Element
                    var p = Product(getValue("category", el), getValue("name", el),
                        getValue("productImageDrawable", el))
                    productList.add(p)
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
        return productList
    }

    private fun getValue(tag: String, element: Element): String {
        val nodeList = element.getElementsByTagName(tag).item(0).childNodes
        val node = nodeList.item(0)
        return node.nodeValue
    }

}