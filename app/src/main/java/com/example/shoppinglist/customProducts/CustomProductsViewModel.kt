package com.example.shoppinglist.customProducts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shoppinglist.R
import com.example.shoppinglist.productList.Product
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.InputStream
import java.lang.Exception
import javax.xml.parsers.DocumentBuilderFactory

class CustomProductsViewModel(application: Application) : AndroidViewModel(application) {
    var customProductList = ArrayList<Product>()
    var productImagesDrawables = ArrayList<Int>()
    var customProduct = Product()

    init {
        productImagesDrawables = getProductImagesFromXML(application)
    }

    private fun getProductImagesFromXML(app: Application): ArrayList<Int>{

        val productImagesList = ArrayList<Int>()
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
                    if (node != null && node.nodeType == Node.ELEMENT_NODE){
                        val el = node as Element
                        productImagesList.add(R.drawable::class.java.getField(getValue("productImageDrawable", el).toLowerCase()).getInt(null))
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }

        return productImagesList
    }

    private fun getValue(tag: String, element: Element): String {
        val nodeList = element.getElementsByTagName(tag).item(0).childNodes
        val node = nodeList.item(0)
        return node.nodeValue
    }
}
