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
import com.example.shoppinglist.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Element

class SharedListBuilderViewModel(
    application: Application) : ListBuilderViewModel(application) {

    var currentUserData: User? =  User()
    private val currentUserDisplayName = FirebaseAuth.getInstance().currentUser!!.displayName.toString()
    private val usersReference = FirebaseDatabase.getInstance().getReference("USERS")
    private val groupsReference = FirebaseDatabase.getInstance().getReference("GROUPS")

    override fun addItemsToSelectedItemsList(product: Product){
        if (selectedProductsList.contains(product)){
            selectedProductsList.elementAt(selectedProductsList.indexOf(product)).quantity += 1
        } else {
            selectedProductsList.add(product)
        }
    }

    override fun addItemsToActiveList(){
        //database code for adding items in active list
        val currentUserValueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUserData = p0.getValue(User::class.java)

                val sharedActiveListReference = groupsReference.child(currentUserData!!.sharedGroupName).child("ACTIVE")
                val sharedInventoryReference = groupsReference.child(currentUserData!!.sharedGroupName).child("INVENTORY")
                val sharedCustomProductListReference = groupsReference.child(currentUserData!!.sharedGroupName).child("CUSTOM")
                val databaseHelper = FirebaseDatabaseHelper(sharedActiveListReference, sharedInventoryReference, sharedCustomProductListReference)
                databaseHelper.addItemsToActiveList(selectedProductsList)
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        //usersReference.orderByChild("personalGroupName").equalTo(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(currentUserValueEventListener)
        usersReference.child(currentUserDisplayName).addListenerForSingleValueEvent(currentUserValueEventListener)
    }

//    override fun getCategoriesList(): ArrayList<String>{
//        return super.getCategoriesList()
//    }
//
//    fun getItemsFromCategory(category: String): List<Product>{
//        val list = ArrayList<Product>()
//        for (product in productsList){
//            if (product.category == category){
//                list.add(product)
//            }
//        }
//        return list
//    }

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