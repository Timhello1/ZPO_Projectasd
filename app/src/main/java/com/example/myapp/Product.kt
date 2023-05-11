package com.example.myapp

data class Product(
    var description: String, val imageId: Int, var ingredients: String,
    var name: String, var prescription: Boolean,var price: String,
    val sellerId: String,var tags: String)
