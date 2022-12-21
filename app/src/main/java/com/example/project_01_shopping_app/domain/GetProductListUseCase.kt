package com.example.project_01_shopping_app.domain

import com.example.project_01_shopping_app.data.entity.ProductEntity
import com.example.project_01_shopping_app.data.repository.product.ProductRepository

class GetProductListUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke(): List<ProductEntity> {
        return productRepository.getProductList()
    }
}