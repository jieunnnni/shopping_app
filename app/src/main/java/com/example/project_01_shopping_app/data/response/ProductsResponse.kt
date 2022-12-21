package com.example.project_01_shopping_app.data.response

data class ProductsResponse(
    val items: List<ProductResponse>,
    val count: Int
)