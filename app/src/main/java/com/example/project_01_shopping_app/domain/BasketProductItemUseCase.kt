package com.example.project_01_shopping_app.domain

import com.example.project_01_shopping_app.data.entity.ProductEntity
import com.example.project_01_shopping_app.data.repository.product.ProductRepository

class BasketProductItemUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke(productEntity: ProductEntity): Long {
        return productRepository.insertProductItem(productEntity)
    }
}