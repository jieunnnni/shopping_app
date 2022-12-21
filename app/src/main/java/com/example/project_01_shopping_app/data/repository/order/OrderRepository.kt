package com.example.project_01_shopping_app.data.repository.order

import com.example.project_01_shopping_app.data.entity.ProductEntity

interface OrderRepository {

    suspend fun orderProduct(
        userId: String,
        productId: Long,
        productList: List<ProductEntity>
    ): DefaultOrderRepository.Result
}