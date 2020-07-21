package com.jammuwalastore.model

data class OrderModel(
    var number: String?,
    var status: String?,
    var date_created: String?,
    var total: String?,
    var payment_method: String?,
    var payment_method_title: String?,
    val line_items: ArrayList<line_items>
)

data class line_items(
    var product_id: Long?,
    var name: String?,
    var quantity: Long?,
    var price: String?,
    var total: Long?,
    var total_tax: String? = "0"
)