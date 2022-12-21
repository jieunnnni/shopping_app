package com.example.project_01_shopping_app.data.repository.user

import com.example.project_01_shopping_app.data.entity.ProductEntity

interface UserRepository {

    suspend fun getUserLikedProduct(id: Long): ProductEntity?

    suspend fun getAllUserLikedProduct(): List<ProductEntity>

    suspend fun insertUserLikedProduct(productEntity: ProductEntity): Long

    suspend fun deleteUserLikedProduct(id: Long)

    suspend fun deleteAllUserLikedProduct()

}