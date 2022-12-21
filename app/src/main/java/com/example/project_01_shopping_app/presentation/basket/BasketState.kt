package com.example.project_01_shopping_app.presentation.basket

import androidx.annotation.StringRes
import com.example.project_01_shopping_app.data.entity.ProductEntity

sealed class BasketState {

    object Uninitialized: BasketState()

    object Loading: BasketState()

    data class Success(
        val productList: List<ProductEntity> = listOf()
    ): BasketState()

    object Order: BasketState()

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): BasketState()
}