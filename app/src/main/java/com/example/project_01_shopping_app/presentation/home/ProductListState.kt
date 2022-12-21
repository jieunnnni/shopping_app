package com.example.project_01_shopping_app.presentation.home

import com.example.project_01_shopping_app.data.entity.ProductEntity

sealed class ProductListState {
    object UnInitialized: ProductListState()

    object Loading: ProductListState()

    data class Success(
        val productList: List<ProductEntity>
    ): ProductListState()

    object Error: ProductListState()
}