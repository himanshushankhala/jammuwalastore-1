package com.jammuwalastore.model

/**
 * Created by ABC on 27-Jun-18.
 */


data class ResponseMessage(
    val message: String
)


data class SliderUrlList(
    val arrayList: ArrayList<SliderUrl>
)

data class SliderUrl(
    val url: String
)

data class ProductsData(
    val id: Long,
    val name: String,
    val price: String,
    val regular_price: String,
    val sale_price: String,
    val stock_status: String,
    var weight: String,
    var quantity: Int = 0,
    val description: String,
    val tax_status: String,
    val tax_class: String,
    val images: ArrayList<images>?,
    val attributes: ArrayList<attributes>?,
    val categories: ArrayList<categories>?

)

data class images(
    val src: String?
)

data class attributes(
    val options: ArrayList<String>?
)

data class categories(
    val name: String?
)