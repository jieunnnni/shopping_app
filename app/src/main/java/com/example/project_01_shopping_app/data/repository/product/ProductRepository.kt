package com.example.project_01_shopping_app.data.repository.product

import com.example.project_01_shopping_app.data.entity.ProductEntity

interface ProductRepository {

    suspend fun getProductList(): List<ProductEntity>

    suspend fun getAllProductListInBasket(): List<ProductEntity>

    suspend fun insertProductItem(ProductItem: ProductEntity): Long

    suspend fun insertProductList(ProductList: List<ProductEntity>)

    suspend fun updateProductItem(ProductItem: ProductEntity)

    suspend fun getProductItem(itemId: Long): ProductEntity?

    suspend fun deleteAll()

    suspend fun deleteProductItem(id: Long)
}