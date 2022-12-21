package com.example.project_01_shopping_app.data.entity

data class OrderEntity(
    val id: String,
    val userId: String,
    val productId: Long,
    val productList: List<ProductEntity>
)