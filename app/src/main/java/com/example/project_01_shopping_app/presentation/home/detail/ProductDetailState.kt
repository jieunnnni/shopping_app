package com.example.project_01_shopping_app.presentation.home.detail

import com.example.project_01_shopping_app.data.entity.ProductEntity

sealed class ProductDetailState {

    object UnInitialized: ProductDetailState()

    object Loading: ProductDetailState()

    data class Success(
        val productEntity: ProductEntity,
        val isLiked: Boolean? = null
    ): ProductDetailState()

    object Basket: ProductDetailState()

    // TODO Activity 에 추가해서 RESULT_CODE 이용해서 어댑터 넣고 like된 항목 붙여주기
    object Like: ProductDetailState()

    object Error: ProductDetailState()
}