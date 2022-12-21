package com.example.project_01_shopping_app.domain

import com.example.project_01_shopping_app.data.repository.product.ProductRepository

class ClearBasketProductsUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke() {
        return productRepository.deleteAll()
    }
}